package org.dosomething.letsdothis.ui.views;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Kudo;

/**
 * Created by izzyoji :) on 6/30/15.
 */
public class KudosView extends LinearLayout
{
    private TextView  count;
    private ImageView image;
    private int       countNum;
    private Kudo      kudos;

    public KudosView(Context context)
    {
        super(context);
    }

    public KudosView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if(! isInEditMode())
        {
            init(context, attrs);
        }
    }

    public KudosView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        if(! isInEditMode())
        {
            init(context, attrs);
        }
    }

    public int getCountNum()
    {
        return countNum;
    }

    public void setCountNum(int countNum)
    {
        this.countNum = countNum;
        if(countNum > 0)
        {
            if(countNum > 99)
            {
                count.setText(R.string.max_kudos);
            }
            else
            {
                count.setText(Integer.toString(countNum));
            }
            count.setVisibility(VISIBLE);
        }
        else
        {
            count.setVisibility(INVISIBLE);
        }
    }

    public Kudo getKudos()
    {
        return kudos;
    }

    public void setKudos(int kudosOrdinal)
    {
        this.kudos = Kudo.values()[kudosOrdinal];
        image.setImageResource(kudos.imageResId);
    }

    private void init(Context context, AttributeSet attrs)
    {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KudosView, 0, 0);

        countNum = a.getInt(R.styleable.KudosView_count, 0);
        int kudosOrdinal = a.getInt(R.styleable.KudosView_kudos, - 1);
        if(kudosOrdinal != -1)
        {
            kudos = Kudo.values()[kudosOrdinal];
        }
        boolean selected = a.getBoolean(R.styleable.KudosView_selected, false);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_kudos, this, true);
        setBackgroundResource(R.drawable.bg_kudos);

        count = (TextView) getChildAt(1);
        setCountNum(countNum);
        setSelected(selected);

        image = (ImageView) getChildAt(0);
        if(kudos != null)
        {
            image.setImageResource(kudos.imageResId);
        }

    }

    public ImageView getImage()
    {
        return image;
    }

    public void setKudos(Kudo kudo)
    {
        this.kudos = kudo;
        image.setImageResource(kudos.imageResId);
        setSelected(kudo.selected);
    }
}
