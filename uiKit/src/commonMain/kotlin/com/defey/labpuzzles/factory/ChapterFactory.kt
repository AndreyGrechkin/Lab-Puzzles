package com.defey.labpuzzles.factory

import com.defey.labpuzzles.models.Chapter
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_chapter_1
import com.defey.labpuzzles.resources.bg_chapter_10
import com.defey.labpuzzles.resources.bg_chapter_2
import com.defey.labpuzzles.resources.bg_chapter_3
import com.defey.labpuzzles.resources.bg_chapter_4
import com.defey.labpuzzles.resources.bg_chapter_5
import com.defey.labpuzzles.resources.bg_chapter_6
import com.defey.labpuzzles.resources.bg_chapter_7
import com.defey.labpuzzles.resources.bg_chapter_8
import com.defey.labpuzzles.resources.bg_chapter_9
import com.defey.labpuzzles.resources.chapter_10_description
import com.defey.labpuzzles.resources.chapter_10_title
import com.defey.labpuzzles.resources.chapter_1_description
import com.defey.labpuzzles.resources.chapter_1_title
import com.defey.labpuzzles.resources.chapter_2_description
import com.defey.labpuzzles.resources.chapter_2_title
import com.defey.labpuzzles.resources.chapter_3_description
import com.defey.labpuzzles.resources.chapter_3_title
import com.defey.labpuzzles.resources.chapter_4_description
import com.defey.labpuzzles.resources.chapter_4_title
import com.defey.labpuzzles.resources.chapter_5_description
import com.defey.labpuzzles.resources.chapter_5_title
import com.defey.labpuzzles.resources.chapter_6_description
import com.defey.labpuzzles.resources.chapter_6_title
import com.defey.labpuzzles.resources.chapter_7_description
import com.defey.labpuzzles.resources.chapter_7_title
import com.defey.labpuzzles.resources.chapter_8_description
import com.defey.labpuzzles.resources.chapter_8_title
import com.defey.labpuzzles.resources.chapter_9_description
import com.defey.labpuzzles.resources.chapter_9_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

class ChapterFactory {

    fun getBackground(chapter: Chapter) : DrawableResource =
        when(chapter) {
            Chapter.CHAPTER_1 -> Res.drawable.bg_chapter_1
            Chapter.CHAPTER_2 -> Res.drawable.bg_chapter_2
            Chapter.CHAPTER_3 -> Res.drawable.bg_chapter_3
            Chapter.CHAPTER_4 -> Res.drawable.bg_chapter_4
            Chapter.CHAPTER_5 -> Res.drawable.bg_chapter_5
            Chapter.CHAPTER_6 -> Res.drawable.bg_chapter_6
            Chapter.CHAPTER_7 -> Res.drawable.bg_chapter_7
            Chapter.CHAPTER_8 -> Res.drawable.bg_chapter_8
            Chapter.CHAPTER_9 -> Res.drawable.bg_chapter_9
            Chapter.CHAPTER_10 -> Res.drawable.bg_chapter_10
        }

    fun getTitle(chapter: Chapter): StringResource {
        return when (chapter) {
            Chapter.CHAPTER_1 -> Res.string.chapter_1_title
            Chapter.CHAPTER_2 -> Res.string.chapter_2_title
            Chapter.CHAPTER_3 -> Res.string.chapter_3_title
            Chapter.CHAPTER_4 -> Res.string.chapter_4_title
            Chapter.CHAPTER_5 -> Res.string.chapter_5_title
            Chapter.CHAPTER_6 -> Res.string.chapter_6_title
            Chapter.CHAPTER_7 -> Res.string.chapter_7_title
            Chapter.CHAPTER_8 -> Res.string.chapter_8_title
            Chapter.CHAPTER_9 -> Res.string.chapter_9_title
            Chapter.CHAPTER_10 -> Res.string.chapter_10_title
        }
    }

    fun getDescription(chapter: Chapter): StringResource {
        return when (chapter) {
            Chapter.CHAPTER_1 -> Res.string.chapter_1_description
            Chapter.CHAPTER_2 -> Res.string.chapter_2_description
            Chapter.CHAPTER_3 -> Res.string.chapter_3_description
            Chapter.CHAPTER_4 -> Res.string.chapter_4_description
            Chapter.CHAPTER_5 -> Res.string.chapter_5_description
            Chapter.CHAPTER_6 -> Res.string.chapter_6_description
            Chapter.CHAPTER_7 -> Res.string.chapter_7_description
            Chapter.CHAPTER_8 -> Res.string.chapter_8_description
            Chapter.CHAPTER_9 -> Res.string.chapter_9_description
            Chapter.CHAPTER_10 -> Res.string.chapter_10_description
        }
    }
}