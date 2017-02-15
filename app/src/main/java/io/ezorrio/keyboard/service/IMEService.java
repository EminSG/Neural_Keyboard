package io.ezorrio.keyboard.service;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import io.ezorrio.keyboard.R;
import io.ezorrio.keyboard.utils.AppUtils;
import io.ezorrio.keyboard.utils.InputConnectionHelper;
import io.ezorrio.keyboard.view.MyKeyboardView;
import io.ezorrio.keyboard.view.SuggestionsView;

import static io.ezorrio.keyboard.constants.Keycodes.KEYCODE_ABC;
import static io.ezorrio.keyboard.constants.Keycodes.KEYCODE_NUMBERS;
import static io.ezorrio.keyboard.constants.Keycodes.KEYCODE_SPACE;
import static io.ezorrio.keyboard.constants.Keycodes.KEYCODE_TOGGLE_LANGUAGE;
import static io.ezorrio.keyboard.constants.ShiftState.STATE_CAPS;
import static io.ezorrio.keyboard.constants.ShiftState.STATE_NO_SHIFT;
import static io.ezorrio.keyboard.constants.ShiftState.STATE_SHIFT;

/**
 * Created by golde on 30.01.2017.
 */

public class IMEService extends InputMethodService implements KeyboardView.OnKeyboardActionListener,
        SpellCheckerSession.SpellCheckerSessionListener, SuggestionsView.SuggestionViewCallback {

    private static final String TAG = "IMEService";

    private static final int EN = 0;
    private static final int RU = 1;

    private SuggestionsView mSuggestionsView;
    private MyKeyboardView mKeyboardView;
    private Keyboard mKeyboard;
    private InputConnection mInputConnection;
    private InputConnectionHelper mInputConnectionHelper;
    private SpellCheckerSession mSpellCheckerSession;

    private int mState = STATE_NO_SHIFT;
    private int mCurrentLanguage = EN;

    private Keyboard mKeyboardRussian;
    private Keyboard mKeyboardEnglish;
    private Keyboard mKeyboardSymbols;

    @Override
    public void onInitializeInterface() {
        mKeyboardRussian = new Keyboard(this, R.xml.qwerty_ru);
        mKeyboardSymbols = new Keyboard(this, R.xml.keyboard_symbols);
        mKeyboardEnglish = new Keyboard(this, R.xml.qwerty_en);
    }

    @Override
    public View onCreateInputView() {
        View root = getLayoutInflater().inflate(R.layout.keyboard, null, false);

        mKeyboardView = (MyKeyboardView) root.findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setOnKeyboardActionListener(this);
        mKeyboardView.setPreviewEnabled(false);

        mSuggestionsView  = (SuggestionsView) root.findViewById(R.id.suggestion_view);
        mSuggestionsView.setCallback(this);
        refreshShiftState();
        return root;
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        mInputConnection = getCurrentInputConnection();
        mInputConnectionHelper = new InputConnectionHelper(mInputConnection);
        switch (attribute.inputType&EditorInfo.TYPE_MASK_CLASS) {
            case EditorInfo.TYPE_CLASS_NUMBER:
            case EditorInfo.TYPE_CLASS_DATETIME:
                mKeyboard = mKeyboardSymbols;
                break;
            case EditorInfo.TYPE_CLASS_TEXT:
                mKeyboard = mCurrentLanguage == EN ? mKeyboardEnglish : mKeyboardRussian;
                break;
        }
        mState = STATE_NO_SHIFT;
    }

    @Override
    public void onPress(int primaryCode) {
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
            case Keyboard.KEYCODE_SHIFT:
            case Keyboard.KEYCODE_DONE:
            case KEYCODE_NUMBERS:
            case KEYCODE_ABC:
            case KEYCODE_SPACE:
            case KEYCODE_TOGGLE_LANGUAGE:
                return;
            default:
                //mKeyboardView.showPopup(mKeyboard.getKeys().);
        }
    }

    @Override
    public void onRelease(int primaryCode) {
        //mKeyboardView.hidePopup();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        updateConnections();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                if (mInputConnectionHelper.isInSelectionMode()){
                    getCurrentInputConnection().commitText("",1);
                } else {
                    mInputConnection.deleteSurroundingText(1, 0);
                }
                break;
            case Keyboard.KEYCODE_SHIFT:
                mState++;
                if (mState > STATE_CAPS) {
                    mState = STATE_NO_SHIFT;
                }
                mKeyboardView.setAllShifted(mState);
                return;
            case Keyboard.KEYCODE_DONE:
                mInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case KEYCODE_NUMBERS:
                setCurrentKeyboard(mKeyboardSymbols);
                break;
            case KEYCODE_ABC:
                setCurrentKeyboard(mCurrentLanguage == EN ? mKeyboardEnglish : mKeyboardRussian);
                break;
            case KEYCODE_TOGGLE_LANGUAGE:
                setCurrentKeyboard(mCurrentLanguage == EN ? mKeyboardRussian : mKeyboardEnglish);
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && (mState == STATE_SHIFT || mState == STATE_CAPS)) {
                    code = Character.toUpperCase(code);
                }
                mInputConnection.commitText(String.valueOf(code), 1);
        }
        refreshShiftState();
        getSuggestions();
    }

    @Override
    public void onText(CharSequence text) {
        refreshShiftState();
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

    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    private void refreshShiftState() {
        if (mState == STATE_SHIFT) {
            mState = STATE_NO_SHIFT;
        }
        if (mInputConnectionHelper.needShift()) {
            mState = STATE_SHIFT;
        }
        mKeyboardView.setAllShifted(mState);
    }

    private void setCurrentKeyboard(Keyboard value) {
        mKeyboard = value;
        mKeyboardView.setKeyboard(mKeyboard);
    }

    private Locale currentLocale(){
        switch (mCurrentLanguage){
            case RU:
                return new Locale("ru","RU");
            case EN:
            default:
                return Locale.ENGLISH;
        }
    }

    private void getSuggestions() {
        final TextServicesManager tsm = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        mSpellCheckerSession = tsm.newSpellCheckerSession(null, currentLocale(), this, false);
        if (mSpellCheckerSession == null){
            Toast.makeText(this, "You don\'t have a spell checker for this language" , Toast.LENGTH_SHORT).show();
        }
        if (mInputConnectionHelper.getCurrentWord() != null && !mInputConnectionHelper.getCurrentWord().isEmpty()) {
            mSpellCheckerSession.getSuggestions(new TextInfo(mInputConnectionHelper.getCurrentWord()), 3);
        }
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] results) {
        if (results == null) {
            mSuggestionsView.clearSuggestions();
            return;
        }
        ArrayList<String> suggestions = new ArrayList<>();
        for (SuggestionsInfo result : results) {
            String tmp = "";
            for (int j = 0; j < result.getSuggestionsCount(); j++) {
                suggestions.add(result.getSuggestionAt(j));
            }
            Log.d(TAG, tmp);

        }

        mSuggestionsView.updateSuggestions(suggestions);
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
    }


    @Override
    public void onSuggestionClicked(int which) {
        String wordForInsert = mSuggestionsView.getSuggestion(which);
        String currentWord = mInputConnectionHelper.getCurrentWord();
        mInputConnection.finishComposingText();
        if (currentWord != null) {
            mInputConnection.deleteSurroundingText(currentWord.length(), 0);
        }
        mInputConnection.setComposingText(wordForInsert, 1);
        mInputConnection.finishComposingText();
        mSuggestionsView.clearSuggestions();
    }

    private void updateConnections(){
        mInputConnection = getCurrentInputConnection();
        mInputConnectionHelper.updateInputConnection(mInputConnection);
    }
}
