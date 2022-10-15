package com.kunal.qrscanner.data.repository

import com.kunal.qrscanner.data.room.AppDatabase
import com.kunal.qrscanner.data.room.entities.ScanHistoryItem

class HistoryRepository(
    private val appDatabase: AppDatabase
) {

    suspend fun saveHistoryItem(item: ScanHistoryItem) {
        appDatabase.getHistoryDao().insertHistoryItem(item)
    }

    fun getHistoryItems(): List<ScanHistoryItem> {
        return appDatabase.getHistoryDao().getAllHistory()
    }

    suspend fun deleteHistoryItem(id: Int?) {
        appDatabase.getHistoryDao().deleteHistoryItem(id)
    }

}