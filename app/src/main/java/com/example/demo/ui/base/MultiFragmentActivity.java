package com.example.demo.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.demo.databinding.ActivityFragmentContainerBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 显示一个 Fragment
 */
public class MultiFragmentActivity extends BaseActivity {

    public static void start(@NonNull Activity activity, @NonNull Fragment fragment) {
        start(activity, fragment.getClass(), fragment.getArguments());
    }


    public static void start(@NonNull Activity activity, @NonNull Class<? extends Fragment> fragment) {
        start(activity, fragment, null);
    }

    public static void start(@NonNull Activity activity, @NonNull Class<? extends Fragment> fragment,
                             @Nullable Bundle arguments) {
        Intent intent = new Intent(activity, MultiFragmentActivity.class);
        intent.putExtra("fragment", fragment.getName());
        if (arguments != null) {
            intent.putExtra("arguments", arguments);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFragmentContainerBinding binding = ActivityFragmentContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.fragmentContainerView);
        final int containerId = binding.fragmentContainerView.getId();

        final String fragmentClass = getIntent().getStringExtra("fragment");
        if (fragmentClass != null) {
            final FragmentManager manager = getSupportFragmentManager();
            final Fragment fragment = manager.getFragmentFactory().instantiate(getClassLoader(), fragmentClass);
            final Bundle arguments = getIntent().getBundleExtra("arguments");
            if (arguments != null) {
                fragment.setArguments(arguments);
            }
            manager.beginTransaction().add(containerId, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 0) {
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
