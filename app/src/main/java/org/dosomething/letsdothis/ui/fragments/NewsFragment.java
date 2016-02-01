package org.dosomething.letsdothis.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.react.CampaignNavigationModule;
import org.dosomething.letsdothis.react.ConfigModule;
import org.dosomething.letsdothis.react.ShareIntentModule;
import org.dosomething.letsdothis.react.WebViewModule;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment displays a feed of news items sourced from the DoSomething.org prototype News API.
 *
 * Created by juy on 1/15/16.
 */
public class NewsFragment extends Fragment implements DefaultHardwareBackBtnHandler {

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
        mReactRootView.startReactApplication(mReactInstanceManager, "NewsFeedView", null);

        return mReactRootView;
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        getActivity().onBackPressed();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onResume(getActivity(), this);
        }
    }


    /**
     * Capture the menu button key-up event to show the React Native dev options dialog.
     *
     * @param keyCode
     * @return boolean
     */
    public boolean onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Custom ReactPackage to add custom DS Native Modules.
     */
    private class LDTReactPackage extends MainReactPackage {
        @Override
        public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
            List<NativeModule> modules = new ArrayList<>();

            modules.addAll(super.createNativeModules(reactContext));
            modules.add(new CampaignNavigationModule(reactContext));
            modules.add(new ConfigModule(reactContext));
            modules.add(new ShareIntentModule(reactContext));
            modules.add(new WebViewModule(reactContext));

            return modules;
        }
    }
}
