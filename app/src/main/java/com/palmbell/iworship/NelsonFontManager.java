package com.palmbell.iworship;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Nelson on 4/7/2018.
 */

public class NelsonFontManager {


    public static final String ROOT = "fonts/";
    public static final String FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }


}
