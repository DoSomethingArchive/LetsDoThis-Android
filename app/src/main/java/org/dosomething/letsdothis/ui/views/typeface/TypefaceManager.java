package org.dosomething.letsdothis.ui.views.typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * Created by izzyoji :) on 4/28/15.
 */
public class TypefaceManager
{

    public final static int BRANDON_BOLD         = 0;
    public final static int BRANDON_REGULAR      = 1;
    public final static int BRANDON_MEDIUM       = 2;
    public final static int PROXIMA_NOVA_BOLD    = 3;
    public final static int PROXIMA_NOVA_REGULAR = 4;


    public final static int DEFAULT = BRANDON_REGULAR;

    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(2);

    public static Typeface obtainTypeface(Context context, int typefaceValue) throws IllegalArgumentException
    {
        Typeface typeface = mTypefaces.get(typefaceValue);
        if(typeface == null)
        {
            typeface = createTypeface(context, typefaceValue);
            mTypefaces.put(typefaceValue, typeface);
        }
        return typeface;
    }

    private static Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException
    {
        Typeface typeface;
        switch(typefaceValue)
        {
            case BRANDON_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/brandon_bold.otf");
                break;
            case BRANDON_MEDIUM:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/brandon_med.otf");
                break;
            case BRANDON_REGULAR:
            default:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/brandon_reg.otf");
                break;
            case PROXIMA_NOVA_BOLD:
                typeface = Typeface
                        .createFromAsset(context.getAssets(), "fonts/proxima_nova_bold.otf");
                break;
            case PROXIMA_NOVA_REGULAR:
                typeface = Typeface
                        .createFromAsset(context.getAssets(), "fonts/proxima_nova_reg.otf");
                break;
        }
        return typeface;
    }

}
