package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public abstract class AbstractQuickReturnFragment extends Fragment
{

    public static final String TAG = AbstractQuickReturnFragment.class.getSimpleName();
    //~=~=~=~=~=~=~=~=~=~=~=~=QuickReturn
    protected Toolbar             toolbar;
    protected RecyclerView        recycleView;
    protected LinearLayoutManager layoutManager;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setScrollListener();
    }

    protected void setScrollListener()
    {
        recycleView.setOnScrollListener(getScrollListener());
    }

    protected RecyclerView.OnScrollListener getScrollListener()
    {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener()
        {
            private int toolbarHeight;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if(layoutManager.findFirstVisibleItemPosition() == 0)
                {
                    return;
                }
                if(newState != RecyclerView.SCROLL_STATE_IDLE)
                {
                    return;
                }

                float translate = toolbar.getTranslationY();
                if(toolbarHeight <= - translate)
                {
                    //hide
                    toolbar.setTranslationY(- toolbarHeight);
                }
                else if(translate >= 0)
                {
                    //show
                    toolbar.setTranslationY(0);
                }

                int half = toolbarHeight / 2;
                if(half > - translate)
                {
                    //show
                    toolbar.animate().setDuration(200).translationY(0);
                }
                else
                {
                    //hide
                    toolbar.animate().setDuration(200).translationY(- toolbarHeight);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                float toolbarTranslate = toolbar.getTranslationY();
                toolbarHeight = toolbar.getHeight();
                boolean hideScroll = dy > 0;

                if(hideScroll)
                {
                    if(toolbarHeight <= - toolbarTranslate)
                    {
                        //hide
                        toolbar.setTranslationY(- toolbarHeight);
                        return;
                    }
                    toolbar.setTranslationY(toolbarTranslate - dy);
                }
                else
                {
                    if(0 <= toolbarTranslate)
                    {
                        //show
                        toolbar.setTranslationY(0);
                        return;
                    }
                    toolbar.setTranslationY(toolbarTranslate - dy);
                }
            }
        };
        return onScrollListener;
    }


}
