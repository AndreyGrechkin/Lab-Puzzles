package com.defey.labpuzzles.gameHub

import androidx.lifecycle.SavedStateHandle
import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel
import com.defey.labpuzzles.models.Chapter
import com.defey.labpuzzles.models.GameType
import com.defey.labpuzzles.models.LevelBlock
import com.defey.labpuzzles.models.LevelState
import com.defey.labpuzzles.models.Screen

class GameHubViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigationManager: NavigationManager,

    ) : BaseViewModel<GameHubUiContract.Event,
        GameHubUiContract.State,
        GameHubUiContract.Action>(initialState = GameHubUiContract.State()) {

    init {
        val chapterArg = savedStateHandle.get<String>("chapter") ?: ""
        val chapter = Chapter.valueOf(chapterArg)
        val list = generateLevelsForBlock(LevelBlock.BLOCK_1)
        updateState {
            copy(
                currentChapter = chapter,
                currentBlock = LevelBlock.BLOCK_1,
                levels = list
            )
        }
//        generateTestLevels()
    }

    override suspend fun handleEvent(event: GameHubUiContract.Event) {
        when (event) {
            GameHubUiContract.Event.OnAchievementsClick -> println("Event: $event")
            is GameHubUiContract.Event.OnAddCurrencyClick -> println("Event: ${event.currencyType}")
            GameHubUiContract.Event.OnChapterClick ->
                navigationManager.navigate(Screen.ChaptersScreen)
            GameHubUiContract.Event.OnDailyQuestsClick -> println("Event: $event")
            GameHubUiContract.Event.OnSettingsClick -> println("Event: $event")
            is GameHubUiContract.Event.OnLevelClick ->
                navigationManager.navigate(Screen.WaterSortScreen.createRoute(event.levelId.toString()))
            GameHubUiContract.Event.OnNextBlockClick -> {
                println("Event: $event")
                val nextBlock = getNextBlock(state.value.currentBlock)
                if (nextBlock != null) {
                    updateState {
                        copy(
                            currentBlock = nextBlock,
                            levels = generateLevelsForBlock(nextBlock),
                            blockProgress = calculateProgressForBlock(nextBlock),
                            levelsCompleted = getCompletedLevelsCount(nextBlock)
                        )
                    }
                }
            }

            GameHubUiContract.Event.OnPrevBlockClick -> {
                println("Event: $event")
                val prevBlock = getPrevBlock(state.value.currentBlock)
                if (prevBlock != null) {
                    updateState {
                        copy(
                            currentBlock = prevBlock,
                            levels = generateLevelsForBlock(prevBlock),
                            blockProgress = calculateProgressForBlock(prevBlock),
                            levelsCompleted = getCompletedLevelsCount(prevBlock)
                        )
                    }
                }
            }
        }
    }

    private fun getNextBlock(current: LevelBlock): LevelBlock? {
        return LevelBlock.fromNumber(current.blockNumber + 1)
    }

    private fun getPrevBlock(current: LevelBlock): LevelBlock? {
        return LevelBlock.fromNumber(current.blockNumber - 1)
    }

    private fun generateLevelsForBlock(block: LevelBlock): List<GameHubUiContract.LevelUI> {
        val startLevelId = (block.blockNumber - 1) * 20 + 1
        return List(20) { index ->
            val levelId = startLevelId + index
            val stars = when {
                levelId < 5 -> 3
                levelId < 8 -> 2
                levelId < 10 -> 1
                else -> 0
            }
            println("LEVEL: levelId ${levelId}")
            GameHubUiContract.LevelUI(
                id = levelId,
                gameType = getGameTypeForLevel(levelId),
                state = LevelState(
                    isLocked = levelId > 25, // Для теста: первые 25 уровней открыты
                    isCompleted = levelId < 10, // Для теста: первые 10 пройдены
                    stars = stars
                ),
                stars = stars
            )
        }
    }

    private fun getGameTypeForLevel(levelId: Int): GameType {
        return when (levelId % 4) {
            0 -> GameType.WATER_SORT
            1 -> GameType.FLOW_FREE
            2 -> GameType.SUDOKU
            else -> GameType.SLIDING_PUZZLE
        }
    }

    private fun calculateProgressForBlock(block: LevelBlock): Float {
        // Заглушка для прогресса
        return when (block.blockNumber) {
            1 -> 0.8f
            2 -> 0.4f
            3 -> 0.1f
            else -> 0f
        }
    }

    private fun getCompletedLevelsCount(block: LevelBlock): Int {
        // Заглушка
        return when (block.blockNumber) {
            1 -> 16
            2 -> 8
            3 -> 2
            else -> 0
        }
    }
}