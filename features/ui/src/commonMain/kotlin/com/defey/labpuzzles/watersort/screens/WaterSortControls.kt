package com.defey.labpuzzles.watersort.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.icon_hint
import com.defey.labpuzzles.resources.icon_restart
import com.defey.labpuzzles.resources.icon_skip
import com.defey.labpuzzles.resources.icon_undo
import org.jetbrains.compose.resources.painterResource

@Composable
fun WaterSortControls(
    onUndo: () -> Unit,
    onHint: () -> Unit,
    onRestart: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(Res.drawable.icon_hint),
            contentDescription = "hint",
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onHint)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = painterResource(Res.drawable.icon_undo),
            contentDescription = "undo",
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onUndo)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(Res.drawable.icon_restart),
            contentDescription = "restart",
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onRestart)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Image(
            painter = painterResource(Res.drawable.icon_skip),
            contentDescription = "skip",
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onSkip)
        )
    }
}