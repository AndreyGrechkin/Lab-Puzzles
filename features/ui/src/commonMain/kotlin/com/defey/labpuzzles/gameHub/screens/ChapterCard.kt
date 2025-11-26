package com.defey.labpuzzles.gameHub.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.extensions.asString
import com.defey.labpuzzles.extensions.toAnyText
import com.defey.labpuzzles.factory.ChapterFactory
import com.defey.labpuzzles.models.Chapter
import com.defey.labpuzzles.models.LevelBlock
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.game_hub_chapter_title
import com.defey.labpuzzles.resources.icon_chapter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChapterCard(
    chapter: Chapter,
    currentBlock: LevelBlock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showNextChapterConditions = currentBlock == LevelBlock.BLOCK_5
    val title = ChapterFactory().getTitle(chapter)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0x2196F3).copy(alpha = 0.9f), // Полупрозрачный синий
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Первая строка: Иконка + номер главы
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Иконка главы
                Image(
                    painter = painterResource(Res.drawable.icon_chapter),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )

                Text(
                    text = Res.string.game_hub_chapter_title.toAnyText((chapter.ordinal + 1).toString()).asString(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Вторая строка: Название главы
            Text(
                text = stringResource(title),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            // Третья строка: Условия след. главы (только для 5-го блока)
            if (showNextChapterConditions) {
                Text(
                    text = "Необходимо: 80⭐ + 10🔬",
                    color = Color.Yellow.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}