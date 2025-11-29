package com.defey.labpuzzles.watersort.screens

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
fun WaterSortStats(
    movesCount: Int,
    elapsedTime: Long,
    modifier: Modifier = Modifier
) {
    val minutes = elapsedTime / 60
    val seconds = elapsedTime % 60
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
            text = "Время: ${minutes.toString().padStart(2, '0')}:${
                seconds.toString().padStart(2, '0')
            }",
            onClick = { },
            modifier = Modifier.weight(1f)
        )
    }
}