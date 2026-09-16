package com.example.dependwife.game.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dependwife.game.domain.model.ArenaMetrics
import com.example.dependwife.game.domain.model.GameState
import com.example.dependwife.shared.resources.Res
import com.example.dependwife.shared.resources.enemy_common
import com.example.dependwife.shared.resources.enemy_midboss
import com.example.dependwife.shared.resources.husband_character
import com.example.dependwife.shared.resources.wife_character
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

@Composable
fun GameArena(
    state: GameState,
    onMetricsChanged: (ArenaMetrics) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .clipToBounds()
            .background(Color(0xFF172238))
            .onSizeChanged { size ->
                onMetricsChanged(
                    ArenaMetrics(
                        width = size.width.toFloat(),
                        height = size.height.toFloat(),
                        defenseLineY = size.height - with(density) { 175.dp.toPx() },
                        projectileStartX = size.width / 2f,
                        projectileStartY = size.height - with(density) { 145.dp.toPx() },
                        hitRadius = with(density) { 38.dp.toPx() }
                    )
                )
            }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp)
                .size(92.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.wife_character),
                contentDescription = "아내",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 88.dp)
                .size(98.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.husband_character),
                contentDescription = "남편",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        state.enemies.forEach { enemy ->
            val enemySize = if (enemy.isBoss) 84.dp else 58.dp
            val enemySizePx = with(density) { enemySize.toPx() }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset {
                    IntOffset(
                        x = (enemy.x - enemySizePx / 2f).roundToInt(),
                        y = (enemy.y - enemySizePx / 2f).roundToInt()
                    )
                }
            ) {
                Text(
                    text = if (enemy.isBoss) "곧휴영욱" else "좆두순",
                    color = if (enemy.isBoss) Color(0xFFFFCA28) else Color(0xFFE7ECF7),
                    fontSize = 11.sp
                )
                Image(
                    painter = painterResource(
                        if (enemy.isBoss) Res.drawable.enemy_midboss else Res.drawable.enemy_common
                    ),
                    contentDescription = if (enemy.isBoss) "중간 보스" else "일반 적",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(enemySize)
                )
                Box(
                    modifier = Modifier
                        .width(enemySize)
                        .height(5.dp)
                        .background(Color(0xFF50151C), RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(enemy.hp.toFloat() / enemy.maxHp.toFloat())
                            .fillMaxHeight()
                            .background(Color(0xFFFF455D), RoundedCornerShape(3.dp))
                    )
                }
            }
        }

        state.projectiles.forEach { projectile ->
            val projectileSize = when (projectile.weaponType) {
                com.example.dependwife.game.domain.model.WeaponType.KNIFE -> 30.dp
                com.example.dependwife.game.domain.model.WeaponType.CUTTING_BOARD -> 36.dp
                com.example.dependwife.game.domain.model.WeaponType.WASHING_MACHINE -> 44.dp
                com.example.dependwife.game.domain.model.WeaponType.TV -> 48.dp
            }
            val projectileSizePx = with(density) { projectileSize.toPx() }
            val weaponLevel = state.weapons.firstOrNull { it.type == projectile.weaponType }?.level ?: 1
            val rotationDegrees = if (
                projectile.weaponType == com.example.dependwife.game.domain.model.WeaponType.KNIFE
            ) {
                atan2(projectile.velocityY, projectile.velocityX) * 180f / PI.toFloat() + 45f
            } else {
                0f
            }
            Image(
                painter = painterResource(weaponImage(projectile.weaponType, weaponLevel)),
                contentDescription = "${projectile.weaponType} 투사체",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (projectile.x - projectileSizePx / 2f).roundToInt(),
                            y = (projectile.y - projectileSizePx / 2f).roundToInt()
                        )
                    }
                    .rotate(rotationDegrees)
                    .size(projectileSize)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(7.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .background(Color(0xCC0A1020), RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            state.weapons.forEach { weapon ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(weaponImage(weapon.type, weapon.level)),
                        contentDescription = weapon.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "${weapon.name} Lv.${weapon.level}",
                            color = Color.White,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "공격 ${weapon.damage} · 관통 ${weapon.penetration}",
                            color = Color(0xFFB7C8E8),
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        Text(
            text = "방어선",
            color = Color(0xFFFF7184),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 166.dp)
        )
    }
}
