package com.example.demo.ui.databinding;

import android.os.Bundle;
import android.view.View;

import com.example.demo.R;
import com.example.demo.databinding.ActivityDatabindingBinding;
import com.example.demo.ui.base.BaseActivity;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModelProvider;
import timber.log.Timber;


public class DataBindingActivity extends BaseActivity {

    private final State<String> state = new State<>();
    private final State<Boolean> state2 = new State<>(false, false);
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
        BindingViewModel bindingViewModel = new ViewModelProvider(this).get(BindingViewModel.class);
        binding.setModel(bindingViewModel);
        binding.setHandler(new EventHandler());

        binding.titlebar.title.setText("DataBinding");

        binding.execFunction.setOnClickListener(v -> {
            Timber.d("execFunction ");
        });
    }

    public static class EventHandler {

        public void openDialog(View view) {
            Timber.d("openDialog: ");
        }

    }

}
