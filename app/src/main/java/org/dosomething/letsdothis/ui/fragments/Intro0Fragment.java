package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.IntroActivity;

/**
 * Created by toidiu on 4/21/15.
 */
public class Intro0Fragment extends BaseIntroFragment
{

    public static final String TAG = Intro0Fragment.class.getSimpleName();

    public static Intro0Fragment newInstance()
    {
        return new Intro0Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_intro0, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.start).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((IntroActivity) getActivity()).replaceCurrentFragment(Intro1Fragment.newInstance(), null);

            }
        });
    }


}
