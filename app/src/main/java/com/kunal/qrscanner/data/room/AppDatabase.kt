package com.kunal.qrscanner.data.room

import android.content.Context
import androidx.room.*
import com.kunal.qrscanner.R
import com.kunal.qrscanner.data.room.entities.ScanHistoryItem
import com.kunal.qrscanner.data.room.type_converters.Converters

@Database(
    entities = [ScanHistoryItem::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                context.resources.getString(R.string.app_name)
            ).fallbackToDestructiveMigration()
                .build()
    }
}