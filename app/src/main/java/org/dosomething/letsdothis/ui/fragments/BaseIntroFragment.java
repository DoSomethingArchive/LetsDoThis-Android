package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import org.dosomething.letsdothis.R;

/**
 * Created by toidiu on 4/21/15.
 */
public class BaseIntroFragment extends Fragment
{

    private PagerChangeListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        listener = (PagerChangeListener) getActivity();
    }

    protected void initIntroNavigation(View view)
    {
        view.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.navigatePrev();
            }
        });
        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                    listener.navigateNext();
            }
        });
    }

    public interface PagerChangeListener
    {
        void navigatePrev();

        void navigateNext();
    }
}
