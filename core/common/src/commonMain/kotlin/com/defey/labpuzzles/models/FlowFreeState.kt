package com.defey.labpuzzles.models

data class FlowFreeState(
    val grid: List<List<FlowFreeCell>>,
    override val movesCount: Int = 0,
    override val isCompleted: Boolean = false,
    val activeLine: ActiveLine? = null,
    val progress: Int = 0
) : GameState()
