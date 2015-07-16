package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.IntroFragment;
import org.dosomething.letsdothis.ui.fragments.RegisterLoginFragment;

/**
 * Created by toidiu on 4/15/15.
 */
public class IntroActivity extends BaseActivity implements IntroFragment.PagerChangeListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String TAG = IntroActivity.class.getSimpleName();
    public static final int INTRO_FRAGMENT_COUNT = 4;

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ViewPager           pager;
    private CirclePageIndicator indicator;
    private int                 indicatorTop;
    private ImageView           lightning;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, IntroActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

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
                        return IntroFragment.newInstance(false, true, R.string.intro_title_1,  R.string.intro_desc_1, 0);
                    case 1:
                        return IntroFragment.newInstance(true, false, R.string.intro_title_2,  R.string.intro_desc_2, 0);
                    case 2:
                        return IntroFragment.newInstance(true, true, R.string.intro_title_3,  R.string.intro_desc_3, 0);

                }
                return RegisterLoginFragment.newInstance();
            }

        });

        lightning = (ImageView) findViewById(R.id.lightning);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicatorTop = indicator.getTop();
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int measuredWidth = getWindow().getDecorView().getMeasuredWidth();
                int translateX = (- position * measuredWidth - positionOffsetPixels) / LIGHTNING_OFFSET;
                lightning.setTranslationX(translateX);

                if(position == (INTRO_FRAGMENT_COUNT - 2))
                {
                    indicator.setTranslationY(indicatorTop + positionOffsetPixels / 2);
                }
                if(BuildConfig.DEBUG)
                {
                    Log.d("--",
                          "pagerPosition " + position + " px " + positionOffsetPixels + " width " + measuredWidth + " translateX " + translateX);
                }
            }

            @Override
            public void onPageSelected(int position)
            {
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    @Override
    public void navigatePrev()
    {
        int currentItem = pager.getCurrentItem();
        if(currentItem != 0)
        {
            pager.setCurrentItem(currentItem - 1);
        }
    }

    @Override
    public void navigateNext()
    {
        int currentItem = pager.getCurrentItem();
        pager.setCurrentItem(currentItem + 1);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
