package com.example.dependwife.game.domain.model

enum class WeaponType {
    KNIFE,
    CUTTING_BOARD,
    WASHING_MACHINE,
    TV
}

enum class UpgradeType {
    UPGRADE,
    UNLOCK
}

data class Enemy(
    val id: Int,
    val x: Float,
    val y: Float,
    val hp: Int,
    val maxHp: Int,
    val speed: Float,
    val isBoss: Boolean,
    val gaugeReward: Int
)

data class Projectile(
    val id: Int,
    val x: Float,
    val y: Float,
    val velocityX: Float,
    val velocityY: Float,
    val damage: Int,
    val weaponType: WeaponType,
    val remainingHits: Int,
    val hitEnemyIds: Set<Int>
)

data class WeaponState(
    val type: WeaponType,
    val name: String,
    val level: Int,
    val damage: Int,
    val fireIntervalSeconds: Float,
    val penetration: Int
)

data class UpgradeChoice(
    val weaponType: WeaponType?,
    val type: UpgradeType,
    val title: String,
    val description: String
)

data class ArenaMetrics(
    val width: Float,
    val height: Float,
    val defenseLineY: Float,
    val projectileStartX: Float,
    val projectileStartY: Float,
    val hitRadius: Float
)

data class GameState(
    val enemies: List<Enemy>,
    val projectiles: List<Projectile>,
    val weapons: List<WeaponState>,
    val score: Int,
    val defeatedEnemies: Int,
    val upgradeGauge: Int,
    val upgradeGaugeTarget: Int,
    val elapsedSeconds: Float,
    val spawnElapsedSeconds: Float,
    val fireElapsedSeconds: Map<WeaponType, Float>,
    val spawnedEnemies: Int,
    val nextEnemyId: Int,
    val nextProjectileId: Int,
    val upgradeChoices: List<UpgradeChoice>,
    val isUpgradeSelectionOpen: Boolean,
    val isGameOver: Boolean
)
