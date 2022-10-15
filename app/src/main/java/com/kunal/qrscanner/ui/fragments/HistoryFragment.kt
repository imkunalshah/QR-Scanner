package com.kunal.qrscanner.ui.fragments

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kunal.qrscanner.utils.gestures.SwipeToDeleteCallback
import com.kunal.qrscanner.databinding.FragmentHistoryBinding
import com.kunal.qrscanner.ui.viewmodels.MainViewModel
import com.kunal.qrscanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun initializeViews() {
        enableSwipeToDelete()
    }

    override fun initializeObservers() {

    }

    /**
     * This Is For Swipe To Delete.
     * Swipe and it will delete a task
     **/
    private fun enableSwipeToDelete() {
        val swipeToDeleteCallback: SwipeToDeleteCallback =
            object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
//                    val task = eventListAdapter?.getData()?.get(position)
//                    eventListAdapter?.removeItem(position)
//                    if (eventListAdapter?.getData()?.isEmpty() == true) {
//                        view.emptyListOverlay.visible()
//                        view.eventListRecyclerView.gone()
//                    }
//                    viewModel.deleteEvent(task?.taskId.toString())
                }
            }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.historyRv)
    }
}