package com.example.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.demo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Fragment Container
 */
public class MultiFragmentActivity extends BaseActivity {
    private final int fragmentContainerId = R.id.fragment_container_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        String fragmentClass = getIntent().getStringExtra("fragment");
        if (fragmentClass != null) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.getFragmentFactory().instantiate(getClassLoader(), fragmentClass);
            Bundle arguments = getIntent().getBundleExtra("arguments");
            if (arguments != null) {
                fragment.setArguments(arguments);
            }
            manager.beginTransaction().add(fragmentContainerId, fragment).commit();
        }
    }

    public static void start(@NonNull Activity activity, @NonNull Class<? extends Fragment> fragment) {
        Intent intent = new Intent(activity, MultiFragmentActivity.class);
        intent.putExtra("fragment", fragment.getName());
        activity.startActivity(intent);
    }

    public static void start(@NonNull Activity activity, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle arguments) {
        Intent intent = new Intent(activity, MultiFragmentActivity.class);
        intent.putExtra("fragment", fragment.getName());
        if (arguments != null) {
            intent.putExtra("arguments", arguments);
        }
        activity.startActivity(intent);
    }

    public void pop() {
        pop(null, 0);
    }

    public void pop(@Nullable String name, int flags) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack(name, flags);
        }
    }

    public void push(@NonNull Fragment fragment) {
        push(fragment, null, null);
    }

    /**
     * @param name An optional name for this back stack state, or null.
     */
    public void push(@NonNull Fragment fragment, @Nullable String tag, @Nullable String name) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.replace(fragmentContainerId, fragment, tag);
        transaction.addToBackStack(name);
        transaction.commit();
    }
}
