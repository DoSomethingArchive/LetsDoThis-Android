package org.dosomething.letsdothis.ui.fragments;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;

/**
 * Created by toidiu on 4/21/15.
 */
public class IntroFragment extends Fragment
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String TAG              = IntroFragment.class.getSimpleName();
    public static final String ARG_SLANTED_LEFT = "ARG_SLANTED_LEFT";
    public static final String ARG_TITLE_RES    = "title";
    public static final String ARG_DESC_RES     = "description";
    public static final String ARG_IMAGE_RES    = "image";
    public static final String ARG_SHOW_PREV    = "ARG_SHOW_PREV";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private PagerChangeListener listener;

    public static Fragment newInstance(boolean showPrev, boolean slantedLeft, int titleTextRes, int descTextRes, int imageResource)
    {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_SHOW_PREV, showPrev);
        bundle.putBoolean(ARG_SLANTED_LEFT, slantedLeft);
        bundle.putInt(ARG_TITLE_RES, titleTextRes);
        bundle.putInt(ARG_DESC_RES, descTextRes);
        bundle.putInt(ARG_IMAGE_RES, imageResource);

        IntroFragment introFragment = new IntroFragment();
        introFragment.setArguments(bundle);

        return introFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        listener = (PagerChangeListener) getActivity();
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
        initIntroNavigation(view);

        boolean slantLeft = getArguments().getBoolean(ARG_SLANTED_LEFT);
        int titleRes = getArguments().getInt(ARG_TITLE_RES);
        int descriptionRes = getArguments().getInt(ARG_DESC_RES);
        int imageRes = getArguments().getInt(ARG_IMAGE_RES);
        boolean showPrev = getArguments().getBoolean(ARG_SHOW_PREV);

        View prev = view.findViewById(R.id.prev);
        if(! showPrev)
        {
            prev.setVisibility(View.INVISIBLE);
            prev.setClickable(false);
        }

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(descriptionRes);

        View slantedBg = view.findViewById(R.id.slanted_bg);
        Resources resources = getResources();
        int shadowColor = resources.getColor(R.color.black_10);
        int slantHeight = resources.getDimensionPixelSize(R.dimen.height_xxtiny);
        int widthOvershoot = resources.getDimensionPixelSize(R.dimen.space_50);
        int heightShadowOvershoot = resources.getDimensionPixelSize(R.dimen.padding_tiny);
        SlantedBackgroundDrawable background = new SlantedBackgroundDrawable(slantLeft,
                                                                             Color.WHITE,
                                                                             shadowColor,
                                                                             slantHeight,
                                                                             widthOvershoot,
                                                                             heightShadowOvershoot);
        slantedBg.setBackground(background);
        slantedBg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initIntroNavigation(View view)
    {
        view.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.navigatePrev();
            }
        });
        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.navigateNext();
            }
        });
    }

    public interface PagerChangeListener
    {
        void navigatePrev();

        void navigateNext();
    }

}
