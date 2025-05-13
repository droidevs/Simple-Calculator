package io.droidevs.calculatorplus.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = HistoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["historyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["historyId"])]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val favId: Long = 0,
    val historyId: Long
)
