package io.ezorrio.keyboard.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by golde on 11.02.2017.
 */

public class AppUtils {
    public static void changeAppLanguage(Context context, String languageCode){
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(languageCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }
}
