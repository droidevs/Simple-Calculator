package io.droidevs.calculatorplus.data

import androidx.room.Embedded
import androidx.room.Relation

data class FavoriteWithHistory(
    @Embedded val favorite: FavoriteEntity,
    @Relation(
        parentColumn = "historyId",
        entityColumn = "id"
    )
    val history: HistoryEntity
)
