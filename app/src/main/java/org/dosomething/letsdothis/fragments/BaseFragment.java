package org.dosomething.letsdothis.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class BaseFragment extends Fragment
{
    public static BaseFragment newInstance()
    {
        return new BaseFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
