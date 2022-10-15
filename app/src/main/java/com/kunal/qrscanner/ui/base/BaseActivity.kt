package com.kunal.qrscanner.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * This Is A Base Activity
 * All New Activities Can
 * Extend This Base Activity Class
 **/
abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity() {

    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        initializeViews()
        initializeObservers()
    }

    abstract fun initializeViews()

    abstract fun initializeObservers()

}