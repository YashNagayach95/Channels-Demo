package com.example.channelsdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.channelsdemo.databinding.ActivityMainChannelBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity_Channel : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainChannelBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainChannelBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewBinding.buttonSnackbar.setOnClickListener {
            viewModel.triggerEvent()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlows.collect {
                    when (it) {
                        is MainViewModel.MyEvents.ErrorEvent -> {
                            Snackbar.make(viewBinding.root, it.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}