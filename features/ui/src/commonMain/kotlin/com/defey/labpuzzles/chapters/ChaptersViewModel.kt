package com.defey.labpuzzles.chapters

import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel
import com.defey.labpuzzles.models.Chapter
import com.defey.labpuzzles.models.Screen

class ChaptersViewModel(
    private val navigationManager: NavigationManager,
): BaseViewModel<ChaptersUiContract.Event,
        ChaptersUiContract.State,
        ChaptersUiContract.Action>(initialState = ChaptersUiContract.State()) {

    init {
        loadChapters()
    }

    override suspend fun handleEvent(event: ChaptersUiContract.Event) {
        when(event) {
            ChaptersUiContract.Event.OnBack -> navigationManager.popBackStack()
            is ChaptersUiContract.Event.OnChapterSelect -> {
                navigationManager.clearNavigation()
                navigationManager.navigate(Screen.GameHubScreen.createRoute(event.chapter))
            }
        }
    }

    private fun loadChapters() {
        // Заглушка - в реальности данные из БД
        val chapters = Chapter.entries.map { chapter ->
            ChaptersUiContract.ChapterListItem(
                chapter = chapter,
                isUnlocked = chapter.ordinal <= 5, // Только глава 1 открыта для теста
                completedLevels = if (chapter.ordinal == 0) 100 else 0,
            )
        }
            updateState { copy(chapters = chapters) }
    }
}