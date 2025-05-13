package io.droidevs.calculatorplus.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import androidx.annotation.VisibleForTesting

@Database(
    entities = [HistoryEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(/* Your custom type converters here if needed */)
abstract class AppDatabase : RoomDatabase() {

    // Define abstract DAOs
    // abstract fun yourDao(): YourDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the database.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db_calculator_plus"
                )
                    // Add .fallbackToDestructiveMigration() or custom .addMigrations(...) if needed
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * For testing or resetting the database in memory.
         */
        @VisibleForTesting
        fun resetInstance() {
            INSTANCE = null
        }
    }
}
