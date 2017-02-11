package io.ezorrio.keyboard.utils;

import android.util.Log;
import android.util.Pair;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import java.util.Objects;

/**
 * Created by golde on 11.02.2017.
 */

public class InputConnectionHelper {
    private static final String TAG = "InputConnectionHelper";

    private static final String SPACE = " ";
    private InputConnection mInputConnection;

    public InputConnectionHelper(InputConnection inputConnection){
        this.mInputConnection = inputConnection;
    }

    public boolean needShift() {
        String text = mInputConnection.getTextBeforeCursor(2, InputConnection.GET_TEXT_WITH_STYLES).toString();
        if (text == null){
            return true;
        }
        return (text.isEmpty() || text.endsWith(". ") || text.endsWith("? ") || text.endsWith("! "));
    }

    public String getCurrentWord() {
        String beforeCursor = mInputConnection.getTextBeforeCursor(20, InputConnection.GET_TEXT_WITH_STYLES).toString();
        String afterCursor = mInputConnection.getTextAfterCursor(20, InputConnection.GET_TEXT_WITH_STYLES).toString();
        String wholeText = beforeCursor + afterCursor;
        int leftSpace = beforeCursor.lastIndexOf(SPACE);
        int rightSpace = afterCursor.indexOf(SPACE);
        if (rightSpace == -1) {
            return wholeText.substring(leftSpace + 1);
        }
        try {
            return wholeText.substring(leftSpace + 1, rightSpace);
        } catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return null;
    }

    public Pair<Integer, Integer> getCursorPositions(){
        ExtractedText et = mInputConnection.getExtractedText(new ExtractedTextRequest(), 0);
        if (et == null){
            return new Pair<>(0,0);
        }
        Integer selectionStart = et.selectionStart;
        Integer selectionEnd = et.selectionEnd;
        Log.d(TAG, selectionStart + " " + selectionEnd);
        return new Pair<>(selectionStart, selectionEnd);
    }

    public boolean isInSelectionMode(){
        Pair<Integer, Integer> value = getCursorPositions();
        return !value.first.equals(value.second);
    }

}
