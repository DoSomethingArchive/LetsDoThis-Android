package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class InvitesFragment extends Fragment
{
    public static final String TAG = InvitesFragment.class.getSimpleName();

    public static InvitesFragment newInstance()
    {
        return new InvitesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_invites, container, false);
    }
}