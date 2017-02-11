package io.ezorrio.keyboard.utils;

import android.util.Log;
import android.util.Pair;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import java.util.Objects;

import static io.ezorrio.keyboard.constants.Symbols.DOT;
import static io.ezorrio.keyboard.constants.Symbols.ENTER;
import static io.ezorrio.keyboard.constants.Symbols.EXCLAMATION;
import static io.ezorrio.keyboard.constants.Symbols.QUESTION;

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
        if (mInputConnection == null){
            return true;
        }

        String textStr = Utils.charSeqToString(mInputConnection.getTextBeforeCursor(2, InputConnection.GET_TEXT_WITH_STYLES));
        Log.d(TAG, textStr == null ? "null" : textStr);
        return (textStr == null || textStr.isEmpty() ||
                textStr.endsWith(DOT + SPACE) ||
                textStr.endsWith(QUESTION + SPACE) ||
                textStr.endsWith(EXCLAMATION + SPACE) ||
                textStr.endsWith(ENTER));
    }

    public String getCurrentWord() {
        if (mInputConnection == null){
            return null;
        }
        String beforeCursor = Utils.charSeqToString(mInputConnection.getTextBeforeCursor(20, InputConnection.GET_TEXT_WITH_STYLES));
        String afterCursor = Utils.charSeqToString(mInputConnection.getTextAfterCursor(20, InputConnection.GET_TEXT_WITH_STYLES));

        if (beforeCursor == null && afterCursor == null){
            return null;
        }

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
        if (mInputConnection == null){
            return null;
        }
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

    public void updateInputConnection(InputConnection inputConnection){
        this.mInputConnection = inputConnection;
    }

}
