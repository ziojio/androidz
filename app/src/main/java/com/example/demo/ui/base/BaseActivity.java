package com.example.demo.ui.base;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

//    @Override
//    public Resources getResources() {
//        if (ScreenUtil.isPortrait(UIApp.getApp())) {
//            return AdaptScreenUtils.adaptWidth(super.getResources(), 360);
//        } else {
//            return AdaptScreenUtils.adaptHeight(super.getResources(), 640);
//        }
//    }

    protected void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

}
