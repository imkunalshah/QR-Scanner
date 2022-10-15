package com.kunal.qrscanner.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "scan_history_item", indices = [Index(value = ["result"], unique = true)])
data class ScanHistoryItem(
    @ColumnInfo(name = "result")
    var result: String,
    var symbol: String,
    var date: Long,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
