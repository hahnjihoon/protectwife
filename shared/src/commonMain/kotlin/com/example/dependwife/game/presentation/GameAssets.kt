package com.example.dependwife.game.presentation

import com.example.dependwife.game.domain.model.WeaponType
import com.example.dependwife.shared.resources.Res
import com.example.dependwife.shared.resources.weapon_board_1
import com.example.dependwife.shared.resources.weapon_board_2
import com.example.dependwife.shared.resources.weapon_board_3
import com.example.dependwife.shared.resources.weapon_knife_1
import com.example.dependwife.shared.resources.weapon_knife_2
import com.example.dependwife.shared.resources.weapon_knife_3
import com.example.dependwife.shared.resources.weapon_tv_1
import com.example.dependwife.shared.resources.weapon_tv_2
import com.example.dependwife.shared.resources.weapon_tv_3
import com.example.dependwife.shared.resources.weapon_washer_1
import com.example.dependwife.shared.resources.weapon_washer_2
import com.example.dependwife.shared.resources.weapon_washer_3
import org.jetbrains.compose.resources.DrawableResource

fun weaponImage(type: WeaponType, level: Int): DrawableResource {
    return when (type) {
        WeaponType.KNIFE -> when (level) {
            1 -> Res.drawable.weapon_knife_1
            2 -> Res.drawable.weapon_knife_2
            else -> Res.drawable.weapon_knife_3
        }
        WeaponType.CUTTING_BOARD -> when (level) {
            1 -> Res.drawable.weapon_board_1
            2 -> Res.drawable.weapon_board_2
            else -> Res.drawable.weapon_board_3
        }
        WeaponType.WASHING_MACHINE -> when (level) {
            1 -> Res.drawable.weapon_washer_1
            2 -> Res.drawable.weapon_washer_2
            else -> Res.drawable.weapon_washer_3
        }
        WeaponType.TV -> when (level) {
            1 -> Res.drawable.weapon_tv_1
            2 -> Res.drawable.weapon_tv_2
            else -> Res.drawable.weapon_tv_3
        }
    }
}
