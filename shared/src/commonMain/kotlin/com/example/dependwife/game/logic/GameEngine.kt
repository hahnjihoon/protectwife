package com.example.dependwife.game.logic

import com.example.dependwife.game.domain.GameRules
import com.example.dependwife.game.domain.model.ArenaMetrics
import com.example.dependwife.game.domain.model.Enemy
import com.example.dependwife.game.domain.model.GameState
import com.example.dependwife.game.domain.model.Projectile
import com.example.dependwife.game.domain.model.UpgradeChoice
import com.example.dependwife.game.domain.model.UpgradeType
import com.example.dependwife.game.domain.model.WeaponState
import com.example.dependwife.game.domain.model.WeaponType
import kotlin.math.sqrt
import kotlin.random.Random

class GameEngine {
    fun initialState(): GameState {
        val knife = GameRules.initialWeapon()
        return GameState(
            enemies = emptyList(),
            projectiles = emptyList(),
            weapons = listOf(knife),
            score = 0,
            defeatedEnemies = 0,
            upgradeGauge = 0,
            upgradeGaugeTarget = GameRules.INITIAL_UPGRADE_GAUGE_TARGET,
            elapsedSeconds = 0f,
            spawnElapsedSeconds = 0f,
            fireElapsedSeconds = mapOf(knife.type to 0f),
            spawnedEnemies = 0,
            nextEnemyId = 1,
            nextProjectileId = 1,
            upgradeChoices = emptyList(),
            isUpgradeSelectionOpen = false,
            isGameOver = false
        )
    }

    fun update(state: GameState, deltaSeconds: Float, metrics: ArenaMetrics): GameState {
        if (state.isGameOver || state.isUpgradeSelectionOpen || metrics.width <= 0f || metrics.height <= 0f) {
            return state
        }

        var enemies = state.enemies.toMutableList()
        var projectiles = state.projectiles.toMutableList()
        var score = state.score
        var defeatedEnemies = state.defeatedEnemies
        var upgradeGauge = state.upgradeGauge
        var spawnedEnemies = state.spawnedEnemies
        var nextEnemyId = state.nextEnemyId
        var nextProjectileId = state.nextProjectileId
        val elapsedSeconds = state.elapsedSeconds + deltaSeconds
        var spawnElapsedSeconds = state.spawnElapsedSeconds + deltaSeconds
        val fireElapsedSeconds = state.fireElapsedSeconds.toMutableMap()
        var isGameOver = false

        val spawnInterval = (0.48f - elapsedSeconds * 0.0015f).coerceAtLeast(0.25f)
        val completedUpgrades = (
            (state.upgradeGaugeTarget - GameRules.INITIAL_UPGRADE_GAUGE_TARGET) /
                GameRules.UPGRADE_GAUGE_TARGET_STEP
            ).coerceAtLeast(0)
        val enemiesPerWave = (1 + completedUpgrades / 2).coerceAtMost(6)
        while (spawnElapsedSeconds >= spawnInterval) {
            spawnElapsedSeconds -= spawnInterval
            repeat(enemiesPerWave) {
                spawnedEnemies += 1
                val isBoss = spawnedEnemies % 12 == 0
                val margin = metrics.width * 0.1f
                enemies += Enemy(
                    id = nextEnemyId,
                    x = Random.nextFloat() * (metrics.width - margin * 2f) + margin,
                    y = if (isBoss) metrics.height * 0.08f else metrics.height * 0.04f,
                    hp = if (isBoss) GameRules.MID_BOSS_HP else GameRules.NORMAL_ENEMY_HP,
                    maxHp = if (isBoss) GameRules.MID_BOSS_HP else GameRules.NORMAL_ENEMY_HP,
                    speed = if (isBoss) 48f else Random.nextInt(72, 103).toFloat(),
                    isBoss = isBoss,
                    gaugeReward = if (isBoss) GameRules.MID_BOSS_GAUGE_REWARD else GameRules.NORMAL_ENEMY_GAUGE_REWARD
                )
                nextEnemyId += 1
            }
        }

        enemies = enemies.map { enemy ->
            enemy.copy(y = enemy.y + enemy.speed * deltaSeconds)
        }.toMutableList()
        if (enemies.any { it.y >= metrics.defenseLineY }) {
            isGameOver = true
        }

        if (!isGameOver) {
            state.weapons.forEach { weapon ->
                var weaponElapsed = (fireElapsedSeconds[weapon.type] ?: 0f) + deltaSeconds
                val target = enemies.maxByOrNull { it.y }
                if (target == null) {
                    weaponElapsed = weaponElapsed.coerceAtMost(weapon.fireIntervalSeconds)
                } else {
                    while (weaponElapsed >= weapon.fireIntervalSeconds) {
                        val projectileSpeed = 620f
                        val currentDeltaX = target.x - metrics.projectileStartX
                        val currentDeltaY = target.y - metrics.projectileStartY
                        val currentDistance = sqrt(
                            currentDeltaX * currentDeltaX + currentDeltaY * currentDeltaY
                        )
                        val interceptA = target.speed * target.speed - projectileSpeed * projectileSpeed
                        val interceptB = 2f * currentDeltaY * target.speed
                        val interceptC = currentDistance * currentDistance
                        val interceptRoot = sqrt(
                            (interceptB * interceptB - 4f * interceptA * interceptC).coerceAtLeast(0f)
                        )
                        val firstInterceptSeconds = (-interceptB + interceptRoot) / (2f * interceptA)
                        val secondInterceptSeconds = (-interceptB - interceptRoot) / (2f * interceptA)
                        val interceptSeconds = when {
                            firstInterceptSeconds > 0f && secondInterceptSeconds > 0f -> {
                                minOf(firstInterceptSeconds, secondInterceptSeconds)
                            }
                            firstInterceptSeconds > 0f -> firstInterceptSeconds
                            secondInterceptSeconds > 0f -> secondInterceptSeconds
                            else -> currentDistance / projectileSpeed
                        }
                        val aimY = target.y + target.speed * interceptSeconds
                        val deltaX = target.x - metrics.projectileStartX
                        val deltaY = aimY - metrics.projectileStartY
                        val distance = sqrt(deltaX * deltaX + deltaY * deltaY).coerceAtLeast(1f)
                        projectiles += Projectile(
                            id = nextProjectileId,
                            x = metrics.projectileStartX,
                            y = metrics.projectileStartY,
                            velocityX = deltaX / distance * projectileSpeed,
                            velocityY = deltaY / distance * projectileSpeed,
                            damage = weapon.damage,
                            weaponType = weapon.type,
                            remainingHits = weapon.penetration,
                            hitEnemyIds = emptySet()
                        )
                        nextProjectileId += 1
                        weaponElapsed -= weapon.fireIntervalSeconds
                    }
                }
                fireElapsedSeconds[weapon.type] = weaponElapsed
            }
        }

        val movedProjectiles = mutableListOf<Projectile>()
        projectiles.forEach { projectile ->
            val movedProjectile = projectile.copy(
                x = projectile.x + projectile.velocityX * deltaSeconds,
                y = projectile.y + projectile.velocityY * deltaSeconds
            )
            val hitEnemy = enemies
                .filter { it.id !in movedProjectile.hitEnemyIds }
                .filter { enemy ->
                    val distanceX = enemy.x - movedProjectile.x
                    val distanceY = enemy.y - movedProjectile.y
                    sqrt(distanceX * distanceX + distanceY * distanceY) <= metrics.hitRadius
                }
                .maxByOrNull { it.y }

            if (hitEnemy != null) {
                val remainingHp = hitEnemy.hp - movedProjectile.damage
                if (remainingHp <= 0) {
                    enemies.removeAll { it.id == hitEnemy.id }
                    score += hitEnemy.maxHp
                    defeatedEnemies += 1
                    upgradeGauge += hitEnemy.gaugeReward
                } else {
                    val enemyIndex = enemies.indexOfFirst { it.id == hitEnemy.id }
                    if (enemyIndex >= 0) {
                        enemies[enemyIndex] = hitEnemy.copy(hp = remainingHp)
                    }
                }

                val hitEnemyIds = movedProjectile.hitEnemyIds + hitEnemy.id
                val remainingHits = movedProjectile.remainingHits - 1
                if (remainingHits > 0) {
                    movedProjectiles += movedProjectile.copy(
                        remainingHits = remainingHits,
                        hitEnemyIds = hitEnemyIds
                    )
                }
            } else if (
                movedProjectile.x >= -100f &&
                movedProjectile.x <= metrics.width + 100f &&
                movedProjectile.y >= -100f &&
                movedProjectile.y <= metrics.height + 100f
            ) {
                movedProjectiles += movedProjectile
            }
        }
        projectiles = movedProjectiles

        val isUpgradeSelectionOpen = upgradeGauge >= state.upgradeGaugeTarget
        if (isUpgradeSelectionOpen) {
            upgradeGauge -= state.upgradeGaugeTarget
        }

        return state.copy(
            enemies = enemies,
            projectiles = projectiles,
            score = score,
            defeatedEnemies = defeatedEnemies,
            upgradeGauge = upgradeGauge,
            elapsedSeconds = elapsedSeconds,
            spawnElapsedSeconds = spawnElapsedSeconds,
            fireElapsedSeconds = fireElapsedSeconds,
            spawnedEnemies = spawnedEnemies,
            nextEnemyId = nextEnemyId,
            nextProjectileId = nextProjectileId,
            upgradeChoices = if (isUpgradeSelectionOpen) GameRules.upgradeChoices(state.weapons) else emptyList(),
            isUpgradeSelectionOpen = isUpgradeSelectionOpen,
            isGameOver = isGameOver
        )
    }

    fun applyUpgrade(state: GameState, choice: UpgradeChoice): GameState {
        val weapons = when (choice.type) {
            UpgradeType.UNLOCK -> {
                val weaponType = choice.weaponType ?: return state
                state.weapons + GameRules.unlockedWeapon(weaponType)
            }
            UpgradeType.UPGRADE -> state.weapons.map { weapon ->
                if (weapon.type == choice.weaponType) {
                    val damageIncrease = when (weapon.type) {
                        WeaponType.KNIFE -> 25
                        WeaponType.CUTTING_BOARD -> 35
                        WeaponType.WASHING_MACHINE -> 50
                        WeaponType.TV -> 75
                    }
                    val minimumFireInterval = when (weapon.type) {
                        WeaponType.KNIFE -> 0.30f
                        WeaponType.CUTTING_BOARD -> 0.45f
                        WeaponType.WASHING_MACHINE -> 0.75f
                        WeaponType.TV -> 1.00f
                    }
                    weapon.copy(
                        level = weapon.level + 1,
                        damage = weapon.damage + damageIncrease,
                        fireIntervalSeconds = (weapon.fireIntervalSeconds * 0.97f)
                            .coerceAtLeast(minimumFireInterval)
                    )
                } else {
                    weapon
                }
            }
        }

        return state.copy(
            weapons = weapons,
            fireElapsedSeconds = weapons.associate { weapon ->
                weapon.type to (state.fireElapsedSeconds[weapon.type] ?: 0f)
            },
            upgradeGaugeTarget = state.upgradeGaugeTarget + GameRules.UPGRADE_GAUGE_TARGET_STEP,
            upgradeChoices = emptyList(),
            isUpgradeSelectionOpen = false
        )
    }
}
