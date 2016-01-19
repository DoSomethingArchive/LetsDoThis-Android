package org.dosomething.letsdothis.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.shell.MainReactPackage;

import org.dosomething.letsdothis.BuildConfig;

/**
 * This fragment displays a feed of news items sourced from the DoSomething.org prototype News API.
 *
 * Created by juy on 1/15/16.
 */
public class NewsFragment extends Fragment {

    public static final String TAG = NewsFragment.class.getSimpleName();

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mReactRootView = new ReactRootView(getActivity());
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getActivity().getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .addPackage(new LDTReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        mReactRootView.startReactApplication(mReactInstanceManager, "LDTReactNewsApp", null);

        return mReactRootView;
    }

    private class LDTReactPackage extends MainReactPackage {

    }
}
