package com.example.dependwife.game

import androidx.compose.ui.window.ComposeUIViewController
import com.example.dependwife.game.presentation.DependWifeGame
import com.example.dependwife.game.storage.IosGameResultStorage

fun MainViewController(): platform.UIKit.UIViewController {
    val gameResultStorage = IosGameResultStorage()
    return ComposeUIViewController {
        DependWifeGame(gameResultStorage)
    }
}
