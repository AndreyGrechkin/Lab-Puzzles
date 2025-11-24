package com.defey.labpuzzles.gameHub.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.defey.labpuzzles.gameHub.GameHubViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameHubScreen() {

    val viewModel: GameHubViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Text(state.name)
        }
    }
}