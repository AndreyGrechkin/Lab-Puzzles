package com.defey.labpuzzles.repository

import com.defey.labpuzzles.models.GameResult
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.models.WaterSortState

interface WaterSortEngine {

    fun makeMove(state: WaterSortState, fromIndex: Int, toIndex: Int): GameResult<WaterSortState>
    fun checkWinCondition(vials: List<Vial>): Boolean
}