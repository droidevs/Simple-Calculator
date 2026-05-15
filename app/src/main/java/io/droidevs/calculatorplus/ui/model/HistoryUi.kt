package io.droidevs.calculatorplus.ui.model

data class HistoryUi (
    val expression: String,
    val result: String,
    val timeStamp: Long,
    val isFavored: Boolean
)