package com.kunal.qrscanner.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kunal.qrscanner.data.room.entities.ScanHistoryItem

@Dao
interface HistoryDao {

    @Query("SELECT * FROM scan_history_item")
    fun getAllHistory(): List<ScanHistoryItem>

    @Query("DELETE FROM scan_history_item WHERE id = :id")
    suspend fun deleteHistoryItem(id: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(history: ScanHistoryItem)

    @Query("SELECT * FROM scan_history_item WHERE symbol = :symbol")
    fun getAllHistoryOfParticularSymbol(symbol: String): List<ScanHistoryItem>

}