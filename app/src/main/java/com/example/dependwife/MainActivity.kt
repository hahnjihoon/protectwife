package com.example.dependwife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dependwife.game.presentation.DependWifeGame
import com.example.dependwife.game.storage.AndroidGameResultStorage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val gameResultStorage = AndroidGameResultStorage(this)
        setContent {
            DependWifeGame(gameResultStorage)
        }
    }
}
