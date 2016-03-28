package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.viewpagerindicator.CirclePageIndicator;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.IntroFragment;
import org.dosomething.letsdothis.ui.fragments.RegisterLoginFragment;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * Activity for displaying intro information to first time users.
 *
 * Created by toidiu on 4/15/15.
 */
public class IntroActivity extends BaseActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String TAG = IntroActivity.class.getSimpleName();
    public static final int INTRO_FRAGMENT_COUNT = 3;

    // Screen names to use for analytics tracking. Array index corresponds to pager position.
    private final String[] TRACKER_SCREEN_TAGS = {
            AnalyticsUtils.SCREEN_ONBOARDING_1,
            AnalyticsUtils.SCREEN_ONBOARDING_2,
            AnalyticsUtils.SCREEN_USER_CONNECT
    };

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ViewPager           pager;
    private CirclePageIndicator indicator;
    private int                 indicatorTop;
    private ImageView           background;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields

    // Google Analytics tracker
    private Tracker mTracker;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, IntroActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mTracker = ((LDTApplication)getApplication()).getDefaultTracker();
        initPager();
    }

    private void initPager()
    {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return INTRO_FRAGMENT_COUNT;
            }

            @Override
            public Fragment getItem(int position)
            {
                switch(position)
                {
                    case 0:
                        return IntroFragment.newInstance(
                                R.string.intro_title_1,
                                R.string.intro_desc_1,
                                R.drawable.onboarding_phone1
                        );
                    case 1:
                        return IntroFragment.newInstance(
                                R.string.intro_title_2,
                                R.string.intro_desc_2,
                                R.drawable.onboarding_phone2
                        );

                }
                return RegisterLoginFragment.newInstance();
            }

        });

        background = (ImageView) findViewById(R.id.background);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicatorTop = indicator.getTop();
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int measuredWidth = getWindow().getDecorView().getMeasuredWidth();
                int translateX = (-position * measuredWidth - positionOffsetPixels) / LIGHTNING_OFFSET;
                // @todo Temporarily disabling parallax until we get finalized assets
                // background.setTranslationX(translateX);

                if (position == (INTRO_FRAGMENT_COUNT - 2)) {
                    indicator.setTranslationY(indicatorTop + positionOffsetPixels / 2);
                }
                if (BuildConfig.DEBUG) {
                    Log.d("--", "pagerPosition " + position + " px " + positionOffsetPixels + " width " + measuredWidth + " translateX " + translateX);
                }
            }

            @Override
            public void onPageSelected(int position) {
                sendScreenViewToAnalytics(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Send initial screen view
        sendScreenViewToAnalytics(0);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * Send screen view to analytics.
     *
     * @param position int Pager position
     */
    protected void sendScreenViewToAnalytics(int position) {
        if (position >= TRACKER_SCREEN_TAGS.length) {
            return;
        }

        mTracker.setScreenName(TRACKER_SCREEN_TAGS[position]);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
