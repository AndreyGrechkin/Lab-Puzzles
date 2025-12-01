package com.defey.labpuzzles.water_sort

import com.defey.labpuzzles.models.Difficulty

class WaterSortDifficultyConfigImpl : WaterSortDifficultyConfig {
    override fun getVariant(difficulty: Difficulty, variantId: String?): WaterSortVariant {
        // Получаем варианты ТОЛЬКО для указанной сложности
        val variants = when (difficulty) {
            Difficulty.BEGINNER -> createBeginnerVariants()
            Difficulty.EASY -> createEasyVariants()
            Difficulty.MEDIUM -> createMediumVariants()
            Difficulty.HARD -> createHardVariants()
            Difficulty.EXPERT -> createExpertVariants()
        }

        // Логика выбора варианта
        return when {
            // Если передан variantId - ищем соответствующий вариант
            variantId != null -> {
                variants.firstOrNull { it.variantId == variantId }
                    ?: variants.random() // Если не нашли - возвращаем случайный
            }
            // Если variantId не передан - сразу возвращаем случайный
            else -> variants.random()
        }
    }

    /**
     * ПОЛУЧЕНИЕ ВСЕХ ВАРИАНТОВ ДЛЯ УКАЗАННОЙ СЛОЖНОСТИ
     *
     * @param difficulty Уровень сложности
     * @return Список всех вариантов для этой сложности
     */
    override fun getAllVariants(difficulty: Difficulty): List<WaterSortVariant> {
        return when (difficulty) {
            Difficulty.BEGINNER -> createBeginnerVariants()
            Difficulty.EASY -> createEasyVariants()
            Difficulty.MEDIUM -> createMediumVariants()
            Difficulty.HARD -> createHardVariants()
            Difficulty.EXPERT -> createExpertVariants()
        }
    }

    // Вариации для BEGINNER (Новичок)
    private fun createBeginnerVariants(): List<WaterSortVariant> {
        return listOf(
            WaterSortVariant(3, 2, 2, "B1"),
            WaterSortVariant(3, 3, 2, "B2")
        )
    }

    // Вариации для EASY (Легко)
    private fun createEasyVariants(): List<WaterSortVariant> {
        return listOf(
            WaterSortVariant(4, 3, 2, "E1"),
            WaterSortVariant(3, 4, 2, "E2"),
            WaterSortVariant(4, 4, 2, "E3")
        )
    }

    // Вариации для MEDIUM (Средне)
    private fun createMediumVariants(): List<WaterSortVariant> {
        return listOf(
            WaterSortVariant(5, 4, 2, "M1"),
            WaterSortVariant(4, 5, 2, "M2"),
            WaterSortVariant(5, 5, 2, "M3"),
            WaterSortVariant(6, 4, 2, "M4"),
        )
    }

    // Вариации для HARD (Сложно)
    private fun createHardVariants(): List<WaterSortVariant> {
        return listOf(
            WaterSortVariant(6, 5, 2, "H1"),
            WaterSortVariant(7, 5, 2, "H2"),
            WaterSortVariant(6, 6, 2, "H3"),
            WaterSortVariant(7, 6, 2, "H4")
        )
    }

    // Вариации для EXPERT (Эксперт)
    private fun createExpertVariants(): List<WaterSortVariant> {
        return listOf(
            WaterSortVariant(7, 7, 2, "X1"),
            WaterSortVariant(8, 7, 2, "X2"),
            WaterSortVariant(7, 8, 2, "X3"),
            WaterSortVariant(8, 8, 2, "X4")
        )
    }
}