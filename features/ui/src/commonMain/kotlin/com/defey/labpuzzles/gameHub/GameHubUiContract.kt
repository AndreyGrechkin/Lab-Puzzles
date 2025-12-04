package com.defey.labpuzzles.gameHub

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState
import com.defey.labpuzzles.models.Chapter
import com.defey.labpuzzles.models.CurrencyType
import com.defey.labpuzzles.models.GameType
import com.defey.labpuzzles.models.LevelBlock
import com.defey.labpuzzles.models.LevelState
import com.defey.labpuzzles.models.PlayerCurrencies

class GameHubUiContract {

    data class State(
        val currentChapter: Chapter = Chapter.CHAPTER_1,
        val currencies: PlayerCurrencies = PlayerCurrencies(),
        val currentBlock: LevelBlock = LevelBlock.BLOCK_1,
        val blockProgress: Float = 0.8f,
        val levelsCompleted: Int = 16,
        val totalLevels: Int = 20,
        val levels: List<LevelUI> = emptyList(),
        val isLoading: Boolean = false
    ) : UiState

    sealed interface Event : UiEvent {
        object OnChapterClick : Event
        object OnDailyQuestsClick : Event
        object OnAchievementsClick : Event
        object OnSettingsClick : Event
        data class OnAddCurrencyClick(val currencyType: CurrencyType) : Event
        data class OnLevelClick(val levelId: Int, val type: GameType) : Event
        object OnNextBlockClick : Event
        object OnPrevBlockClick : Event
    }

    sealed interface Action : UiAction {

    }

    data class LevelUI(
        val id: Int,
        val gameType: GameType,
        val state: LevelState,
        val stars: Int
    )
}