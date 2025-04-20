package com.example.demo.activity.databinding;

import android.os.Bundle;
import android.view.View;

import com.example.demo.R;
import com.example.demo.activity.BaseActivity;
import com.example.demo.databinding.ActivityDatabindingBinding;
import com.example.demo.util.Timber;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModelProvider;

public class DataBindingActivity extends BaseActivity implements View.OnClickListener {

    private final ObservableInt stateInt = new ObservableInt();
    private final ObservableArrayMap<String, String> stateArrayMap = new ObservableArrayMap<>();
    private final ObservableBoolean stateBoolean = new ObservableBoolean();
    private final ObservableArrayList<String> stateArrayList = new ObservableArrayList<>();
    private final ObservableField<String> stateString = new ObservableField<>();
    private final State<String> state = new State<>();
    private final ObservableField<String> state2 = new ObservableField<>() {
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
