package com.example.demo.activity.databinding;

import android.os.Bundle;
import android.view.View;

import com.example.demo.R;
import com.example.demo.activity.BaseActivity;
import com.example.demo.databinding.ActivityDatabindingBinding;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModelProvider;
import timber.log.Timber;


public class DataBindingActivity extends BaseActivity implements View.OnClickListener {

    private final State<String> state = new State<>();
    private final State<Boolean> state2 = new State<>(false);
    private final ObservableField<String> state3 = new ObservableField<>() {
        @Override
        public void set(String value) {
            boolean isUnChanged = this.get() == value;
            super.set(value);
            if (isUnChanged) {
                this.notifyChange();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDatabindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);
        ViewModelProvider provider = new ViewModelProvider(this);
        BindingViewModel viewModel = provider.get(BindingViewModel.class);
        binding.setModel(viewModel);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.execFunction.setOnClickListener(v -> {
            Timber.d("execFunction ");
        });
    }

    public void onClick(View view) {
        Timber.d("openDialog: ");
    }
}
