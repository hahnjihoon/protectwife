package com.example.dependwife.game.storage

import android.content.Context

class AndroidGameResultStorage(context: Context) : GameResultStorage {
    private val preferences = context.getSharedPreferences("game_results", Context.MODE_PRIVATE)

    override fun loadResultSummary(): GameResultSummary {
        return GameResultSummary(
            bestScore = preferences.getInt("best_score", 0),
            lastScore = preferences.getInt("last_score", 0),
            totalGames = preferences.getInt("total_games", 0),
            lastDefeatedEnemies = preferences.getInt("last_defeated_enemies", 0)
        )
    }

    override fun saveGameResult(score: Int, defeatedEnemies: Int): GameResultSummary {
        val savedResult = loadResultSummary()
        val updatedResult = GameResultSummary(
            bestScore = maxOf(savedResult.bestScore, score),
            lastScore = score,
            totalGames = savedResult.totalGames + 1,
            lastDefeatedEnemies = defeatedEnemies
        )

        preferences.edit()
            .putInt("best_score", updatedResult.bestScore)
            .putInt("last_score", updatedResult.lastScore)
            .putInt("total_games", updatedResult.totalGames)
            .putInt("last_defeated_enemies", updatedResult.lastDefeatedEnemies)
            .apply()

        return updatedResult
    }
}
