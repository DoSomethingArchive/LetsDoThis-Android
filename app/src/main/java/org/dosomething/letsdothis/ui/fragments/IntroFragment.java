package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dosomething.letsdothis.R;

/**
 * Created by toidiu on 4/21/15.
 */
public class IntroFragment extends Fragment
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String TAG              = IntroFragment.class.getSimpleName();
    public static final String ARG_TITLE_RES    = "title";
    public static final String ARG_DESC_RES     = "description";

    public static Fragment newInstance(int titleTextRes, int descTextRes)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TITLE_RES, titleTextRes);
        bundle.putInt(ARG_DESC_RES, descTextRes);

        IntroFragment introFragment = new IntroFragment();
        introFragment.setArguments(bundle);

        return introFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        int titleRes = getArguments().getInt(ARG_TITLE_RES);
        int descriptionRes = getArguments().getInt(ARG_DESC_RES);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(titleRes);
        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(descriptionRes);
    }

}
