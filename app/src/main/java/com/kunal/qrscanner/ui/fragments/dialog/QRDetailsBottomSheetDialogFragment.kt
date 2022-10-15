package com.kunal.qrscanner.ui.fragments.dialog

import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.kunal.qrscanner.R
import com.kunal.qrscanner.databinding.FragmentBottomSheetQrDetailsBinding
import com.kunal.qrscanner.utils.Constants
import com.kunal.qrscanner.ui.base.BaseBottomSheetDialogFragment
import com.kunal.qrscanner.utils.gestures.getQrCodeBitmap
import com.kunal.qrscanner.utils.gone
import com.kunal.qrscanner.utils.showToast
import com.kunal.qrscanner.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


class QRDetailsBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<FragmentBottomSheetQrDetailsBinding>(
        FragmentBottomSheetQrDetailsBinding::inflate
    ) {

    var onDialogDismiss: (() -> Unit)? = null

    companion object {
        fun newInstance(data: String, symbol: String?): QRDetailsBottomSheetDialogFragment {
            val args = Bundle()
            args.apply {
                putString(Constants.SCANNED_DATA, data)
                putString(Constants.SYMBOL, symbol)
            }
            val fragment = QRDetailsBottomSheetDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initializeViews() {
        val bitCoinRegex = Constants.BITCOIN_REGEX.toRegex()
        val ethereumRegex = Constants.ETHEREUM_REGEX.toRegex()
        val symbol = arguments?.getString(Constants.SYMBOL) ?: ""
        val data = arguments?.getString(Constants.SCANNED_DATA) ?: ""
        when (symbol) {
            Constants.BITCOIN -> {
                binding.successAnim.setAnimation(R.raw.bitcoin)
            }
            Constants.ETHEREUM -> {
                binding.successAnim.setAnimation(R.raw.etherium)
            }
        }
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(
            requireActivity().resources.getString(R.string.app_name), data
        )
        binding.apply {
            dataText.text = data
            dataText.setOnClickListener {
                //haptic feedback here
                clipboard.setPrimaryClip(clip)
                requireContext().showToast(Constants.COPIED_TO_CLIPBOARD)
            }
            if (bitCoinRegex.matches(data) && symbol == Constants.BITCOIN) {
                failureAnim.gone()
                successAnim.visible()
                shareButton.visible()
            } else if (ethereumRegex.matches(data) && symbol == Constants.ETHEREUM) {
                failureAnim.gone()
                successAnim.visible()
                shareButton.visible()
            } else {
                shareButton.gone()
                successAnim.gone()
                failureAnim.visible()
            }

            shareButton.setOnClickListener {
                //haptic feedback here
                val qrCodeBitmap = getQrCodeBitmap(data)
                cacheQRCodeAndShare(qrCodeBitmap, data)
            }
        }
    }

    override fun initializeObservers() {

    }

    private fun cacheQRCodeAndShare(image: Bitmap, data: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val imagesFolder = File(requireContext().cacheDir, "images")
            kotlin.runCatching {
                var uri: Uri? = null
                imagesFolder.mkdirs()
                val file = File(imagesFolder, "$data.png")
                val stream = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.PNG, 90, stream)
                stream.flush()
                stream.close()
                uri = FileProvider.getUriForFile(requireContext(), "com.kunal.fileprovider", file)
                shareImageUri(uri)
            }.onFailure { t ->
                Timber.e("IOException while trying to write file for sharing: $t")
            }
        }
    }

    private fun shareImageUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        startActivity(intent)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogDismiss?.invoke()
        super.onDismiss(dialog)
    }
}