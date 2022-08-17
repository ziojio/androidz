package com.example.demo.ui.base;

import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {

    protected void showToast(String msg) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show());
    }

}