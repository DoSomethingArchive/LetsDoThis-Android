package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.viewpagerindicator.TabPageIndicator;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.BaseIntroFragment;
import org.dosomething.letsdothis.ui.fragments.Intro1Fragment;
import org.dosomething.letsdothis.ui.fragments.Intro2Fragment;
import org.dosomething.letsdothis.ui.fragments.Intro3Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toidiu on 4/15/15.
 */
public class IntroActivity extends ActionBarActivity implements BaseIntroFragment.PagerChangeListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String TAG = IntroActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ViewPager pager;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private List<Fragment> fragmentList;

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
        fragmentList = new ArrayList<>();
        fragmentList.add(Intro1Fragment.newInstance());
        fragmentList.add(Intro2Fragment.newInstance());
        fragmentList.add(Intro3Fragment.newInstance());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return fragmentList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                return Integer.toString(position);
            }
        });

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
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
        if(currentItem == (fragmentList.size() - 1))
        {
            startActivity(RegisterLoginActivity.getLaunchIntent(this));
            finish();
        }
        else
        {
            pager.setCurrentItem(currentItem + 1);
        }
    }
}
