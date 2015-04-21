package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;

/**
 * Created by toidiu on 4/21/15.
 */
public class Intro2Fragment extends BaseIntroFragment
{

    public static final String TAG = Intro2Fragment.class.getSimpleName();

    public static Intro2Fragment newInstance()
    {
        return new Intro2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_intro2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initIntroNavigation(view, Intro3Fragment.newInstance());
    }


}
