package com.kunal.qrscanner.ui.fragments.dialog

import android.content.DialogInterface
import com.kunal.qrscanner.databinding.FragmentDialogSymbolSelectionBinding
import com.kunal.qrscanner.utils.Constants
import com.kunal.qrscanner.ui.base.BaseDialogFragment
import com.kunal.qrscanner.utils.giveHapticFeedback

class SymbolSelectionDialogFragment :
    BaseDialogFragment<FragmentDialogSymbolSelectionBinding>(FragmentDialogSymbolSelectionBinding::inflate) {

    var onSymbolSelected: ((String) -> Unit)? = null

    var onDialogDismiss: (() -> Unit)? = null

    companion object {
        fun newInstance(): SymbolSelectionDialogFragment {
            return SymbolSelectionDialogFragment()
        }
    }

    override fun initializeViews() {
        binding.apply {
            bitcoin.setOnClickListener {
                context?.giveHapticFeedback()
                onSymbolSelected?.invoke(Constants.BITCOIN)
                dismiss()
            }
            etherium.setOnClickListener {
                context?.giveHapticFeedback()
                onSymbolSelected?.invoke(Constants.ETHEREUM)
                dismiss()
            }
        }
    }

    override fun initializeObservers() {

    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogDismiss?.invoke()
        super.onDismiss(dialog)
    }
}