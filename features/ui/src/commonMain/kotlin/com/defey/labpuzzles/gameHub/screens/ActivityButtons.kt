package com.defey.labpuzzles.gameHub.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.icon_achievements
import com.defey.labpuzzles.resources.icon_daily
import com.defey.labpuzzles.resources.icon_settings
import org.jetbrains.compose.resources.painterResource

@Composable
fun ActivityButtons(
    onDailyQuestsClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.icon_daily),
            contentDescription = "daily",
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onDailyQuestsClick)
        )

        Image(
            painter = painterResource(Res.drawable.icon_achievements),
            contentDescription = "achievements",
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onAchievementsClick)
        )

        Image(
            painter = painterResource(Res.drawable.icon_settings),
            contentDescription = "settings",
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onSettingsClick)
        )
    }
}