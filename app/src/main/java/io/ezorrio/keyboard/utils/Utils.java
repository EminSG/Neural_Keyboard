package io.ezorrio.keyboard.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by golde on 11.02.2017.
 */

public class Utils {
    public static float dpToPx(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static String charSeqToString(CharSequence sequence) {
        return sequence == null ? null : sequence.toString();
    }
}
