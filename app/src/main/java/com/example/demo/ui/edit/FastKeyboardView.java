package com.example.demo.ui.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.demo.R;

import timber.log.Timber;


public class FastKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    private EditText editText;
    private Keyboard keyboard;
    private KeyboardView.OnKeyboardActionListener keyboardActionListener;

    public FastKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initKeyboard();
    }

    public FastKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initKeyboard();
    }

    @Override
    public void swipeUp() {
        if (keyboardActionListener != null) {
            keyboardActionListener.swipeUp();
        }
    }

    @Override
    public void swipeDown() {
        Timber.d("swipeDown");
        if (keyboardActionListener != null) {
            keyboardActionListener.swipeDown();
        }
    }

    @Override
    public void swipeRight() {
        if (keyboardActionListener != null) {
            keyboardActionListener.swipeRight();
        }
    }

    @Override
    public void swipeLeft() {
        if (keyboardActionListener != null) {
            keyboardActionListener.swipeLeft();
        }
    }

    @Override
    public void onText(CharSequence text) {
        if (keyboardActionListener != null) {
            keyboardActionListener.onText(text);
        }
    }

    @Override
    public void onRelease(int primaryCode) {
        if (keyboardActionListener != null) {
            keyboardActionListener.onRelease(primaryCode);
        }
    }

    @Override
    public void onPress(int primaryCode) {
        if (keyboardActionListener != null) {
            keyboardActionListener.onPress(primaryCode);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = editText.getText();
        int len = editable.length();
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                if (len > 0) {
                    int start = editText.getSelectionStart();
                    int end = editText.getSelectionEnd();
                    if (start == -1) {
                        editable.delete(len - 1, len);
                    } else {
                        if (start == end) {
                            if (start > 0) {
                                editable.delete(start - 1, start);
                            }
                        } else {
                            editable.delete(start, end);
                        }
                    }
                }
                break;
            case Keyboard.KEYCODE_DONE:
                clearFocus();
                break;
            default:
                String ch = Character.toString((char) primaryCode);
                int start = editText.getSelectionStart();
                if (len == 0 || start == -1) {
                    editable.append(ch);
                } else {
                    int end = editText.getSelectionEnd();
                    editable.replace(start, end, ch);
                }
                break;
        }
        if (keyboardActionListener != null) {
            keyboardActionListener.onKey(primaryCode, keyCodes);
        }
    }

    public void initKeyboard() {
        setOnKeyboardActionListener(this);
        setPreviewEnabled(false);

        // DEFAULT
        setLetter();
    }

    public FastKeyboardView setEditText(EditText editText) {
        this.editText = editText;
        return this;
    }

    public FastKeyboardView setKeyboardActionListener(OnKeyboardActionListener keyboardActionListener) {
        this.keyboardActionListener = keyboardActionListener;
        return this;
    }

    public FastKeyboardView setNineGridNumber() {
        keyboard = new Keyboard(getContext(), R.xml.keyboard_nine_grid_number, 9);
        setKeyboard(keyboard);
        return this;
    }

    public FastKeyboardView setNineGridNumberOnly() {
        keyboard = new Keyboard(getContext(), R.xml.keyboard_nine_grid_number, R.id.keyboard_number_only);
        setKeyboard(keyboard);
        return this;
    }

    public FastKeyboardView setNineGridNumber(boolean only) {
        keyboard = new Keyboard(getContext(), R.xml.keyboard_nine_grid_number,
                only ? R.id.keyboard_number_only : R.id.keyboard_number);
        setKeyboard(keyboard);
        return this;
    }

    public FastKeyboardView setLetter() {
        keyboard = new Keyboard(getContext(), R.xml.keyboard_letter);
        setKeyboard(keyboard);
        return this;
    }

    public FastKeyboardView setSymbol() {
        keyboard = new Keyboard(getContext(), R.xml.keyboard_symbol);
        setKeyboard(keyboard);
        return this;
    }

    public FastKeyboardView enableBlankClose() {
        getRootView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hide();
                return false;
            }
        });
        return this;
    }

    @SuppressLint("ClickableViewAccessibility")
    public FastKeyboardView bindEditText(EditText editText) {
        Timber.d("FastKeyboardView with editText ");
        this.editText = editText;

        editText.setShowSoftInputOnFocus(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.setFocusedByDefault(false);
        }
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            Timber.d("editText hasFocus " + hasFocus);
            if (hasFocus) {
                if (!v.isSelected()) {
                    v.setSelected(true);
                    show();
                }
            } else {
                hide();
            }
        });
        editText.setOnTouchListener((v, event) -> {
            Timber.d("editText OnTouchListener");
            if (v.hasFocus()) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    show();
                }
            } else {
                v.setSelected(false);
            }
            return false;
        });
        return this;
    }


    public void show() {
        Timber.d("show ");
        int visibility = getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            hideSystemSoftInput();
            setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        Timber.d("hide ");
        int visibility = getVisibility();
        if (visibility == View.VISIBLE) {
            setVisibility(View.GONE);
        }
    }

    public void clearFocus() {
        Timber.d("clearFocus ");
        editText.clearFocus();
    }

    public void hideSystemSoftInput() {
        Timber.d("hideSystemSoftInput ");
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Keyboard keyboard = getKeyboard();
        // if (keyboard == null) return;
        // List<Keyboard.Key> keys = keyboard.getKeys();
        // if (keys != null && keys.size() > 0) {
        //     Paint paint = new Paint();
        //     paint.setTextAlign(Paint.Align.CENTER);
        //     Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        //     paint.setTypeface(font);
        //     paint.setAntiAlias(true);
        //     for (Keyboard.Key key : keys) {
        //         if (key.codes[0] == -4) {
        //             Drawable dr = getContext().getResources().getDrawable(R.drawable.keyboard_blue);
        //             dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        //             dr.draw(canvas);
        //         } else {
        //             Drawable dr = getContext().getResources().getDrawable(R.drawable.keyboard_white);
        //             dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        //             dr.draw(canvas);
        //         }
        //         if (key.label != null) {
        //             if (key.codes[0] == -4 ||
        //                     key.codes[0] == -5) {
        //                 paint.setTextSize(17 * 2);
        //             } else {
        //                 paint.setTextSize(20 * 2);
        //             }
        //             if (key.codes[0] == -4) {
        //                 paint.setColor(getContext().getResources().getColor(android.R.color.white));
        //             } else {
        //                 paint.setColor(Color.parseColor("#03A9F4"));
        //             }
        //             Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
        //             Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        //             int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //             // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        //             paint.setTextAlign(Paint.Align.CENTER);
        //             canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
        //         }
        //     }
        // }
    }
}