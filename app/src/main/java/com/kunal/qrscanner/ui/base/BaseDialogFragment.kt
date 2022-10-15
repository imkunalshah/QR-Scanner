package com.kunal.qrscanner.ui.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.kunal.qrscanner.R

abstract class BaseDialogFragment<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
    DialogFragment() {

    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindingFactory(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager
            ?.defaultDisplay
            ?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val newWidth = (width * 80) / 100
        view.layoutParams.width = newWidth
        initializeViews()
        initializeObservers()
    }

    abstract fun initializeViews()

    abstract fun initializeObservers()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded)
        return dialog
    }
}