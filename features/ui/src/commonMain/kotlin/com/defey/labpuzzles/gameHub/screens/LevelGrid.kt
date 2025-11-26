package com.defey.labpuzzles.gameHub.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defey.labpuzzles.gameHub.GameHubUiContract
import com.defey.labpuzzles.models.GameType
import com.defey.labpuzzles.models.LevelBlock
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_arrow_back
import com.defey.labpuzzles.resources.bg_arrow_forrow
import com.defey.labpuzzles.resources.bg_level_card
import org.jetbrains.compose.resources.painterResource

@Composable
fun LevelGrid(
    levels: List<GameHubUiContract.LevelUI>,
    currentBlock: LevelBlock,
    totalBlocks: Int = 5,
    onLevelClick: (Int) -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                var totalDrag = 0f
                var isDragging: Boolean
                detectHorizontalDragGestures(
                    onDragStart = {
                        isDragging = true
                        totalDrag = 0f
                    },
                    onDragEnd = {
                        isDragging = false
                        when {
                            totalDrag > 50f && currentBlock.blockNumber > 0 -> onPrevClick()
                            totalDrag < -50f && currentBlock.blockNumber < totalBlocks -> onNextClick()
                        }
                        totalDrag = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    totalDrag += dragAmount
                }
            }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(levels) { level ->
                LevelCell(
                    level = level,
                    onClick = { onLevelClick(level.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentBlock.blockNumber > 1) {
                ArrowButton(
                    isPrevious = true,
                    onClick = onPrevClick
                )
            } else Spacer(modifier = Modifier.size(48.dp))

            if (currentBlock.blockNumber < totalBlocks) {
                ArrowButton(
                    isPrevious = false,
                    onClick = onNextClick
                )
            } else Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
private fun ArrowButton(
    isPrevious: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val arrowIcon = if (isPrevious) {
        painterResource(Res.drawable.bg_arrow_back)
    } else {
        painterResource(Res.drawable.bg_arrow_forrow)
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = arrowIcon,
            contentDescription = if (isPrevious) "Предыдущий блок" else "Следующий блок",
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun LevelCell(
    level: GameHubUiContract.LevelUI,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cellColor = when (level.gameType) {
        GameType.WATER_SORT -> listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
        GameType.FLOW_FREE -> listOf(Color(0xFF9C27B0), Color(0xFF6A1B9A))
        GameType.SUDOKU -> listOf(Color(0xFFFFC107), Color(0xFFFF8F00))
        GameType.SLIDING_PUZZLE -> listOf(Color(0xFFF44336), Color(0xFFC62828))
    }

    if (level.state.isLocked) {
        Box(
            modifier = modifier
                .aspectRatio(0.8f)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.bg_level_card),
                contentDescription = "Level ${level.id}",
                modifier = Modifier.matchParentSize().clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        Box(
            modifier = modifier
                .aspectRatio(0.8f)
                .background(
                    brush = Brush.verticalGradient(cellColor),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Номер уровня
                Text(
                    text = level.id.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                if (level.state.isCompleted && level.stars > 0) {
                    Text(
                        text = "⭐".repeat(level.stars),
                        color = Color.Yellow,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }

            // Золотая подсветка для пройденных уровней
            if (level.state.isCompleted) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(
                            width = 3.dp,
                            color = Color(0xFFFFD700),
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }
        }
    }
}