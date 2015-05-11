package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.CirclePageIndicator;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.IntroFragment;
import org.dosomething.letsdothis.ui.fragments.RegisterLoginFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toidiu on 4/15/15.
 */
public class IntroActivity extends BaseActivity implements IntroFragment.PagerChangeListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String TAG = IntroActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ViewPager                               pager;
    private List<IntroFragment.FragmentExtraHolder> extraList;
    private CirclePageIndicator                     indicator;
    private int                                     indicatorTop;

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
        extraList = new ArrayList<>();
        extraList.add(new IntroFragment.FragmentExtraHolder(true, "intro 1 text", null));
        extraList.add(new IntroFragment.FragmentExtraHolder(false, "intro 2 text", null));
        extraList.add(new IntroFragment.FragmentExtraHolder(true, "intro 3 text", null));
        extraList.add(new IntroFragment.FragmentExtraHolder(true, null, null));

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return extraList.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                boolean showPrev = true;
                if(position == 0)
                {
                    showPrev = false;
                }

                if(extraList.get(position).text == null)
                {
                    return RegisterLoginFragment.newInstance();
                }
                return IntroFragment.newInstance(showPrev, extraList.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                return Integer.toString(position);
            }
        });

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicatorTop = indicator.getTop();
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                if(position == (extraList.size() - 2))
                {
                    indicator.setTranslationY(indicatorTop + positionOffsetPixels / 2);
                }
                if(BuildConfig.DEBUG)
                {
                    Log.d("--",
                          "position " + position + "offset " + positionOffset + " px " + position * positionOffsetPixels);
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

}
