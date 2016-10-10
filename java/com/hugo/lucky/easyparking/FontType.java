package com.hugo.lucky.easyparking;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by lucky on 6/11/2016.
 */
/*FrontType Repository*/
public class FontType {

    public static Typeface getCaviarDreams(Context context)
    {
       return  Typeface.createFromAsset(context.getAssets(),"fonts/Caviar_Dreams_Bold.ttf");
    }
    public static Typeface getLobster(Context context)
    {
        return Typeface.createFromAsset(context.getAssets(),"fonts/Lobster_1.3.otf");
    }
    public static Typeface getBlackJack(Context context)
    {
        return Typeface.createFromAsset(context.getAssets(),"fonts/blackjack.otf");
    }
}
