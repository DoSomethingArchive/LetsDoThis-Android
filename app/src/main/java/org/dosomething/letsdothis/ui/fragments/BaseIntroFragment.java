package org.dosomething.letsdothis.ui.fragments;
import android.support.v4.app.Fragment;
import android.view.View;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.IntroActivity;

/**
 * Created by toidiu on 4/21/15.
 */
public class BaseIntroFragment extends Fragment
{
    protected void initIntroNavigation(View view, final Fragment next)
    {
        view.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(next != null)
                {
                    ((IntroActivity) getActivity()).replaceCurrentFragment(next, null);
                }
            }
        });
    }
}
