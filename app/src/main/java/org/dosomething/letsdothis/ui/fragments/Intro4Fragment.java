package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.IntroActivity;
import org.dosomething.letsdothis.ui.LoginActivity;
import org.dosomething.letsdothis.ui.SignupActivity;

/**
 * Created by toidiu on 4/15/15.
 */
public class Intro4Fragment extends BaseIntroFragment
{

    public static final String TAG = Intro4Fragment.class.getSimpleName();

    public static Intro4Fragment newInstance()
    {
        return new Intro4Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_intro4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initAppNavigation(view);
    }


    private void initAppNavigation(View view)
    {
                view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        startActivity(LoginActivity.getLaunchIntent(getActivity()));
                        getActivity().finish();
                    }
                });
                view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        startActivity(SignupActivity.getLaunchIntent(getActivity()));
                        getActivity().finish();
                    }
                });
    }

}
