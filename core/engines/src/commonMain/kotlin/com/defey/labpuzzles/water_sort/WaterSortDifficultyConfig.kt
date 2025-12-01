package com.defey.labpuzzles.water_sort

import com.defey.labpuzzles.models.Difficulty

interface WaterSortDifficultyConfig {

    fun getVariant(difficulty: Difficulty, variantId: String?): WaterSortVariant
    fun getAllVariants(difficulty: Difficulty): List<WaterSortVariant>
}