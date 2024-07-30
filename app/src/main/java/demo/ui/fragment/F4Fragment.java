package demo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import demo.databinding.ActivityFuncBinding;
import demo.ui.base.MultiFragment;
import timber.log.Timber;

public class F4Fragment extends MultiFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityFuncBinding binding = ActivityFuncBinding.inflate(inflater, container, false);

        binding.titlebar.title.setText(getClass().getSimpleName());
        binding.titlebar.left.setOnClickListener(v -> pop());

        binding.execFunction.setOnClickListener(v -> Timber.d("execFunction"));
        return binding.getRoot();
    }

}

