package io.ezorrio.keyboard.view;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import static io.ezorrio.keyboard.constants.ShiftState.STATE_CAPS;
import static io.ezorrio.keyboard.constants.ShiftState.STATE_SHIFT;

/**
 * Created by golde on 08.02.2017.
 */

public class MyKeyboardView extends KeyboardView {
    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        setAnimation(null);
        //getKeyboard().getKeys().get(getKeyboard().getShiftKeyIndex()).
    }

    public void setAllShifted(int value) {
        boolean shifted = value == STATE_SHIFT || value == STATE_CAPS;
        getKeyboard().setShifted(shifted);
        invalidateAllKeys();
        setShifted(shifted);
    }
}
