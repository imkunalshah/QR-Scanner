package com.kunal.qrscanner.ui.fragments

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.*
import com.kunal.qrscanner.databinding.FragmentScanBinding
import com.kunal.qrscanner.ui.fragments.dialog.QRDetailsBottomSheetDialogFragment
import com.kunal.qrscanner.ui.fragments.dialog.SymbolSelectionDialogFragment
import com.kunal.qrscanner.ui.viewmodels.MainViewModel
import com.kunal.qrscanner.utils.Constants
import com.kunal.qrscanner.ui.base.BaseFragment
import com.kunal.qrscanner.utils.giveHapticFeedback
import com.kunal.qrscanner.utils.inVisible
import com.kunal.qrscanner.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ScanFragment : BaseFragment<FragmentScanBinding>(FragmentScanBinding::inflate) {

    /**
     * This Is A Shared View Model
     * Which Is Shared Across All
     * The Fragments And The Containing Activity
     **/
    private val mainViewModel: MainViewModel by activityViewModels()

    private var codeScanner: CodeScanner? = null

    private var isSymbolSelectionDialogOpen = false
    private var isDetailsDialogOpen = false

    override fun initializeViews() {
        if (mainViewModel.currentSelectedSymbol.value == null || mainViewModel.currentSelectedSymbol.value?.isBlank() == true || mainViewModel.currentSelectedSymbol.value?.isEmpty() == true) {
            mainViewModel.currentSelectedSymbol.postValue(Constants.BITCOIN)
        }
        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        codeScanner?.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback { result ->
                codeScanner?.stopPreview()
                isDetailsDialogOpen = true
                Timber.d(result.text)
                context?.giveHapticFeedback()
                val qRDetailsDialog =
                    QRDetailsBottomSheetDialogFragment.newInstance(
                        result.text,
                        mainViewModel.currentSelectedSymbol.value
                    )
                qRDetailsDialog.also { dialog ->
                    dialog.onDialogDismiss = {
                        isDetailsDialogOpen = false
                        lifecycleScope.launch {
                            delay(1000L)
                            codeScanner?.startPreview()
                        }
                    }
                }
                qRDetailsDialog.show(
                    childFragmentManager,
                    Constants.QR_DETAILS_BOTTOM_SHEET_DIALOG_FRAGMENT
                )
            }
            errorCallback = ErrorCallback {
                Timber.e(it)
            }
        }
        binding.apply {
            symbolSelectionButton.setOnClickListener {
                codeScanner?.stopPreview()
                context?.giveHapticFeedback()
                isSymbolSelectionDialogOpen = true
                val symbolSelectionDialogFragment = SymbolSelectionDialogFragment.newInstance()
                symbolSelectionDialogFragment.also { dialog ->
                    dialog.onSymbolSelected = { symbol ->
                        mainViewModel.currentSelectedSymbol.postValue(symbol)
                    }
                    dialog.onDialogDismiss = {
                        isSymbolSelectionDialogOpen = false
                        lifecycleScope.launch {
                            delay(1000L)
                            codeScanner?.startPreview()
                        }
                    }
                }
                symbolSelectionDialogFragment.show(
                    childFragmentManager, Constants.SYMBOL_SELECTION_DIALOG_FRAGMENT
                )
            }
        }
    }

    override fun initializeObservers() {
        mainViewModel.currentSelectedSymbol.observe(viewLifecycleOwner) { symbol ->
            when (symbol) {
                Constants.BITCOIN -> {
                    binding.bitcoinSymbolImage.visible()
                    binding.etheriumSymbolImage.inVisible()
                    binding.symbolName.text = symbol
                }
                Constants.ETHEREUM -> {
                    binding.bitcoinSymbolImage.inVisible()
                    binding.etheriumSymbolImage.visible()
                    binding.symbolName.text = symbol
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        if (!isDetailsDialogOpen && !isSymbolSelectionDialogOpen) {
            codeScanner?.startPreview()
        }
    }

    override fun onPause() {
        Timber.d("onPause")
        codeScanner?.releaseResources()
        super.onPause()
    }
}