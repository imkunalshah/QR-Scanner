package com.kunal.qrscanner.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunal.qrscanner.data.repository.HistoryRepository
import com.kunal.qrscanner.data.room.entities.ScanHistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var _historyItems: MutableLiveData<List<ScanHistoryItem>> =
        MutableLiveData<List<ScanHistoryItem>>()
    val historyItems: LiveData<List<ScanHistoryItem>> = _historyItems

    val currentSelectedSymbol: MutableLiveData<String> = MutableLiveData()

    fun getHistoryItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = historyRepository.getHistoryItems()
            _historyItems.postValue(items)
        }
    }

    fun saveHistoryItem(history: ScanHistoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.saveHistoryItem(history)
        }
    }

    fun deleteHistoryItem(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.deleteHistoryItem(id)
        }
    }

    fun resetHistoryList() {
        _historyItems.postValue(emptyList())
    }
}