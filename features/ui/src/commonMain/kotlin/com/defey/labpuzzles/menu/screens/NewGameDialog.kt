package com.defey.labpuzzles.menu.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.dialog.DialogController
import com.defey.labpuzzles.dialog.FuturisticDialogButton
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.new_game_dialog_cancel
import com.defey.labpuzzles.resources.new_game_dialog_confirm
import com.defey.labpuzzles.resources.new_game_dialog_message
import com.defey.labpuzzles.resources.new_game_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewGameDialog(
    dialogController: DialogController,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.new_game_dialog_title),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.new_game_dialog_message),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            FuturisticDialogButton(
                modifier = Modifier.weight(1f).padding(end = 16.dp),
                text = stringResource(Res.string.new_game_dialog_cancel),
                onClick = {
                    onDismiss()
                    dialogController.hideDialog()
                }
            )

            FuturisticDialogButton(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.new_game_dialog_confirm),
                onClick = {
                    onConfirm()
                    dialogController.hideDialog()
                }
            )
        }
    }
}