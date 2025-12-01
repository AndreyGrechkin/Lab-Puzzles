package com.defey.labpuzzles.watersort.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_water_sort
import com.defey.labpuzzles.water_sort.PourDirection
import com.defey.labpuzzles.water_sort.WaterSortGameField
import com.defey.labpuzzles.watersort.WaterSortUiContract
import com.defey.labpuzzles.watersort.WaterSortViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WaterSortScreen() {
    val viewModel: WaterSortViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val animationState by viewModel.animationState.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),

            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.bg_water_sort),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Spacer(
                modifier = Modifier.matchParentSize()
                    .background(Color.Black.copy(alpha = 0.7f))
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
                        viewModel.onEvent(WaterSortUiContract.Event.OnBackClick)
                    },
                    onAddCurrencyClick = {}
                )
                WaterSortStats(
                    movesCount = state.movesCount,
                    elapsedTime = state.elapsedTime,
                    modifier = Modifier.padding(top = 16.dp)
                )

                WaterSortControls(
                    onHint = {},
                    onRestart = {
                        viewModel.onEvent(WaterSortUiContract.Event.OnRetry)
                    },
                    onSkip = {},
                    onUndo = {},
                    modifier = Modifier.padding(top = 16.dp),
                )

                WaterSortGameField(
                    vials = state.vials,
                    selectedVialIndex = state.selectedVialIndex,
                    totalWidth = 40.dp,
                    modifier = Modifier.padding(top = 48.dp),
                    animationState = animationState,
                    onVialClick = { index: Int, direction: PourDirection? ->
                        viewModel.onEvent(WaterSortUiContract.Event.OnVialClick(index, direction))
                    }
                )
            }
        }
    }
}