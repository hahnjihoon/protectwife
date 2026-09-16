package com.example.dependwife.game.domain

import com.example.dependwife.game.domain.model.UpgradeChoice
import com.example.dependwife.game.domain.model.UpgradeType
import com.example.dependwife.game.domain.model.WeaponState
import com.example.dependwife.game.domain.model.WeaponType

object GameRules {
    const val NORMAL_ENEMY_HP = 100
    const val MID_BOSS_HP = 500
    const val INITIAL_UPGRADE_GAUGE_TARGET = 500
    const val UPGRADE_GAUGE_TARGET_STEP = 150
    const val NORMAL_ENEMY_GAUGE_REWARD = 100
    const val MID_BOSS_GAUGE_REWARD = 500

    fun initialWeapon(): WeaponState {
        return WeaponState(
            type = WeaponType.KNIFE,
            name = "식칼",
            level = 1,
            damage = 50,
            fireIntervalSeconds = 0.6f,
            penetration = 1
        )
    }

    fun unlockedWeapon(type: WeaponType): WeaponState {
        return when (type) {
            WeaponType.KNIFE -> initialWeapon()
            WeaponType.CUTTING_BOARD -> WeaponState(
                type = type,
                name = "도마",
                level = 1,
                damage = 70,
                fireIntervalSeconds = 0.9f,
                penetration = 1
            )
            WeaponType.WASHING_MACHINE -> WeaponState(
                type = type,
                name = "세탁기",
                level = 1,
                damage = 100,
                fireIntervalSeconds = 1.5f,
                penetration = 3
            )
            WeaponType.TV -> WeaponState(
                type = type,
                name = "TV",
                level = 1,
                damage = 150,
                fireIntervalSeconds = 2.0f,
                penetration = 5
            )
        }
    }

    fun upgradeChoices(weapons: List<WeaponState>): List<UpgradeChoice> {
        val choices = mutableListOf<UpgradeChoice>()

        weapons.forEach { weapon ->
            val damageIncrease = when (weapon.type) {
                WeaponType.KNIFE -> 25
                WeaponType.CUTTING_BOARD -> 35
                WeaponType.WASHING_MACHINE -> 50
                WeaponType.TV -> 75
            }
            choices += UpgradeChoice(
                weaponType = weapon.type,
                type = UpgradeType.UPGRADE,
                title = "${weapon.name} 강화",
                description = "공격력 +$damageIncrease / 발사 속도 증가 / ${if (weapon.level < 3) "외형 변경" else "최종 외형"}"
            )
        }

        val ownedTypes = weapons.map { it.type }.toSet()
        val unlockType = when {
            WeaponType.CUTTING_BOARD !in ownedTypes -> WeaponType.CUTTING_BOARD
            WeaponType.WASHING_MACHINE !in ownedTypes -> WeaponType.WASHING_MACHINE
            WeaponType.TV !in ownedTypes -> WeaponType.TV
            else -> null
        }
        if (unlockType != null) {
            val weapon = unlockedWeapon(unlockType)
            choices += UpgradeChoice(
                weaponType = unlockType,
                type = UpgradeType.UNLOCK,
                title = "${weapon.name} 해금",
                description = "공격력 ${weapon.damage} / ${weapon.fireIntervalSeconds}초 / 관통 ${weapon.penetration}"
            )
        }

        return choices
    }
}
