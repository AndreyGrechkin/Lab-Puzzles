package com.defey.labpuzzles.water_sort.calculations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.water_sort.Position
import com.defey.labpuzzles.water_sort.PourDirection
import com.defey.labpuzzles.water_sort.VialTransform
import kotlin.math.abs
import kotlin.math.sin

// РАСЧЕТ ПОЗИЦИИ ГОРЛЫШКА ИСТОЧНИКА С УЧЕТОМ НАКЛОНА
fun calculateNeckPosition(
    sourcePos: Position,
    direction: PourDirection,
): Offset {
    // ЦЕНТР ГОРЛЫШКА ИСТОЧНИКА
    val centerX = sourcePos.x + sourcePos.width / 2
    val topY = sourcePos.y

    // СМЕЩЕНИЕ ИЗ-ЗА НАКЛОНА (45 градусов)
    val diffX = sin(3.14 / 4).toFloat() * (sourcePos.width / 2)
    val diffY = sourcePos.width / 5 + diffX
    return when (direction) {
        PourDirection.RIGHT -> Offset(centerX + diffX, topY + diffY) // Смещаем вправо
        PourDirection.LEFT -> Offset(centerX - diffX, topY + diffY)  // Смещаем влево
    }
}

// РАСЧЕТ ПОЗИЦИИ ГОРЛЫШКА ЦЕЛИ
fun calculateTargetNeckPosition(targetPos: Position, density: Density): Offset {
    // ЦЕНТР ГОРЛЫШКА ЦЕЛИ
    return Offset(
        x = targetPos.x + targetPos.width / 2,
        y = targetPos.y + with(density) { 4.dp.toPx() } + targetPos.width / 2.5f// Чуть ниже верха
    )
}


// ПОЗИЦИЯ ДЛЯ АНИМАЦИИ ВЫБОРА (поднятие)
fun calculateSelectPosition(
    sourcePos: Position,
    progress: Float
): Position {
    // Поднимаем на 25% от ВЫСОТЫ ПРОБИРКИ (в пикселях)
    val liftHeight = sourcePos.height * 0.25f
    val liftedY = sourcePos.y - (liftHeight * progress)

    return sourcePos.copy(y = liftedY)
}

// ПОЗИЦИЯ ДЛЯ СОСТОЯНИЯ ПОДНЯТОЙ ПРОБИРКИ
fun calculateLiftedPosition(sourcePos: Position): Position {
    // Поднята на 25% от высоты пробирки
    val liftHeight = sourcePos.height * 0.25f
    val liftedY = sourcePos.y - liftHeight

    return sourcePos.copy(y = liftedY)
}

// ТРАНСФОРМАЦИЯ ДЛЯ АНИМАЦИИ ВЫБОРА
fun calculateSelectTransform(progress: Float, density: Density): VialTransform {
    return VialTransform(
        rotation = 0f,                              // Без наклона
        scale = 1f + (0.1f * progress),             // Легкое увеличение на 10%
        elevation = with(density) { 4.dp.toPx() } * progress // Легкая тень
    )
}

// ПОЗИЦИЯ ДЛЯ АНИМАЦИИ ОТМЕНЫ ВЫБОРА (опускание)
fun calculateDeselectPosition(
    sourcePos: Position,
    progress: Float
): Position {
    // Опускаем обратно на исходную позицию
    val liftHeight = sourcePos.height * 0.25f
    val liftedY = sourcePos.y - (liftHeight * (1f - progress))

    return sourcePos.copy(y = liftedY)
}

// ТРАНСФОРМАЦИЯ ДЛЯ АНИМАЦИИ ОТМЕНЫ ВЫБОРА
fun calculateDeselectTransform(progress: Float, density: Density): VialTransform {
    return VialTransform(
        rotation = 0f,
        scale = 1.1f - (0.1f * progress),           // Уменьшаем масштаб до 1.0
        elevation = with(density) { 4.dp.toPx() } * (1f - progress) // Убираем тень
    )
}

// ПОЗИЦИЯ ДЛЯ АНИМАЦИИ ПОКАЧИВАНИЯ (невалидный ход)
fun calculateShakePosition(
    sourcePos: Position,
    progress: Float
): Position {
    // Используем поднятую позицию (25% от высоты)
    val liftedPos = calculateLiftedPosition(sourcePos)

    // Горизонтальное покачивание: 12.5% от ширины пробирки
    val shakeDistance = sourcePos.width * 0.125f
    val shakeOffset = sin(progress * 3 * 3.14).toFloat() * shakeDistance

    return liftedPos.copy(x = liftedPos.x + shakeOffset)
}

// ТРАНСФОРМАЦИЯ ДЛЯ АНИМАЦИИ ПОКАЧИВАНИЯ
fun calculateShakeTransform(density: Density): VialTransform {
    return VialTransform(
        rotation = 0f,
        scale = 1.1f,                               // Остается увеличенной
        elevation = with(density) { 4.dp.toPx() }   // Остается с тенью
    )
}

fun determinePourDirection(
    sourcePos: Position,
    targetPos: Position,
    containerWidth: Int
): PourDirection {
    val horizontalThreshold = 20f // Пикселей - порог "друг над другом"

    // Если X координаты отличаются значительно - определяем по горизонтали
    if (abs(targetPos.x - sourcePos.x) > horizontalThreshold) {
        return if (targetPos.x > sourcePos.x) PourDirection.RIGHT else PourDirection.LEFT
    }

    // Если пробирки друг над другом - определяем по положению относительно центра
    return if (sourcePos.x < containerWidth / 2) {
        PourDirection.LEFT  // Ближе к левому краю - наклон влево
    } else {
        PourDirection.RIGHT // Ближе к правому краю - наклон вправо
    }
}

// ПОЗИЦИЯ ДЛЯ ДВИЖЕНИЯ К ЦЕЛИ (из поднятого состояния)
fun calculateMovePosition(
    sourcePos: Position, // УЖЕ ПОДНЯТАЯ позиция
    targetPos: Position,
    progress: Float,
    direction: PourDirection,
): Position {
    // ДВИГАЕМСЯ ОТ ПОДНЯТОЙ ПОЗИЦИИ К ЦЕЛИ
    val diffX = sin(3.14 / 4).toFloat() * targetPos.width / 2
    val currentY = sourcePos.y - (sourcePos.height * 0.25f) + (targetPos.y - sourcePos.y) * progress
    // КОРРЕКТИРОВКА ДЛЯ ГОРЛЫШКА (твой расчет)
    val adjustedX = when (direction) {
        PourDirection.RIGHT -> {
            sourcePos.x + (targetPos.width / 2 - diffX) + (targetPos.x - targetPos.width / 2 - sourcePos.x) * progress
            // ПРАВОЕ горлышко над ЦЕНТРОМ цели
        }

        PourDirection.LEFT -> {
            // ЛЕВОЕ горлышко над ЦЕНТРОМ цели
            sourcePos.x - (targetPos.width / 2 - diffX) + (targetPos.x + targetPos.width / 2 - sourcePos.x) * progress
        }
    }
    return Position(adjustedX, currentY, sourcePos.width, sourcePos.height)
}

// ТРАНСФОРМАЦИЯ ДЛЯ ДВИЖЕНИЯ К ЦЕЛИ
fun calculateMoveTransform(progress: Float, direction: PourDirection): VialTransform {
    // ПЛАВНЫЙ НАКЛОН ВО ВРЕМЯ ДВИЖЕНИЯ
    val rotation = when (direction) {
        PourDirection.RIGHT -> 45f * progress  // От 0° до 45° вправо
        PourDirection.LEFT -> -45f * progress  // От 0° до -45° влево
    }

    // ОПРЕДЕЛЯЕМ ТОЧКУ ВРАЩЕНИЯ
    val transformOrigin = when (direction) {
        PourDirection.RIGHT -> TransformOrigin(0.5f, 0f)  // От 0° до 45° вправо
        PourDirection.LEFT -> TransformOrigin(0.5f, 0f)  // От 0° до -45° влево
    }

    return VialTransform(
        rotation = rotation,
        scale = 1.1f - (0.1f * progress),
        elevation = 0f,
        transformOrigin = transformOrigin
    )
}

// ПОЗИЦИЯ ДЛЯ ФАЗЫ ПЕРЕЛИВАНИЯ
fun calculatePourPosition(
    sourcePos: Position,
    targetPos: Position,
    direction: PourDirection
): Position {
    // ОСТАЕМСЯ НАД ЦЕЛЕВОЙ ПРОБИРКОЙ (как в конце движения)
    return calculateMovePosition(sourcePos, targetPos, 1f, direction)
}

// ТРАНСФОРМАЦИЯ ДЛЯ ФАЗЫ ПЕРЕЛИВАНИЯ
fun calculatePourTransform(direction: PourDirection): VialTransform {
    return VialTransform(
        rotation = when (direction) {
            PourDirection.RIGHT -> 45f  // Максимальный наклон вправо
            PourDirection.LEFT -> -45f  // Максимальный наклон влево
        },
        scale = 1f,
        elevation = 0f,
        transformOrigin = TransformOrigin(0.5f, 0f)  // Вращение вокруг центра горлышка
    )
}

// ПОЗИЦИЯ ДЛЯ ВОЗВРАТА НА МЕСТО
fun calculateReturnPosition(
    sourcePos: Position, // Исходная позиция (куда возвращаемся)
    targetPos: Position, // Текущая позиция (над целью)
    progress: Float,
    direction: PourDirection
): Position {
    // ДВИЖЕМСЯ ОБРАТНО К ИСХОДНОЙ ПОЗИЦИИ
    val currentY =
        targetPos.y - (sourcePos.height * 0.25f) + (sourcePos.y + (sourcePos.height * 0.25f) - targetPos.y) * progress

    // КОРРЕКТИРОВКА С УЧЕТОМ СМЕЩЕНИЯ ПРИ НАКЛОНЕ
    val diffX = sin(3.14 / 4).toFloat() * targetPos.width / 2
    val adjustedX = when (direction) {
        PourDirection.RIGHT -> {
            targetPos.x - (targetPos.width / 2 - diffX) + (sourcePos.x - (targetPos.x - targetPos.width / 2)) * progress
        }

        PourDirection.LEFT -> {
            targetPos.x + (targetPos.width / 2 - diffX) + (sourcePos.x - (targetPos.x + targetPos.width / 2)) * progress
        }
    }

    return Position(adjustedX, currentY, sourcePos.width, sourcePos.height)
}

// ТРАНСФОРМАЦИЯ ДЛЯ ВОЗВРАТА НА МЕСТО
fun calculateReturnTransform(progress: Float, direction: PourDirection): VialTransform {
    // ПЛАВНО ВОЗВРАЩАЕМСЯ К ВЕРТИКАЛЬНОМУ ПОЛОЖЕНИЮ
    val startRotation = when (direction) {
        PourDirection.RIGHT -> 45f  // Начинаем наклоненными вправо
        PourDirection.LEFT -> -45f  // Начинаем наклоненными влево
    }
    val rotation = startRotation * (1f - progress)  // От 45° до 0°

    return VialTransform(
        rotation = rotation,
        scale = 1f,  // Возвращаем к нормальному размеру
        elevation = 0f,
        transformOrigin = TransformOrigin(0.5f, 0f)
    )
}
