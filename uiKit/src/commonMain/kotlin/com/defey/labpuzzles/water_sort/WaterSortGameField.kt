package com.defey.labpuzzles.water_sort

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.water_sort.calculations.determinePourDirection

@Composable
fun WaterSortGameField(
    vials: List<Vial>,
    selectedVialIndex: Int?,
    animationState: PourAnimationState,
    totalWidth: Dp,
    onVialClick: (Int, PourDirection?) -> Unit,
    modifier: Modifier = Modifier
) {
    val vialPositions = remember { mutableStateMapOf<Int, Position>() }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var currentPourDirection by remember { mutableStateOf<PourDirection?>(null) }
    val density = LocalDensity.current

    val animatingIndex = when (animationState) {
        is PourAnimationState.SelectingVial -> animationState.selectedIndex
        is PourAnimationState.DeselectingVial -> animationState.selectedIndex
        is PourAnimationState.VialLifted -> animationState.selectedIndex
        is PourAnimationState.InvalidMoveShake -> animationState.selectedIndex
        is PourAnimationState.MovingToTarget -> animationState.sourceIndex
        is PourAnimationState.PouringStream -> animationState.sourceIndex
        is PourAnimationState.ReturningBack -> animationState.sourceIndex
        PourAnimationState.Idle -> null
    }

    val clicksEnabled = animationState is PourAnimationState.Idle ||
            animationState is PourAnimationState.VialLifted

    Box(
        modifier = modifier
            .onSizeChanged { containerSize = it }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(60.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(vials) { index, vial ->
                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            vialPositions[index] = Position(
                                x = coordinates.positionInParent().x,
                                y = coordinates.positionInParent().y,
                                width = with(density) { totalWidth.toPx() },
                                height = coordinates.size.height.toFloat()
                            )
                        }
                ) {
                    if (index != animatingIndex) {
                        WaterSortVial(
                            colors = vial.colors,
                            capacity = vial.capacity,
                            totalWidth = totalWidth,
                            isSelected = selectedVialIndex == index && clicksEnabled,
                            modifier = Modifier.clickable(
                                enabled = clicksEnabled,
                                onClick = {
                                    if (selectedVialIndex != null && selectedVialIndex != index) {
                                        val sourcePos = vialPositions[selectedVialIndex]
                                        val targetPos = vialPositions[index]
                                        val containerWidth = containerSize.width

                                        if (sourcePos != null && targetPos != null) {
                                            currentPourDirection = determinePourDirection(
                                                sourcePos,
                                                targetPos,
                                                containerWidth
                                            )

                                            onVialClick(index, currentPourDirection)
                                        }
                                    } else {
                                        onVialClick(index, null)
                                    }
                                }
                            )
                        )
                    } else {
                        Spacer(modifier = Modifier.size(totalWidth, totalWidth * 3))
                    }
                }
            }
        }

        if (animatingIndex != null && vialPositions.containsKey(animatingIndex)) {
            val sourceVial = vials[animatingIndex]
            val sourcePosition = vialPositions[animatingIndex] ?: return
            AnimatedVialOverlay(
                vial = sourceVial,
                totalWidth = totalWidth,
                animationState = animationState,
                vialPositions = vialPositions,
                sourcePosition = sourcePosition,
                density = density,
                onClick = {
                    if (clicksEnabled) {
                        onVialClick(animatingIndex, null)
                    }
                }
            )
        }
    }
}