package com.defey.labpuzzles.models

abstract class GameState {
    abstract val movesCount: Int
    abstract val isCompleted: Boolean
}