package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.RegisterLoginActivity;

/**
 * Created by toidiu on 4/21/15.
 */
public class Intro3Fragment extends BaseIntroFragment
{

    public static final String TAG = Intro3Fragment.class.getSimpleName();

    public static Intro3Fragment newInstance()
    {
        return new Intro3Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_intro3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initIntroNavigation(view);
    }


}
