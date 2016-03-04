package org.dosomething.letsdothis.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.Causes;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.tasks.GetCampaignsByCauseTask;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Activity displaying campaigns for a single cause.
 *
 * Created by juy on 1/27/16.
 */
public class CauseActivity extends BaseActivity {

    // View types
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_CAMPAIGN = 1;

    // Intent Bundle extra keys
    private static final String EXTRA_CAUSE_NAME = "cause_name";

    // Google Analytics tracker
    private Tracker mTracker;

    // Name of the cause to display campaigns for
    private String mCauseName;

    // Progress bar view
    private ProgressBar mProgressBar;

    // RecyclerView
    private RecyclerView mRecyclerView;

    // Adapter
    private CauseCampaignsAdapter mAdapter;

    public static Intent getLaunchIntent(Context context, String causeName) {
        return new Intent(context, CauseActivity.class)
                .putExtra(EXTRA_CAUSE_NAME, causeName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cause);

        LDTApplication application = (LDTApplication) getApplication();
        mTracker = application.getDefaultTracker();

        if (!EventBusExt.getDefault().isRegistered(this)) {
            EventBusExt.getDefault().register(this);
        }

        mCauseName = getIntent().getStringExtra(EXTRA_CAUSE_NAME);

        CustomToolbar toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name).toUpperCase());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        mAdapter = new CauseCampaignsAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        GetCampaignsByCauseTask task = new GetCampaignsByCauseTask(mCauseName);
        TaskQueue.loadQueueDefault(this).execute(task);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Submit screen view to Google Analytics
        String screenName = String.format(AnalyticsUtils.SCREEN_CAUSE, mCauseName);
        AnalyticsUtils.sendScreen(mTracker, screenName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetCampaignsByCauseTask task) {
        mProgressBar.setVisibility(View.GONE);

        ResponseCampaignList response = task.getResults();
        if (response != null) {
            mAdapter.setCampaigns(response.getCampaigns(true));
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     *  Adapter for campaign data.
     */
    private class CauseCampaignsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Campaign> mCampaigns;

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEW_TYPE_HEADER;
            } else {
                return VIEW_TYPE_CAMPAIGN;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_cause_header, parent, false);
                return new HeaderViewHolder(v);

            } else if (viewType == VIEW_TYPE_CAMPAIGN) {
                View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_campaign, parent, false);
                return new CauseCampaignViewHolder(v);
            } else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder.getItemViewType() == VIEW_TYPE_HEADER) {
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;

                int color = getResources().getColor(Causes.getColorRes(mCauseName));
                int red = (color >> 16) & 0xff;
                int green = (color >> 8) & 0xff;
                int blue = color & 0xff;
                int alpha = 0xff;

                holder.mBackground.setBackgroundColor(Color.argb(alpha, red, green, blue));
                holder.mTitle.setText(mCauseName.toUpperCase());
                holder.mDescription.setText(Causes.getDescriptionRes(mCauseName));

            } else if (viewHolder.getItemViewType() == VIEW_TYPE_CAMPAIGN) {
                CauseCampaignViewHolder holder = (CauseCampaignViewHolder) viewHolder;

                // -1 to account for the header being in the 1st position
                final Campaign campaign = mCampaigns.get(position - 1);

                // Title
                holder.mTitle.setText(campaign.title);

                // Background campaign image
                int height = getResources().getDimensionPixelSize(R.dimen.campaign_height);
                if (campaign.imagePath == null || campaign.imagePath.isEmpty()) {
                    Picasso.with(CauseActivity.this).load(R.drawable.image_error).resize(0, height)
                            .into(holder.mImageView);
                } else {
                    CharSequence tag = (CharSequence) holder.mImageView.getTag();
                    if (!TextUtils.equals(campaign.imagePath, tag)) {
                        Picasso.with(CauseActivity.this)
                                .load(campaign.imagePath)
                                .placeholder(R.drawable.image_loading)
                                .resize(0, height)
                                .into(holder.mImageView);
                        holder.mImageView.setTag(campaign.imagePath);
                    }
                }

                // Signed-up indicator
                if (campaign.userIsSignedUp) {
                    holder.mSignedupIndicator.setVisibility(View.VISIBLE);
                } else {
                    holder.mSignedupIndicator.setVisibility(View.GONE);
                }

                // Setting campaign ID to use in click listener
                holder.setCampaignId(campaign.id);
            }
        }

        @Override
        public int getItemCount() {
            int numCampaigns = mCampaigns != null ? mCampaigns.size() : 0;

            // Adding the +1 to account for the header row
            return numCampaigns + 1;
        }

        public void setCampaigns(List<Campaign> campaigns) {
            mCampaigns = new ArrayList<>();
            mCampaigns.addAll(campaigns);
        }
    }

    /**
     * Campaign row ViewHolder.
     */
    private class CauseCampaignViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected View mRoot;
        protected ImageView mImageView;
        protected TextView mTitle;
        protected View mSignedupIndicator;
        private int mCampaignId;

        public CauseCampaignViewHolder(View view) {
            super(view);

            this.mRoot = view;
            this.mImageView = (ImageView) view.findViewById(R.id.image);
            this.mTitle = (TextView) view.findViewById(R.id.title);
            this.mSignedupIndicator = view.findViewById(R.id.signedup_indicator);

            this.mRoot.setOnClickListener(this);
        }

        private void setCampaignId(int id) {
            mCampaignId = id;
        }

        @Override
        public void onClick(View view) {
            startActivity(CampaignDetailsActivity.getLaunchIntent(CauseActivity.this, mCampaignId));
        }
    }

    /**
     * Header row ViewHolder.
     */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mBackground;
        protected TextView mTitle;
        protected TextView mDescription;

        public HeaderViewHolder(View view) {
            super(view);

            this.mBackground = (ImageView) view.findViewById(R.id.title_bg);
            this.mTitle = (TextView) view.findViewById(R.id.title);
            this.mDescription = (TextView) view.findViewById(R.id.description);
        }
    }
}
