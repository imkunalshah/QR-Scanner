package com.kunal.qrscanner.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "scan_history_item")
data class ScanHistoryItem(
    var result: String,
    var symbol: String,
    var date: Date,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
