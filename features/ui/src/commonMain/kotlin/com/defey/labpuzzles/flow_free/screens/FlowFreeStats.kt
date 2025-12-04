package com.defey.labpuzzles.flow_free.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.menu.screens.FuturisticButton

@Composable
fun FlowFreeStats(
    movesCount: Int,
    progress: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FuturisticButton(
            text = "Ходы: $movesCount",
            onClick = { },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))

        FuturisticButton(
            text = "Прогресс: ${progress}%",
            onClick = { },
            modifier = Modifier.weight(1f)
        )
    }
}