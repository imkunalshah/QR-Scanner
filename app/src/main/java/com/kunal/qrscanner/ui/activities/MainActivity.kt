package com.kunal.qrscanner.ui.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kunal.qrscanner.R
import com.kunal.qrscanner.databinding.ActivityMainBinding
import com.kunal.qrscanner.ui.base.BaseActivity
import com.kunal.qrscanner.ui.viewmodels.MainViewModel
import com.kunal.qrscanner.utils.Constants
import com.kunal.qrscanner.utils.showLongToast
import com.kunal.qrscanner.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    /**
     * This Is A Shared View Model
     * Which Is Shared Across All
     * The Fragments And The Containing Activity
     **/
    val mainViewModel: MainViewModel by viewModels()

    var doubleBackToExitPressedOnce = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // PERMISSION GRANTED
            showToast(Constants.CAMERA_PERMISSION_SUCCESS)
            setUpBottomNavigation()
        } else {
            // PERMISSION NOT GRANTED
            showLongToast(Constants.CAMERA_PERMISSION_FAILURE)
            lifecycleScope.launch {
                delay(1500)
                finish()
            }
        }
    }

    companion object {
        fun callingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        var keepSplashOnScreen = true
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        lifecycleScope.launch {
            delay(1500)
            keepSplashOnScreen = false
        }
        super.onCreate(savedInstanceState)
    }

    override fun initializeViews() {
        requestCameraPermission()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finish()
                    return
                }
                doubleBackToExitPressedOnce = true
                showToast(Constants.PRESS_BACK_AGAIN_TO_EXIT)
                lifecycleScope.launch {
                    delay(2000L)
                    doubleBackToExitPressedOnce = false
                }
            }
        })
    }

    private fun requestCameraPermission() {
        val cameraPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            setUpBottomNavigation()
        }
    }

    private fun setUpBottomNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun initializeObservers() {

    }

}