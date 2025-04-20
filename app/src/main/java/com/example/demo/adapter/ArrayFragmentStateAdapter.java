package com.example.demo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ArrayFragmentStateAdapter extends FragmentStateAdapter {
    private final Fragment[] fragments;

    public ArrayFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, @NonNull Fragment[] fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    public ArrayFragmentStateAdapter(@NonNull Fragment fragment, @NonNull Fragment[] fragments) {
        super(fragment);
        this.fragments = fragments;
    }

    public ArrayFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, @NonNull Fragment[] fragments) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
