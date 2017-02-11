package io.ezorrio.keyboard.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.ezorrio.keyboard.R;
/**
 * Created by golde on 01.02.2017.
 */

public class SuggestionsView extends FrameLayout {

    private TextView mSuggestionOne;
    private TextView mSuggestionTwo;
    private TextView mSuggestionThree;

    public SuggestionsView(Context context) {
        super(context);
        init();
    }

    public SuggestionsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    protected void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_suggestion_bar, this);
        mSuggestionOne = (TextView) findViewById(R.id.suggestion_1);
        mSuggestionTwo = (TextView) findViewById(R.id.suggestion_2);
        mSuggestionThree = (TextView) findViewById(R.id.suggestion_3);
    }

    public void updateSuggestions(String suggestion1, String suggestion2, String suggestion3) {
        mSuggestionOne.setText(suggestion1);
        mSuggestionTwo.setText(suggestion2);
        mSuggestionThree.setText(suggestion3);
    }

    public void updateSuggestions(ArrayList<String> suggestions) {
        switch (suggestions.size()){
            case 0:
                return;
            case 1:
                updateSuggestions("", suggestions.get(0), "");
                return;
            case 2:
                updateSuggestions(suggestions.get(0), suggestions.get(1), "");
                return;
            default:
                updateSuggestions(suggestions.get(0), suggestions.get(1), suggestions.get(2));
        }
    }

    public void setCallback(final SuggestionViewCallback callback){
        mSuggestionOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSuggestionClicked(1);
            }
        });

        mSuggestionTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSuggestionClicked(2);
            }
        });

        mSuggestionThree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSuggestionClicked(3);
            }
        });
    }

    public String getSuggestion(int which){
        switch (which){
            case 1:
                return mSuggestionOne.getText().toString();
            case 2:
                return mSuggestionTwo.getText().toString();
            case 3:
                return mSuggestionThree.getText().toString();
        }
        return "";
    }

    public interface SuggestionViewCallback {
        void onSuggestionClicked(int which);
    }

}
