package io.ezorrio.keyboard.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;

import io.ezorrio.keyboard.R;
import io.ezorrio.keyboard.utils.Utils;
import static io.ezorrio.keyboard.constants.ShiftState.STATE_CAPS;
import static io.ezorrio.keyboard.constants.ShiftState.STATE_NO_SHIFT;
import static io.ezorrio.keyboard.constants.ShiftState.STATE_SHIFT;

/**
 * Created by golde on 08.02.2017.
 */

public class MyKeyboardView extends KeyboardView {
    private static final String COLOR_NO_ACTIVE = "#B0B0B0";
    private Context mContext;
    private PopupKeyboard mPopup;
    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }

    private void init(){

    }

    private void setShiftIcon(int value){
        int shiftIndex = getKeyboard().getShiftKeyIndex();
        if (shiftIndex == -1){
            return;
        }
        switch (value){
            case STATE_NO_SHIFT:
                getKeyboard().getKeys().get(shiftIndex).icon = ContextCompat.getDrawable(mContext, R.drawable.ic_keyboard_shift);
                getKeyboard().getKeys().get(shiftIndex).icon.mutate().setColorFilter(Color.parseColor(COLOR_NO_ACTIVE), PorterDuff.Mode.MULTIPLY);
                break;
            case STATE_SHIFT:
                getKeyboard().getKeys().get(shiftIndex).icon = ContextCompat.getDrawable(mContext, R.drawable.ic_keyboard_shift);
                getKeyboard().getKeys().get(shiftIndex).icon.mutate().setColorFilter(null);
                break;
            case STATE_CAPS:
                getKeyboard().getKeys().get(shiftIndex).icon = ContextCompat.getDrawable(mContext, R.drawable.ic_keyboard_caps);
                break;
        }
    }

    public void setAllShifted(int value) {
        boolean shifted = value == STATE_SHIFT || value == STATE_CAPS;
        setShiftIcon(value);
        getKeyboard().setShifted(shifted);
        setShifted(shifted);
        invalidateAllKeys();
    }

    @Override
    protected boolean onLongPress(Keyboard.Key popupKey){
        return false;
    }

    public void showPopup(Keyboard.Key popupKey){
        mPopup = new PopupKeyboard(mContext, popupKey);
        updatePopupPosition(mPopup, popupKey);
    }

    public void hidePopup(){
        if (mPopup != null) {
            mPopup.dismiss();
        }
    }

    private void updatePopupPosition(PopupKeyboard popup, Keyboard.Key popupKey){
        if(popup.isShowing()){
            popup.update(popupKey.x - popup.getWidth(), popupKey.y - (int) Utils.dpToPx(mContext, 12), -1, -1);
        } else {
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.showAtLocation(this, Gravity.NO_GRAVITY, popupKey.x - popup.getWidth(), popupKey.y - (int) Utils.dpToPx(mContext, 36));
        }
    }

}
