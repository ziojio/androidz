package com.example.demo.ui.ktx

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KtxViewModel : ViewModel() {
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    val date: MutableLiveData<String> = MutableLiveData(format.format(Date()))
}
