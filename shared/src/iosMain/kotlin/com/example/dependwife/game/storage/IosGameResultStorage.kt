package com.example.dependwife.game.storage

import platform.Foundation.NSUserDefaults

class IosGameResultStorage : GameResultStorage {
    private val preferences = NSUserDefaults.standardUserDefaults

    override fun loadResultSummary(): GameResultSummary {
        return GameResultSummary(
            bestScore = preferences.integerForKey("best_score").toInt(),
            lastScore = preferences.integerForKey("last_score").toInt(),
            totalGames = preferences.integerForKey("total_games").toInt(),
            lastDefeatedEnemies = preferences.integerForKey("last_defeated_enemies").toInt()
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

        preferences.setInteger(updatedResult.bestScore.toLong(), "best_score")
        preferences.setInteger(updatedResult.lastScore.toLong(), "last_score")
        preferences.setInteger(updatedResult.totalGames.toLong(), "total_games")
        preferences.setInteger(updatedResult.lastDefeatedEnemies.toLong(), "last_defeated_enemies")

        return updatedResult
    }
}
