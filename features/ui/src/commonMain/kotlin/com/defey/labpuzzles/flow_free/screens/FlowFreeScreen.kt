package com.defey.labpuzzles.flow_free.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.flow_free.FlowFreeUiContract
import com.defey.labpuzzles.flow_free.FlowFreeViewModel
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_flow_free
import com.defey.labpuzzles.watersort.screens.WaterSortControls
import com.defey.labpuzzles.watersort.screens.WaterSortTopBar
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FlowFreeScreen() {
    val viewModel: FlowFreeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    // Обработка ошибок (авто-сброс через 1 секунду)
    LaunchedEffect(state.showError) {
        if (state.showError) {
            delay(1000)
            viewModel.onEvent(FlowFreeUiContract.Event.OnErrorShown)
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),

            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.bg_flow_free),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)

            ) {
                WaterSortTopBar(
                    currencies = state.currencies,
                    levelId = state.levelId,
                    onBackClick = {
                        viewModel.onEvent(FlowFreeUiContract.Event.OnBackClick)
                    },
                    onAddCurrencyClick = {}
                )

                FlowFreeStats(
                    movesCount = state.movesCount,
                    progress = state.progress,
                    modifier = Modifier.padding(top = 16.dp)
                )

                WaterSortControls(
                    onHint = {},
                    onRestart = {
                        viewModel.onEvent(FlowFreeUiContract.Event.OnRetry)
                    },
                    onSkip = {},
                    onUndo = {},
                    modifier = Modifier.padding(top = 16.dp),
                )

                FlowFreeGameBoard(
                    state = state,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                )
            }
        }
    }
}
