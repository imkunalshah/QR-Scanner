package com.kunal.qrscanner.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.kunal.qrscanner.R
import com.kunal.qrscanner.data.room.entities.ScanHistoryItem
import com.kunal.qrscanner.databinding.LayoutHistoryItemBinding
import com.kunal.qrscanner.utils.Constants
import com.kunal.qrscanner.utils.getFormattedDate
import com.kunal.qrscanner.utils.pxFromDp

class HistoryListAdapter(
    historyList: List<ScanHistoryItem>,
    private val onHistoryItemClick: ((item: ScanHistoryItem?, position: Int) -> Unit)
) : RecyclerView.Adapter<HistoryListAdapter.HistoryItemViewHolder>() {

    private var _historyList: MutableList<ScanHistoryItem>? = null
    var context: Context? = null

    init {
        _historyList = historyList.toMutableList()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryItemViewHolder {
        context = parent.context
        val binding =
            LayoutHistoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return HistoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val task = _historyList?.get(position)
        holder.bind(task)
    }

    override fun getItemCount() = _historyList?.size ?: 0

    @SuppressLint("NotifyDataSetChanged")
    fun updateHistoryList(newHistoryList: List<ScanHistoryItem>) {
        _historyList?.let { historyList ->
            newHistoryList.let { newHistoryList ->
                historyList.clear()
                historyList.addAll(newHistoryList)
                notifyDataSetChanged()
            }
        }
    }

    fun getData(): ArrayList<ScanHistoryItem> {
        return ArrayList(_historyList ?: emptyList())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int) {
        _historyList?.let { historyList ->
            if (historyList.isNotEmpty()) {
                _historyList?.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        _historyList?.let { historyList ->
            if (historyList.isNotEmpty()) {
                historyList.clear()
                notifyDataSetChanged()
            }
        }
    }

    inner class HistoryItemViewHolder(private val binding: LayoutHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: ScanHistoryItem?) {
            when (history?.symbol) {
                Constants.BITCOIN -> {
                    binding.symbolName.text = Constants.BITCOIN
                    binding.symbolImage.setImageResource(R.drawable.ic_btc)
                    context?.pxFromDp(8)?.let { binding.symbolImage.setPadding(it) }
                }
                Constants.ETHEREUM -> {
                    binding.symbolName.text = Constants.ETHEREUM
                    binding.symbolImage.setImageResource(R.drawable.ic_eth)
                    context?.pxFromDp(14)?.let { binding.symbolImage.setPadding(it) }
                }
            }
            binding.date.text = "Scanned On ${getFormattedDate(history?.date ?: 0)}"
            binding.root.setOnClickListener {
                onHistoryItemClick.invoke(history, adapterPosition)
            }
        }

    }
}