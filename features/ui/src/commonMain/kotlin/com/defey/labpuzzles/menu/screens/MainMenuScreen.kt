package com.defey.labpuzzles.menu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.dialog.CustomDialog
import com.defey.labpuzzles.dialog.rememberDialogController
import com.defey.labpuzzles.extensions.asString
import com.defey.labpuzzles.extensions.toAnyText
import com.defey.labpuzzles.menu.MainMenuUiContract
import com.defey.labpuzzles.menu.MainMenuViewModel
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_main_menu
import com.defey.labpuzzles.resources.main_achievements
import com.defey.labpuzzles.resources.main_continue
import com.defey.labpuzzles.resources.main_new_game
import com.defey.labpuzzles.resources.main_settings
import com.defey.labpuzzles.resources.version
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainMenuScreen() {

    val viewModel: MainMenuViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val dialogController = rememberDialogController()

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                MainMenuUiContract.Action.ShowNewGameDialog -> {
                    dialogController.showDialog {
                        NewGameDialog(
                            dialogController = dialogController,
                            onConfirm = {
                                viewModel.onEvent(MainMenuUiContract.Event.OnNewGame)
                            }
                        )
                    }
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(Res.drawable.bg_main_menu),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = Res.string.version.toAnyText(state.version).asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(paddingValues)
                    .padding(bottom = 24.dp, end = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.hasSavedGame) {
                    FuturisticButton(
                        text = stringResource(Res.string.main_continue),
                        onClick = {
                            viewModel.onEvent(MainMenuUiContract.Event.OnContinueGame)
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }

                FuturisticButton(
                    text = stringResource(Res.string.main_new_game),
                    onClick = {
                        viewModel.onEvent(MainMenuUiContract.Event.OnNewGameClicked)
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                FuturisticButton(
                    text = stringResource(Res.string.main_achievements),
                    onClick = {
                        viewModel.onEvent(MainMenuUiContract.Event.OnAchievements)
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                FuturisticButton(
                    text = stringResource(Res.string.main_settings),
                    onClick = {
                        viewModel.onEvent(MainMenuUiContract.Event.OnSettings)
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
        CustomDialog(
            controller = dialogController,
            modifier = Modifier.padding(48.dp)
        )
    }
}