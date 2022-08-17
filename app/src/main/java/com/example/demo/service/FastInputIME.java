package com.example.demo.service;

import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.R;

public class FastInputIME extends InputMethodService implements View.OnClickListener {

    private static final String TAG = "FastInputIME";
    private final boolean debug = false;

    private int currentInputType = InputType.TYPE_CLASS_TEXT;
    private int upperFlag = 0; // 1 to Upper once, 2 keep upper state

    private final SparseIntArray array = new SparseIntArray(40);

    {
        array.put(R.id.input_0, '0');
        array.put(R.id.input_1, '1');
        array.put(R.id.input_2, '2');
        array.put(R.id.input_3, '3');
        array.put(R.id.input_4, '4');
        array.put(R.id.input_5, '5');
        array.put(R.id.input_6, '6');
        array.put(R.id.input_7, '7');
        array.put(R.id.input_8, '8');
        array.put(R.id.input_9, '9');

        array.put(R.id.input_a, 'a');
        array.put(R.id.input_b, 'b');
        array.put(R.id.input_c, 'c');
        array.put(R.id.input_d, 'd');
        array.put(R.id.input_e, 'e');
        array.put(R.id.input_f, 'f');
        array.put(R.id.input_g, 'g');
        array.put(R.id.input_h, 'h');
        array.put(R.id.input_i, 'i');
        array.put(R.id.input_j, 'j');
        array.put(R.id.input_k, 'k');
        array.put(R.id.input_l, 'l');
        array.put(R.id.input_m, 'm');
        array.put(R.id.input_n, 'n');
        array.put(R.id.input_o, 'o');
        array.put(R.id.input_p, 'p');
        array.put(R.id.input_q, 'q');
        array.put(R.id.input_r, 'r');
        array.put(R.id.input_s, 's');
        array.put(R.id.input_t, 't');
        array.put(R.id.input_u, 'u');
        array.put(R.id.input_v, 'v');
        array.put(R.id.input_w, 'w');
        array.put(R.id.input_x, 'x');
        array.put(R.id.input_y, 'y');
        array.put(R.id.input_z, 'z');
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log("onCreate ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            log("shouldOfferSwitchingToNextInputMethod=" + shouldOfferSwitchingToNextInputMethod());
        }
    }

    @Override
    public View onCreateInputView() {
        log("onCreateInputView ");
        return createInputView();
    }

    public View createInputView() {
        View inputView = getLayoutInflater().inflate(R.layout.fast_input_qwert, null);
        for (int i = 0, len = array.size(); i < len; i++) {
            int id = array.keyAt(i);
            char c = (char) array.valueAt(i);
            View view = inputView.findViewById(id);
            if (view != null) {
                view.setOnClickListener(v -> {
                    InputConnection input = getCurrentInputConnection();
                    String text = String.valueOf(c);
                    if (upperFlag > 0) {
                        text = text.toUpperCase();
                        if (upperFlag == 1) {
                            upperFlag = 0;
                            showCharUpper(inputView, false);
                        }
                    }
                    input.commitText(text, 1);
                });
            }
        }
        inputView.findViewById(R.id.input_upper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upperFlag = upperFlag > 0 ? 0 : 1;
                showCharUpper(inputView, upperFlag > 0);
            }
        });
        inputView.findViewById(R.id.input_upper).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                upperFlag = upperFlag > 0 ? 0 : 2;
                showCharUpper(inputView, upperFlag > 0);
                return true;
            }
        });
        inputView.findViewById(R.id.input_123).setOnClickListener(this);
        inputView.findViewById(R.id.input_space).setOnClickListener(this);
        inputView.findViewById(R.id.input_delete).setOnClickListener(this);
        inputView.findViewById(R.id.input_linebreak).setOnClickListener(this);
        inputView.findViewById(R.id.input_close).setOnClickListener(this);
        return inputView;
    }

    private View createInputNumberView() {
        View inputView = getLayoutInflater().inflate(R.layout.fast_input, null);
        for (int i = 0, len = array.size(); i < len; i++) {
            int id = array.keyAt(i);
            char c = (char) array.valueAt(i);
            View view = inputView.findViewById(id);
            if (view != null) {
                view.findViewById(id).setOnClickListener(v -> {
                    InputConnection input = getCurrentInputConnection();
                    input.commitText(String.valueOf(c), 1);
                });
            }
        }
        inputView.findViewById(R.id.input_delete).setOnClickListener(this);
        inputView.findViewById(R.id.input_done).setOnClickListener(this);
        inputView.findViewById(R.id.input_close).setOnClickListener(this);
        return inputView;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        log("onStartInputView info=" + info);
        log("onStartInputView restarting=" + restarting);
        int type = info.inputType & InputType.TYPE_MASK_CLASS;
        log("onStartInputView currentInputType=" + currentInputType);
        log("onStartInputView type=" + type);
        log("TYPE_CLASS_TEXT=" + InputType.TYPE_CLASS_TEXT + " TYPE_CLASS_NUMBER=" + InputType.TYPE_CLASS_NUMBER);
        if (type != currentInputType) {
            if (type == InputType.TYPE_CLASS_TEXT) {
                setInputView(createInputView());
            } else if (type == InputType.TYPE_CLASS_NUMBER) {
                setInputView(createInputNumberView());
            }
            currentInputType = type;
        }
    }

    @Override
    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
        super.onCurrentInputMethodSubtypeChanged(newSubtype);
        log("onCurrentInputMethodSubtypeChanged newSubtype=" + newSubtype);
        log("onCurrentInputMethodSubtypeChanged getName=" + getString(newSubtype.getNameResId()));
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy ");
    }

    private void showCharUpper(View view, boolean isUpper) {
        for (int i = 0, len = array.size(); i < len; i++) {
            int id = array.keyAt(i);
            char c = (char) array.valueAt(i);
            TextView t = view.findViewById(id);
            if (t != null) {
                String ch = String.valueOf(c);
                t.setText(isUpper ? ch.toUpperCase() : ch);
            }
        }
        ImageView up = view.findViewById(R.id.input_upper_icon);
        if (isUpper) {
            up.setImageResource(R.drawable.input_uppercase_enable);
        } else {
            up.setImageResource(R.drawable.input_uppercase);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        InputConnection input = getCurrentInputConnection();
        if (id == R.id.input_close) {
            requestHideSelf(InputMethodManager.HIDE_NOT_ALWAYS);
        } else if (id == R.id.input_delete) {
            input.deleteSurroundingText(1, 0);
        } else if (id == R.id.input_space) {
            input.commitText(" ", 1);
        } else if (id == R.id.input_done) {
            requestHideSelf(InputMethodManager.HIDE_NOT_ALWAYS);
        } else if (id == R.id.input_linebreak) {
            input.commitText("\n", 1);
        } else if (id == R.id.input_123) {
            View view = createInputNumberView();
            TextView done = view.findViewById(R.id.input_done);
            done.setText("返回");
            done.setOnClickListener(view1 -> {
                setInputView(createInputView());
            });
            setInputView(view);
        }
    }

    private void log(String msg) {
        if (debug) {
            Log.d(TAG, msg);
        }
    }
}
