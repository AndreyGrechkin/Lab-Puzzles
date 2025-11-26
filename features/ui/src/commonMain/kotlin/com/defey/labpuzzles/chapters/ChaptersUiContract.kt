package com.defey.labpuzzles.chapters

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState
import com.defey.labpuzzles.models.Chapter

class ChaptersUiContract {

    data class State(
        val chapters: List<ChapterListItem> = emptyList()
    ): UiState

    sealed interface Event: UiEvent {
        data class OnChapterSelect(val chapter: Chapter) : Event
        data object OnBack : Event
    }

    sealed interface Action: UiAction {

    }

    data class ChapterListItem(
        val chapter: Chapter,
        val isUnlocked: Boolean,
//        val progress: Int, // 0-100
        val completedLevels: Int, // 0-100
//        val stars: Int // 0-3
    )
}