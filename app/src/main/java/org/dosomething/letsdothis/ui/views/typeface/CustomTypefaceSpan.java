package org.dosomething.letsdothis.ui.views.typeface;

/*
 * Copyright 2013 Simple Finance Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  @author Tristan Waddington
 *
 *  Modified to use CustomTypefaceManager
 */

/**
 * Created by izzyoji :) on 4/28/15.
 */

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class CustomTypefaceSpan extends MetricAffectingSpan
{

    private Typeface mTypeface;

    public CustomTypefaceSpan(Context context)
    {
        this(context, TypefaceManager.DEFAULT);
    }

    public CustomTypefaceSpan(Context context, int typeface)
    {
        mTypeface = TypefaceManager.obtainTypeface(context, typeface);
    }

    @Override
    public void updateMeasureState(TextPaint p)
    {
        p.setTypeface(mTypeface);

        // This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp)
    {
        tp.setTypeface(mTypeface);

        // This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }


    public static SpannableString format(Context context, int textResId, int type)
    {
        return format(context, context.getString(textResId), type);
    }


    public static SpannableString format(Context context, CharSequence text)
    {
        return format(context, text, - 1);
    }

    public static SpannableString format(Context context, int textResId)
    {
        return format(context, context.getString(textResId), - 1);
    }

    public static SpannableString format(Context context, CharSequence text, int type)
    {
        if(type == - 1)
        {
            type = TypefaceManager.DEFAULT;
        }
        SpannableString spannedText = new SpannableString(text);
        spannedText.setSpan(new CustomTypefaceSpan(context, type), 0, spannedText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannedText;
    }

}