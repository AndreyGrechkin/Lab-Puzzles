package com.defey.labpuzzles.gameHub.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.factory.ChapterFactory
import com.defey.labpuzzles.gameHub.GameHubUiContract
import com.defey.labpuzzles.gameHub.GameHubViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameHubScreen() {

    val viewModel: GameHubViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(ChapterFactory().getBackground(state.currentChapter)),
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
                CurrencyPanel(
                    currencies = state.currencies,
                    onAddCurrencyClick = {
                        viewModel.onEvent(
                            GameHubUiContract.Event.OnAddCurrencyClick(
                                it
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        ChapterCard(
                            chapter = state.currentChapter,
                            currentBlock = state.currentBlock,
                            onClick = { viewModel.onEvent(GameHubUiContract.Event.OnChapterClick) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BlockProgress(
                            chapter = state.currentChapter,
                            currentBlock = state.currentBlock,
                            levelsCompleted = state.levelsCompleted,
                            totalLevels = state.totalLevels,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ActivityButtons(
                        onDailyQuestsClick = { viewModel.onEvent(GameHubUiContract.Event.OnDailyQuestsClick) },
                        onAchievementsClick = { viewModel.onEvent(GameHubUiContract.Event.OnAchievementsClick) },
                        onSettingsClick = { viewModel.onEvent(GameHubUiContract.Event.OnSettingsClick) },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LevelGrid(
                    levels = state.levels,
                    currentBlock = state.currentBlock,
                    onLevelClick = { levelId ->
                        viewModel.onEvent(GameHubUiContract.Event.OnLevelClick(levelId))
                    },
                    onPrevClick = { viewModel.onEvent(GameHubUiContract.Event.OnPrevBlockClick) },
                    onNextClick = { viewModel.onEvent(GameHubUiContract.Event.OnNextBlockClick) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}