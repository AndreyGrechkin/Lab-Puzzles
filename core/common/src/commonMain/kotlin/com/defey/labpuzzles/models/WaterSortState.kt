package com.defey.labpuzzles.models

data class WaterSortState(
    val vials: List<Vial>,
    override val movesCount: Int = 0,
    override val isCompleted: Boolean = false
) : GameState()