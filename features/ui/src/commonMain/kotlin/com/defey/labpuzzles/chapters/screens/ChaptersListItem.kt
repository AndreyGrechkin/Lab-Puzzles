package com.defey.labpuzzles.chapters.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defey.labpuzzles.chapters.ChaptersUiContract
import com.defey.labpuzzles.factory.ChapterFactory
import com.defey.labpuzzles.menu.screens.FuturisticButton
import com.defey.labpuzzles.models.Chapter
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_dialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChaptersListItem(
    chapterItem: ChaptersUiContract.ChapterListItem,
    onChapterSelect: (Chapter) -> Unit,
) {
    val isUnlocked = chapterItem.isUnlocked
    val isCompleted = chapterItem.completedLevels == 100

    Box{
        Image(
            painter = painterResource(Res.drawable.bg_dialog),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ГЛАВА ${chapterItem.chapter.ordinal + 1}: ${
                    stringResource(ChapterFactory().getTitle(chapterItem.chapter))
                }",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            if (isUnlocked) {
                Spacer(modifier = Modifier.height(8.dp))

                if (isCompleted) {
                    Text(
                        text = "ЗАВЕРШЕНО!",
                        color = Color.Yellow,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${chapterItem.completedLevels}/100 УРОВНЕЙ",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        text = "${chapterItem.completedLevels}/100 УРОВНЕЙ",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "ДО ЗАВЕРШЕНИЯ: ${100 - chapterItem.completedLevels}",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(ChapterFactory().getDescription(chapterItem.chapter)),
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(16.dp))
                FuturisticButton(
                    text = if (isCompleted) "ИГРАТЬ" else "ПРОДОЛЖИТЬ",
                    modifier = Modifier.fillMaxWidth(0.8f),
                    onClick = { onChapterSelect(chapterItem.chapter) }
                )
            } else {
                // Только замок для заблокированных глав
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "🔒",
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}