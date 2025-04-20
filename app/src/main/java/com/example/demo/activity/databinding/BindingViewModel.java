package com.example.demo.activity.databinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BindingViewModel extends ViewModel {

    public final MutableLiveData<String> datatime = new MutableLiveData<>(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(new Date()));

    public final MutableLiveData<String> imgUrl = new MutableLiveData<>(
            "https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7482b3ad2cd14edda31f05399c2ae759~tplv-k3u1fbpfcp-zoom-1.image");

}
