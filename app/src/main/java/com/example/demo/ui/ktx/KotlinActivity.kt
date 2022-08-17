package com.example.demo.ui.ktx

import android.os.Bundle
import androidx.activity.viewModels
import com.example.demo.databinding.ActivityKtBinding
import com.example.demo.ui.base.BaseActivity
import timber.log.Timber


class KotlinActivity : BaseActivity() {
    companion object {
        private const val TAG = "KotlinActivity"
    }

    private val viewModel: KtxViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityKtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ktx.setOnClickListener {
            Timber.d("onCreate: ${viewModel.date.value}")
        }

        Timber.d("onCreate: $applicationInfo")
    }

}
