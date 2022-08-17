package com.example.demo.ui.edit;

import android.os.Bundle;

import com.example.demo.databinding.ActivityEditBinding;
import com.example.demo.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import timber.log.Timber;

public class EditActivity extends BaseActivity {
    private ActivityEditBinding binding;
    private SimpleDateFormat format;
    private ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        launcher = registerForActivityResult(new ActivityResultContracts.CreateDocument(), result -> {
            Timber.d("result=" + result);
        });
        setContentView(binding.getRoot());

        // binding.etText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        // binding.etText.addTextChangedListener(new TextWatcher() {
        //     @Override
        //     public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //         Timber.d("beforeTextChanged: s=%s, start=%s, count=%s, after=%s", s, start, count, after);
        //     }
        //
        //     @Override
        //     public void onTextChanged(CharSequence s, int start, int before, int count) {
        //         Timber.d("onTextChanged: s=%s, start=%s, before=%s, count=%s", s, start, before, count);
        //     }
        //
        //     @Override
        //     public void afterTextChanged(Editable s) {
        //         Timber.d("afterTextChanged: s=%s", s);
        //     }
        // });

        binding.bindNum.setOnClickListener(v -> {
            binding.keyboard.bindEditText(binding.etText).setNineGridNumber();
        });
        binding.bindLetter.setOnClickListener(v -> {
            binding.keyboard.bindEditText(binding.etLetter).setLetter();
        });
        binding.bindMark.setOnClickListener(v -> {
            binding.keyboard.bindEditText(binding.etMark).setSymbol();
        });
    }

}
