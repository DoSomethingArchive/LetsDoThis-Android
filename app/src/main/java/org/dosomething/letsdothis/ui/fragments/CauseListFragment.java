package org.dosomething.letsdothis.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Causes;
import org.dosomething.letsdothis.ui.CauseActivity;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * Fragment displaying a list of causes.
 *
 * Created by juy on 1/26/16.
 */
public class CauseListFragment extends Fragment {

    public static final String TAG = CauseListFragment.class.getSimpleName();

    // Google Analytics tracker
    private Tracker mTracker;

    public static CauseListFragment newInstance() {
        return new CauseListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LDTApplication application = (LDTApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setAdapter(new CauseListAdapter());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Submit screen view to Google Analytics
        AnalyticsUtils.sendScreen(mTracker, AnalyticsUtils.SCREEN_CAUSE_LIST);
    }

    /**
     * Adapter for RecyclerView.
     */
    private class CauseListAdapter extends RecyclerView.Adapter<CauseListItemViewHolder> {

        @Override
        public CauseListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_cause_list, parent, false);
            return new CauseListItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CauseListItemViewHolder holder, int position) {
            final String name = Causes.LIST_ORDER[position];
            final int resColor = Causes.getColorRes(name);

            holder.name.setText(name);
            holder.color.setBackgroundResource(resColor);
            holder.setCauseName(name);
        }

        @Override
        public int getItemCount() {
            return Causes.LIST_ORDER.length;
        }
    }

    /**
     * ViewHolder for each row in the cause list.
     */
    private class CauseListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        View color;
        View rootView;

        private String causeName;

        CauseListItemViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            color = itemView.findViewById(R.id.color);
            rootView = itemView;
            rootView.setOnClickListener(this);
        }

        public void setCauseName(String name) {
            causeName = name;
        }

        @Override
        public void onClick(View v) {
            Intent i = CauseActivity.getLaunchIntent(getActivity(), causeName);
            startActivity(i);
        }
    }
}
