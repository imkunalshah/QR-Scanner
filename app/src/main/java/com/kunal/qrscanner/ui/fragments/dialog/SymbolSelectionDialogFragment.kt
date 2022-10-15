package com.kunal.qrscanner.ui.fragments.dialog

import com.kunal.qrscanner.databinding.FragmentDialogSymbolSelectionBinding
import com.kunal.qrscanner.utils.Constants
import com.kunal.qrscanner.ui.base.BaseDialogFragment

class SymbolSelectionDialogFragment :
    BaseDialogFragment<FragmentDialogSymbolSelectionBinding>(FragmentDialogSymbolSelectionBinding::inflate) {

    var onSymbolSelected: ((String) -> Unit)? = null

    companion object {
        fun newInstance(): SymbolSelectionDialogFragment {
            return SymbolSelectionDialogFragment()
        }
    }

    override fun initializeViews() {
        binding.apply {
            bitcoin.setOnClickListener {
                //haptic feedback here

                onSymbolSelected?.invoke(Constants.BITCOIN)
                dismiss()
            }
            etherium.setOnClickListener {
                //haptic feedback here

                onSymbolSelected?.invoke(Constants.ETHEREUM)
                dismiss()
            }
        }
    }

    override fun initializeObservers() {

    }
}