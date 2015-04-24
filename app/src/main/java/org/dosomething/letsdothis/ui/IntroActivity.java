package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.viewpagerindicator.CirclePageIndicator;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.BaseIntroFragment;
import org.dosomething.letsdothis.ui.fragments.IntroFragment;

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
    private ViewPager                      pager;
    private List<IntroFragmentExtraHolder> extraList;

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
        extraList.add(new IntroFragmentExtraHolder(true, "intro 1 text", null));
        extraList.add(new IntroFragmentExtraHolder(false, "intro 2 text", null));
        extraList.add(new IntroFragmentExtraHolder(true, "intro 3 text", null));

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
                return IntroFragment.newInstance(showPrev, extraList.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                return Integer.toString(position);
            }
        });

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
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
        if(currentItem == (extraList.size() - 1))
        {
            startActivity(RegisterLoginActivity.getLaunchIntent(this));
            finish();
        }
        else
        {
            pager.setCurrentItem(currentItem + 1);
        }
    }

    public static class IntroFragmentExtraHolder
    {
        public boolean slantedLeft;
        public String  text;
        public String  imageResource;

        IntroFragmentExtraHolder(boolean slanted, String text, String imageResource)
        {
            this.slantedLeft = slanted;
            this.text = text;

            //FIXME eventually pass in a image from assets
            this.imageResource = imageResource;
        }
    }

}
