package com.defey.labpuzzles.water_sort

import com.defey.labpuzzles.models.Difficulty
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.models.WaterSortColors
import com.defey.labpuzzles.repository.WaterSortLevelGenerator

class WaterSortLevelGeneratorImpl(
    private val difficultyConfig: WaterSortDifficultyConfig
) : WaterSortLevelGenerator {
    override fun generateLevel(
        levelId: String,
        difficulty: Difficulty,
        variantId: String?
    ): List<Vial> {
        // 1. Получаем вариант параметров для генерации
        val variant = difficultyConfig.getVariant(difficulty, variantId)

        // 2. Логируем информацию о генерации
        println("🎯 Генерация уровня: $levelId")
        println("   Сложность: $difficulty")
        println("   Вариант: ${variant.variantId}")
        println("   Параметры: ${variant.colorCount} цветов, " +
                "емкость ${variant.capacity}, " +
                "пустых пробирок: ${variant.emptyVials}")

        // 3. Генерируем начальное состояние
        return generateRandomState(variant)
    }

    private fun generateRandomState(variant: WaterSortVariant): List<Vial> {
        // 1. Выбираем цвета из палитры (первые variant.colorCount цветов)
        val colors = WaterSortColors.fullColorPalette.take(variant.colorCount)

        // 2. Создаем все единицы жидкости (colorCount × capacity)
        val allColorUnits = colors.flatMap { color ->
            List(variant.capacity) { color }
        }.shuffled()

        // 3. Создаем список для хранения пробирок
        val vials = mutableListOf<Vial>()

        // 4. Распределяем цветные единицы по пробиркам равномерно
        for (i in 0 until variant.colorCount) {
            val startIndex = i * variant.capacity
            val vialColors = allColorUnits.subList(startIndex, startIndex + variant.capacity)
            vials.add(Vial(colors = vialColors, capacity = variant.capacity))
        }

        // 5. Добавляем пустые пробирки
        repeat(variant.emptyVials) {
            vials.add(Vial(colors = emptyList(), capacity = variant.capacity))
        }

        // 6. Логируем результат
        println("   🧪 Создано ${vials.size} пробирок")


        // 7. Возвращаем перемешанный результат
        return vials.shuffled()
    }
}
