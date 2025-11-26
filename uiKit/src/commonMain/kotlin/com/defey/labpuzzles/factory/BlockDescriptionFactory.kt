package com.defey.labpuzzles.factory

import com.defey.labpuzzles.models.Chapter
import com.defey.labpuzzles.models.LevelBlock
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.chapter_10_block_1
import com.defey.labpuzzles.resources.chapter_10_block_2
import com.defey.labpuzzles.resources.chapter_10_block_3
import com.defey.labpuzzles.resources.chapter_10_block_4
import com.defey.labpuzzles.resources.chapter_10_block_5
import com.defey.labpuzzles.resources.chapter_1_block_1
import com.defey.labpuzzles.resources.chapter_1_block_2
import com.defey.labpuzzles.resources.chapter_1_block_3
import com.defey.labpuzzles.resources.chapter_1_block_4
import com.defey.labpuzzles.resources.chapter_1_block_5
import com.defey.labpuzzles.resources.chapter_2_block_1
import com.defey.labpuzzles.resources.chapter_2_block_2
import com.defey.labpuzzles.resources.chapter_2_block_3
import com.defey.labpuzzles.resources.chapter_2_block_4
import com.defey.labpuzzles.resources.chapter_2_block_5
import com.defey.labpuzzles.resources.chapter_3_block_1
import com.defey.labpuzzles.resources.chapter_3_block_2
import com.defey.labpuzzles.resources.chapter_3_block_3
import com.defey.labpuzzles.resources.chapter_3_block_4
import com.defey.labpuzzles.resources.chapter_3_block_5
import com.defey.labpuzzles.resources.chapter_4_block_1
import com.defey.labpuzzles.resources.chapter_4_block_2
import com.defey.labpuzzles.resources.chapter_4_block_3
import com.defey.labpuzzles.resources.chapter_4_block_4
import com.defey.labpuzzles.resources.chapter_4_block_5
import com.defey.labpuzzles.resources.chapter_5_block_1
import com.defey.labpuzzles.resources.chapter_5_block_2
import com.defey.labpuzzles.resources.chapter_5_block_3
import com.defey.labpuzzles.resources.chapter_5_block_4
import com.defey.labpuzzles.resources.chapter_5_block_5
import com.defey.labpuzzles.resources.chapter_6_block_1
import com.defey.labpuzzles.resources.chapter_6_block_2
import com.defey.labpuzzles.resources.chapter_6_block_3
import com.defey.labpuzzles.resources.chapter_6_block_4
import com.defey.labpuzzles.resources.chapter_6_block_5
import com.defey.labpuzzles.resources.chapter_7_block_1
import com.defey.labpuzzles.resources.chapter_7_block_2
import com.defey.labpuzzles.resources.chapter_7_block_3
import com.defey.labpuzzles.resources.chapter_7_block_4
import com.defey.labpuzzles.resources.chapter_7_block_5
import com.defey.labpuzzles.resources.chapter_8_block_1
import com.defey.labpuzzles.resources.chapter_8_block_2
import com.defey.labpuzzles.resources.chapter_8_block_3
import com.defey.labpuzzles.resources.chapter_8_block_4
import com.defey.labpuzzles.resources.chapter_8_block_5
import com.defey.labpuzzles.resources.chapter_9_block_1
import com.defey.labpuzzles.resources.chapter_9_block_2
import com.defey.labpuzzles.resources.chapter_9_block_3
import com.defey.labpuzzles.resources.chapter_9_block_4
import com.defey.labpuzzles.resources.chapter_9_block_5
import org.jetbrains.compose.resources.StringResource

class BlockDescriptionFactory {
    fun getBlockDescription(chapter: Chapter, block: LevelBlock): StringResource {
        return when (chapter) {
            Chapter.CHAPTER_1 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_1_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_1_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_1_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_1_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_1_block_5
            }

            Chapter.CHAPTER_2 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_2_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_2_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_2_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_2_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_2_block_5
            }

            Chapter.CHAPTER_3 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_3_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_3_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_3_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_3_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_3_block_5
            }

            Chapter.CHAPTER_4 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_4_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_4_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_4_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_4_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_4_block_5
            }

            Chapter.CHAPTER_5 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_5_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_5_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_5_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_5_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_5_block_5
            }

            Chapter.CHAPTER_6 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_6_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_6_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_6_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_6_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_6_block_5
            }

            Chapter.CHAPTER_7 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_7_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_7_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_7_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_7_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_7_block_5
            }

            Chapter.CHAPTER_8 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_8_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_8_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_8_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_8_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_8_block_5
            }

            Chapter.CHAPTER_9 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_9_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_9_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_9_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_9_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_9_block_5
            }

            Chapter.CHAPTER_10 -> when (block) {
                LevelBlock.BLOCK_1 -> Res.string.chapter_10_block_1
                LevelBlock.BLOCK_2 -> Res.string.chapter_10_block_2
                LevelBlock.BLOCK_3 -> Res.string.chapter_10_block_3
                LevelBlock.BLOCK_4 -> Res.string.chapter_10_block_4
                LevelBlock.BLOCK_5 -> Res.string.chapter_10_block_5
            }
        }
    }
}