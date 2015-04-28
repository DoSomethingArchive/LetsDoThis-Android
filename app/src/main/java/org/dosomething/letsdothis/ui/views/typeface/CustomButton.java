/*
* Copyright 2013 Evgeny Shishkin
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.dosomething.letsdothis.ui.views.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 4/28/15.
 */
public class CustomButton extends Button
{

    public CustomButton(Context context)
    {
        super(context);
        onInitTypeface(context, null, 0);
    }

    public CustomButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        onInitTypeface(context, attrs, 0);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        onInitTypeface(context, attrs, defStyle);
    }

    private void onInitTypeface(Context context, AttributeSet attrs, int defStyle)
    {
        // Typeface.createFromAsset doesn't work in the layout editor, so skipping.
        if(isInEditMode())
        {
            return;
        }

        int typefaceValue = 0;
        if(attrs != null)
        {
            TypedArray values = context
                    .obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyle, 0);
            typefaceValue = values
                    .getInt(R.styleable.CustomTextView_typeface, TypefaceManager.DEFAULT);
            values.recycle();
        }

        Typeface robotoTypeface = TypefaceManager.obtainTypeface(context, typefaceValue);
        setTypeface(robotoTypeface);
    }

}
