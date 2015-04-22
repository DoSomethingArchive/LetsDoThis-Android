package org.dosomething.letsdothis.ui.fragments;
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

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class ActionsFragment extends Fragment
{

    public static final String TAG = ActionsFragment.class.getSimpleName();

    public static ActionsFragment newInstance()
    {
        return new ActionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_actions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);

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
                return CategoryFragment.newInstance();
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                return Integer.toString(position);
            }
        });

        indicator.setViewPager(pager);
    }
}
