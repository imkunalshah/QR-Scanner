package com.kunal.qrscanner.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kunal.qrscanner.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val currentSelectedSymbol: MutableLiveData<String> = MutableLiveData()

}