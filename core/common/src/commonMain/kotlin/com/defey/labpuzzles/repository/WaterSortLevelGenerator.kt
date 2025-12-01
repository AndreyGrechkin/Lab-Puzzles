package com.defey.labpuzzles.repository

import com.defey.labpuzzles.models.Difficulty
import com.defey.labpuzzles.models.Vial

interface WaterSortLevelGenerator {
    fun generateLevel(
        levelId: String,
        difficulty: Difficulty,
        variantId: String? = null
    ): List<Vial>
}