package com.example.demo.ui.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.RadioGroup;

import com.example.demo.databinding.ActivityMainBinding;
import com.example.demo.ui.base.BaseActivity;

import androidx.annotation.NonNull;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {
    private int count = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        new CountDownTimer(count * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                count = 0;
            }
        }.start();
        splashScreen.setKeepOnScreenCondition(() -> count > 0);
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 1) {
                    return new DeviceFragment();
                }
                return new HomeFragment();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
        binding.viewpager.setUserInputEnabled(false);
        binding.bottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                binding.viewpager.setCurrentItem(group.indexOfChild(group.findViewById(checkedId)));
            }
        });
    }

}
