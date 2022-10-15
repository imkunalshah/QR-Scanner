package com.kunal.qrscanner.ui.fragments

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kunal.qrscanner.data.room.entities.ScanHistoryItem
import com.kunal.qrscanner.utils.gestures.SwipeToDeleteCallback
import com.kunal.qrscanner.databinding.FragmentHistoryBinding
import com.kunal.qrscanner.ui.adapters.HistoryListAdapter
import com.kunal.qrscanner.ui.viewmodels.MainViewModel
import com.kunal.qrscanner.ui.base.BaseFragment
import com.kunal.qrscanner.ui.fragments.dialog.QRDetailsBottomSheetDialogFragment
import com.kunal.qrscanner.utils.Constants
import com.kunal.qrscanner.utils.giveHapticFeedback
import com.kunal.qrscanner.utils.gone
import com.kunal.qrscanner.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {

    /**
     * This Is A Shared View Model
     * Which Is Shared Across All
     * The Fragments And The Containing Activity
     **/
    private val mainViewModel: MainViewModel by activityViewModels()

    private val _historyListAdapter by lazy {
        HistoryListAdapter(
            emptyList(),
            ::onHistoryItemClick
        )
    }

    private var historyListAdapter: HistoryListAdapter? = null
        get() {
            kotlin.runCatching {
                field = _historyListAdapter
            }.onFailure {
                Timber.d("Error: $it")
                field = null
            }
            return field
        }

    private fun onHistoryItemClick(scanHistoryItem: ScanHistoryItem?, pos: Int) {
        val qRDetailsDialog =
            QRDetailsBottomSheetDialogFragment.newInstance(
                scanHistoryItem?.result,
                scanHistoryItem?.symbol
            )
        qRDetailsDialog.show(
            childFragmentManager,
            Constants.QR_DETAILS_BOTTOM_SHEET_DIALOG_FRAGMENT
        )
    }

    override fun initializeViews() {
        binding.apply {
            historyRv.apply {
                adapter = historyListAdapter
            }
        }
        enableSwipeToDelete()
    }

    override fun initializeObservers() {
        mainViewModel.historyItems.observe(viewLifecycleOwner) { items ->
            if (items.isNotEmpty()) {
                binding.emptyListOverlay.gone()
                binding.historyRv.visible()
                historyListAdapter?.updateHistoryList(items)
            }
        }
    }

    /**
     * This Is For Swipe To Delete.
     * Swipe and it will delete the item
     **/
    private fun enableSwipeToDelete() {
        val swipeToDeleteCallback: SwipeToDeleteCallback =
            object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
                    val history = historyListAdapter?.getData()?.get(position)
                    historyListAdapter?.removeItem(position)
                    mainViewModel.deleteHistoryItem(history?.id)
                    context?.giveHapticFeedback()
                    if (historyListAdapter?.getData()?.isEmpty() == true) {
                        binding.emptyListOverlay.visible()
                        binding.historyRv.gone()
                    }
                }
            }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.historyRv)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getHistoryItems()
    }

    override fun onPause() {
        super.onPause()
        historyListAdapter?.clearData()
        mainViewModel.resetHistoryList()
    }
}