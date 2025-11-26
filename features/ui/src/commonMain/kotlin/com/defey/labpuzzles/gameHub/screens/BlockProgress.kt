package com.defey.labpuzzles.gameHub.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.factory.BlockDescriptionFactory
import com.defey.labpuzzles.models.Chapter
import com.defey.labpuzzles.models.LevelBlock
import org.jetbrains.compose.resources.stringResource

@Composable
fun BlockProgress(
    chapter: Chapter,
    currentBlock: LevelBlock,
    levelsCompleted: Int,
    totalLevels: Int = 20,
    modifier: Modifier = Modifier
) {
    val progress = if (totalLevels > 0) {
        levelsCompleted.toFloat() / totalLevels.toFloat()
    } else 0f

    val blockDescription = remember(chapter, currentBlock) {
        BlockDescriptionFactory().getBlockDescription(chapter, currentBlock)
    }

    Column(
        modifier = modifier.fillMaxWidth()
            .background(
                color = Color(0x2196F3).copy(alpha = 0.9f), // Полупрозрачный синий
                shape = RoundedCornerShape(12.dp)
            ).padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Заголовок с номером блока
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ЭТАП ${currentBlock.blockNumber} • $levelsCompleted/$totalLevels УРОВНЕЙ",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Процент выполнения
            Text(
                text = "${(progress * 100).toInt()}%",
                color = Color.Cyan,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Прогресс-бар
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(12.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFF3A621), Color(0xF3FACE49))
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
            )

            // Полоски прогресса для визуального эффекта
            if (progress > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(12.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }

        // Сюжетное описание блока
        Text(
            text = stringResource(blockDescription),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}