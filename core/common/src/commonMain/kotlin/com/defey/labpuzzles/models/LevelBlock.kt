package com.defey.labpuzzles.models

enum class LevelBlock(val blockNumber: Int) {
    BLOCK_1(1),
    BLOCK_2(2),
    BLOCK_3(3),
    BLOCK_4(4),
    BLOCK_5(5);

    companion object {
        fun fromNumber(number: Int): LevelBlock? {
            return entries.find { it.blockNumber == number }
        }
    }
}