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
import org.dosomething.letsdothis.data.InterestGroup;
import org.dosomething.letsdothis.tasks.GetInterestGroupTitleTask;
import org.dosomething.letsdothis.ui.views.typeface.CustomTypefaceSpan;
import org.dosomething.letsdothis.ui.views.typeface.TypefaceManager;
import org.dosomething.letsdothis.utils.ViewUtils;

import java.util.HashMap;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class ActionsFragment extends Fragment
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String TAG = ActionsFragment.class.getSimpleName();
    public static final int INDICATOR_SPACING = 8;

    // Listener to update title on the toolbar
    private SetTitleListener titleListener;

    // Paging indicator
    private TabPageIndicator mIndicator;

    // Page titles retrieved from the server
    private HashMap<Integer, String> mTitleOverrides = new HashMap<>();

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

        EventBusExt.getDefault().register(this);
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
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        int pxFromDip = ViewUtils.getPxFromDip(getResources(), INDICATOR_SPACING).intValue();
        mIndicator.setPadding(pxFromDip, pxFromDip, pxFromDip, pxFromDip);

        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);

        FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount()
            {
                return 4;
            }

            @Override
            public Fragment getItem(int position)
            {
                return CampaignFragment.newInstance(position);
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                String title = getString(InterestGroup.values()[position].nameResId);

                int groupId = InterestGroup.values()[position].id;
                if (mTitleOverrides.containsKey(groupId)) {
                    title = mTitleOverrides.get(groupId);
                }

                return CustomTypefaceSpan.format(getActivity(), title,
                        TypefaceManager.BRANDON_BOLD);
            }
        };

        pager.setAdapter(pagerAdapter);
        mIndicator.setViewPager(pager);

        // Task to get group names from the server
        TaskQueue.loadQueueDefault(getActivity()).execute(new GetInterestGroupTitleTask(
                InterestGroup.values()[0].id,
                InterestGroup.values()[1].id,
                InterestGroup.values()[2].id,
                InterestGroup.values()[3].id
        ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusExt.getDefault().unregister(this);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetInterestGroupTitleTask task) {
        mTitleOverrides = task.mTermResults;

        mIndicator.notifyDataSetChanged();
    }

}
