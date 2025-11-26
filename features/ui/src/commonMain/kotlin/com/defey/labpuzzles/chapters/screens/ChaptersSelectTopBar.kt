package com.defey.labpuzzles.chapters.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChaptersSelectTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0x801A237E)),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(Res.drawable.bg_arrow_back),
            contentDescription = "Назад",
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterStart)
                .clickable(onClick = onBackClick)
                .padding(start = 16.dp)
        )

        Text(
            text = "ВЫБОР ГЛАВЫ",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}