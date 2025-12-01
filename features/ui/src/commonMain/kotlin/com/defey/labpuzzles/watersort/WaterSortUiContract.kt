package com.defey.labpuzzles.watersort

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState
import com.defey.labpuzzles.models.CurrencyType
import com.defey.labpuzzles.models.PlayerCurrencies
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.water_sort.PourDirection

class WaterSortUiContract {

    data class State(
        val isLoading: Boolean = true,
        val levelId: String = "",
        val vials: List<Vial> = emptyList(),
        val selectedVialIndex: Int? = null,
        val movesCount: Int = 0,
        val elapsedTime: Long = 0L,
        val isLevelCompleted: Boolean = false,
        val currencies: PlayerCurrencies = PlayerCurrencies(),
    ) : UiState

    sealed interface Event : UiEvent {
        data object OnBackClick : Event
        data object OnRetry : Event
        data class OnAddCurrencyClick(val currencyType: CurrencyType) : Event
        data class OnVialClick(val index: Int, val direction: PourDirection?) : Event
    }

    sealed interface Action : UiAction {

    }
}