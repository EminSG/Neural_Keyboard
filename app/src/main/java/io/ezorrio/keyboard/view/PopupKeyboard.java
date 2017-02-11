package io.ezorrio.keyboard.view;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import io.ezorrio.keyboard.R;
import io.ezorrio.keyboard.utils.Utils;

/**
 * Created by golde on 11.02.2017.
 */

public class PopupKeyboard extends PopupWindow implements KeyboardView.OnKeyboardActionListener {
    private InputConnection inputConnection;
    private Keyboard popupKeyboard;
    private KeyboardView keyboardView;
    public PopupKeyboard(Context context, int xmlResId){
        this.inputConnection = ((InputMethodService) context).getCurrentInputConnection();
        View custom = LayoutInflater.from(context).inflate(R.layout.popup_keyboard, new FrameLayout(context), false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            custom.setElevation(Utils.dpToPx(context, 5));
        }

        keyboardView = (KeyboardView) custom.findViewById(R.id.keyboard_view);

        popupKeyboard = new Keyboard(context, xmlResId);
        keyboardView.setKeyboard(popupKeyboard);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(this);

        setContentView(custom);
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
        dismiss();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        char code = (char) primaryCode;
        inputConnection.commitText(String.valueOf(code), 1);
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

    }

    @Override
    public void swipeUp() {

    }
}
