package com.example.dependwife.game.storage

data class GameResultSummary(
    val bestScore: Int,
    val lastScore: Int,
    val totalGames: Int,
    val lastDefeatedEnemies: Int
)

interface GameResultStorage {
    fun loadResultSummary(): GameResultSummary

    fun saveGameResult(score: Int, defeatedEnemies: Int): GameResultSummary
}
