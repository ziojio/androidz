package com.example.demo.ui.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.demo.databinding.FastKeyboardBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import timber.log.Timber;


public class FastKeyboardDialog extends AppCompatDialog {
    private final EditText editText;

    @SuppressLint("ClickableViewAccessibility")
    public static void bindEditText(EditText editText, Activity activity) {
        editText.setShowSoftInputOnFocus(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.setFocusedByDefault(false);
        }
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            Timber.d("editText OnFocusChangeListener");
            FastKeyboardDialog fastKeyboardDialog = new FastKeyboardDialog(activity, editText);
            if (hasFocus) {
                fastKeyboardDialog.show();
            }
        });
        editText.setOnTouchListener((v, event) -> {
            if (v.hasFocus() && event.getAction() == MotionEvent.ACTION_UP) {
                Timber.d("editText OnTouchListener");
                FastKeyboardDialog fastKeyboardDialog = new FastKeyboardDialog(activity, editText);
                fastKeyboardDialog.show();
            }
            return false;
        });
    }

    public FastKeyboardDialog(@NonNull Activity context, EditText editText) {
        super(context);
        this.editText = editText;

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setDimAmount(0.2f);
        getWindow().setLayout(-1, -2);
        getWindow().setBackgroundDrawable(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FastKeyboardBinding binding = FastKeyboardBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        binding.keyboard.setEditText(editText)
                .setNineGridNumber()
                .setKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
                    @Override
                    public void onPress(int primaryCode) {

                    }

                    @Override
                    public void onRelease(int primaryCode) {

                    }

                    @Override
                    public void onKey(int primaryCode, int[] keyCodes) {
                        if (primaryCode == Keyboard.KEYCODE_DONE) {
                            dismiss();
                        }
                    }

                    @Override
                    public void onText(CharSequence text) {

                    }

                    @Override
                    public void swipeLeft() {

                    }

                    @Override
                    public void swipeRight() {

                    }

                    @Override
                    public void swipeDown() {
                        dismiss();
                    }

                    @Override
                    public void swipeUp() {

                    }
                });
    }

}
