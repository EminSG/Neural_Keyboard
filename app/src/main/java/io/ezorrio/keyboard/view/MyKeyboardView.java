package io.ezorrio.keyboard.view;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;

import io.ezorrio.keyboard.R;
import io.ezorrio.keyboard.constants.Symbols;
import io.ezorrio.keyboard.utils.Utils;

import static io.ezorrio.keyboard.constants.ShiftState.STATE_CAPS;
import static io.ezorrio.keyboard.constants.ShiftState.STATE_SHIFT;

/**
 * Created by golde on 08.02.2017.
 */

public class MyKeyboardView extends KeyboardView {
    private Context mContext;
    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setAnimation(null);
    }

    public void setAllShifted(int value) {
        boolean shifted = value == STATE_SHIFT || value == STATE_CAPS;
        getKeyboard().setShifted(shifted);
        setShifted(shifted);
        invalidateAllKeys();
    }

    @Override
    protected boolean onLongPress(Keyboard.Key popupKey){
        int xmlResId = 0;
        if (Symbols.DOT.equals(popupKey.label)){
            xmlResId = R.xml.popup;
        }

        if (xmlResId == 0){
            return false;
        }

        PopupKeyboard popup = new PopupKeyboard(mContext, xmlResId);
        updatePopupPosition(popup, popupKey);
        return true;
    }

    private void updatePopupPosition(PopupKeyboard popup, Keyboard.Key popupKey){
        if(popup.isShowing()){
            popup.update(popupKey.x - popup.getWidth(), popupKey.y - (int) Utils.dpToPx(mContext, 36), -1, -1);
        } else {
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.showAtLocation(this, Gravity.NO_GRAVITY, popupKey.x - popup.getWidth(), popupKey.y - (int) Utils.dpToPx(mContext, 36));
        }
    }

}
