package com.example.demo.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showToast(String text) {
        runOnUiThread(() -> Toast.makeText(this, text, Toast.LENGTH_SHORT).show());
    }

    // @Override
    // public Resources getResources() {
    //     if (ScreenUtil.isPortrait(UIApp.getApp())) {
    //         return AdaptScreenUtils.adaptWidth(super.getResources(), 360);
    //     } else {
    //         return AdaptScreenUtils.adaptHeight(super.getResources(), 640);
    //     }
    // }
}
