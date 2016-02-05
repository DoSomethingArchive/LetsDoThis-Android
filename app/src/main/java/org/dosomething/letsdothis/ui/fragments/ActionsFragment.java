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
import android.widget.Button;

import com.google.android.gms.analytics.Tracker;
import com.viewpagerindicator.TabPageIndicator;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.InterestGroup;
import org.dosomething.letsdothis.tasks.GetInterestGroupTitleTask;
import org.dosomething.letsdothis.ui.views.typeface.CustomTypefaceSpan;
import org.dosomething.letsdothis.ui.views.typeface.TypefaceManager;
import org.dosomething.letsdothis.utils.AnalyticsUtils;
import org.dosomething.letsdothis.utils.ViewUtils;

import java.util.HashMap;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.utils.NetworkUtils;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class ActionsFragment extends Fragment
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String TAG = ActionsFragment.class.getSimpleName();
    public static final int INDICATOR_SPACING = 8;

    // Listener to update title on the toolbar
    private SetTitleListener mTitleListener;

    // Paging indicator
    private TabPageIndicator mIndicator;

    // Page titles retrieved from the server
    private HashMap<Integer, String> mTitleOverrides = new HashMap<>();

    // Pager
    private ViewPager mPager;

    // No network view
    private View mNoNetworkView;

    // Retry button
    private Button mRetryButton;

    // Google Analytics tracker
    private Tracker mTracker;

    public static ActionsFragment newInstance()
    {
        return new ActionsFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mTitleListener = (SetTitleListener) getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!EventBusExt.getDefault().isRegistered(this)) {
            EventBusExt.getDefault().register(this);
        }

        mTitleListener.setTitle(getResources().getString(R.string.actions));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LDTApplication application = (LDTApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
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
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mNoNetworkView = view.findViewById(R.id.no_network_view);
        mRetryButton = (Button) view.findViewById(R.id.retry);

        int pxFromDip = ViewUtils.getPxFromDip(getResources(), INDICATOR_SPACING).intValue();
        mIndicator.setPadding(pxFromDip, pxFromDip, pxFromDip, pxFromDip);

        mPager.setOffscreenPageLimit(3);

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

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                // Submit screen view to Google Analytics
                sendScreenViewToAnalytics(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        };

        // Log initial screen into analytics
        sendScreenViewToAnalytics(0);

        mPager.setAdapter(pagerAdapter);
        mIndicator.setViewPager(mPager);
        mIndicator.setOnPageChangeListener(pageChangeListener);

        if (NetworkUtils.isOnline(getActivity())) {
            fetchGroupNames();
        }
        else {
            // Show the no-network view and hide the rest if the device isn't online
            mNoNetworkView.setVisibility(View.VISIBLE);
            mIndicator.setVisibility(View.GONE);
            mPager.setVisibility(View.GONE);
        }

        // Retry button listener
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchGroupNames();
            }
        });
    }

    /**
     * Send screen view to analytics.
     *
     * @param position int Pager position
     */
    protected void sendScreenViewToAnalytics(int position) {
        if (mTracker == null) {
            return;
        }

        String screenName = String.format(AnalyticsUtils.SCREEN_INTEREST_GROUP, InterestGroup.values()[position].id);
        AnalyticsUtils.sendScreen(mTracker, screenName);
    }

    /**
     * Execute task to get the interest group names from the server.
     */
    private void fetchGroupNames() {
        // Task to get group names from the server
        TaskQueue.loadQueueDefault(getActivity()).execute(new GetInterestGroupTitleTask(
                InterestGroup.values()[0].id,
                InterestGroup.values()[1].id,
                InterestGroup.values()[2].id,
                InterestGroup.values()[3].id
        ));
    }

    /**
     * Handle completion of the GetInterestGroupTitleTask.
     *
     * @param task
     */
    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetInterestGroupTitleTask task) {
        if (!task.mTermResults.isEmpty()) {
            mTitleOverrides = task.mTermResults;

            mIndicator.notifyDataSetChanged();
        }

        if (NetworkUtils.isOnline(getActivity())) {
            mNoNetworkView.setVisibility(View.GONE);
            mIndicator.setVisibility(View.VISIBLE);
            mPager.setVisibility(View.VISIBLE);
        }
    }

}
