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
public class Intro1Fragment extends BaseIntroFragment
{

    public static final String TAG = Intro1Fragment.class.getSimpleName();

    public static Intro1Fragment newInstance()
    {
        return new Intro1Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_intro1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initIntroNavigation(view);
    }

}
