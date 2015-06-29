package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.NotificationsFragment.SetTitleListener;
import org.dosomething.letsdothis.data.InterestGroup;
import org.dosomething.letsdothis.ui.views.typeface.CustomTypefaceSpan;
import org.dosomething.letsdothis.ui.views.typeface.TypefaceManager;
import org.dosomething.letsdothis.utils.ViewUtils;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class ActionsFragment extends Fragment
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String TAG = ActionsFragment.class.getSimpleName();
    public static final int INDICATOR_SPACING = 8;
    private SetTitleListener titleListener;

    public static ActionsFragment newInstance()
    {
        return new ActionsFragment();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        titleListener = (SetTitleListener) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        titleListener.setTitle("Actions");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_actions, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        int pxFromDip = ViewUtils.getPxFromDip(getResources(), INDICATOR_SPACING).intValue();
        indicator.setPadding(pxFromDip, pxFromDip, pxFromDip, pxFromDip);

        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager())
        {
            @Override
            public int getCount()
            {
                return 4;
            }

            @Override
            public Fragment getItem(int position)
            {
                return CategoryFragment.newInstance(position);
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                return CustomTypefaceSpan.format(getActivity(), getString(InterestGroup.values()[position].nameResId),
                                                 TypefaceManager.BRANDON_BOLD);
            }
        });

        indicator.setViewPager(pager);
    }
}
