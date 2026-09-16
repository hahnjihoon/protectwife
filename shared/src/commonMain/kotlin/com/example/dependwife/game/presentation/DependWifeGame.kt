package com.example.dependwife.game.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dependwife.game.domain.GameRules
import com.example.dependwife.game.domain.model.ArenaMetrics
import com.example.dependwife.game.logic.GameEngine
import com.example.dependwife.game.storage.GameResultStorage

@Composable
fun DependWifeGame(gameResultStorage: GameResultStorage) {
    val engine = remember { GameEngine() }
    var state by remember { mutableStateOf(engine.initialState()) }
    var resultSummary by remember { mutableStateOf(gameResultStorage.loadResultSummary()) }
    var metrics by remember {
        mutableStateOf(
            ArenaMetrics(
                width = 0f,
                height = 0f,
                defenseLineY = 0f,
                projectileStartX = 0f,
                projectileStartY = 0f,
                hitRadius = 0f
            )
        )
    }

    LaunchedEffect(Unit) {
        var previousFrameNanos = 0L
        while (true) {
            withFrameNanos { frameNanos ->
                if (previousFrameNanos != 0L) {
                    val deltaSeconds = ((frameNanos - previousFrameNanos) / 1_000_000_000f)
                        .coerceAtMost(0.05f)
                    state = engine.update(state, deltaSeconds, metrics)
                }
                previousFrameNanos = frameNanos
            }
        }
    }

    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) {
            resultSummary = gameResultStorage.saveGameResult(
                score = state.score,
                defeatedEnemies = state.defeatedEnemies
            )
        }
    }

    MaterialTheme {
        Surface(
            color = Color(0xFF0D1526),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF111D32))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "DEPEND WIFE",
                                color = Color.White,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "점수 ${state.score} · 처치 ${state.defeatedEnemies} · 최고 ${resultSummary.bestScore}",
                                color = Color(0xFFD6E2FA),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        Text(
                            text = "강화 게이지 ${state.upgradeGauge} / ${state.upgradeGaugeTarget}",
                            color = Color(0xFFFFD166),
                            style = MaterialTheme.typography.labelSmall
                        )
                        LinearProgressIndicator(
                            progress = { state.upgradeGauge.toFloat() / state.upgradeGaugeTarget },
                            color = Color(0xFFFFB300),
                            trackColor = Color(0xFF263750),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    GameArena(
                        state = state,
                        onMetricsChanged = { metrics = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

                if (state.isUpgradeSelectionOpen) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xAA000000))
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF17243A)),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(18.dp)
                            ) {
                                Text(
                                    text = "강화 선택",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                state.upgradeChoices.forEach { choice ->
                                    Button(
                                        onClick = { state = engine.applyUpgrade(state, choice) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(choice.title)
                                            Text(
                                                text = choice.description,
                                                style = MaterialTheme.typography.labelSmall,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (state.isGameOver) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xCC000000))
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF311A24)),
                            modifier = Modifier.padding(28.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.padding(22.dp)
                            ) {
                                Text(
                                    text = "방어 실패",
                                    color = Color(0xFFFF7488),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "점수 ${state.score} · 처치 ${state.defeatedEnemies}",
                                    color = Color.White
                                )
                                Button(onClick = { state = engine.initialState() }) {
                                    Text("다시 시작")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
