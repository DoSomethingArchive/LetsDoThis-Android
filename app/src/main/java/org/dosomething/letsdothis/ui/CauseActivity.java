package org.dosomething.letsdothis.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.Causes;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.tasks.GetCampaignsByCauseTask;

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

    // Intent Bundle extra keys
    private static final String EXTRA_CAUSE_NAME = "cause_name";

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

        if (!EventBusExt.getDefault().isRegistered(this)) {
            EventBusExt.getDefault().register(this);
        }

        mCauseName = getIntent().getStringExtra(EXTRA_CAUSE_NAME);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        ImageView titleBg = (ImageView) findViewById(R.id.title_bg);
        TextView titleView = (TextView) findViewById(R.id.title);
        TextView descView = (TextView) findViewById(R.id.description);

        // Extracting rgb values from the color resource so we can also add an alpha to the tint
        int color = getResources().getColor(Causes.getColorRes(mCauseName));
        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = color & 0xff;
        int alpha = 0x77;

        titleBg.setColorFilter(Color.argb(alpha, red, green, blue));
        titleView.setText(mCauseName.toUpperCase());
        descView.setText(Causes.getDescriptionRes(mCauseName));

        mAdapter = new CauseCampaignsAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        GetCampaignsByCauseTask task = new GetCampaignsByCauseTask(mCauseName);
        TaskQueue.loadQueueDefault(this).execute(task);
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
        mAdapter.setCampaigns(ResponseCampaignList.getCampaigns(response));
        mAdapter.notifyDataSetChanged();
    }

    /**
     *  Adapter for campaign data.
     */
    private class CauseCampaignsAdapter extends RecyclerView.Adapter<CauseCampaignViewHolder> {

        private ArrayList<Campaign> mCampaigns;

        @Override
        public CauseCampaignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_campaign, parent, false);
            return new CauseCampaignViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CauseCampaignViewHolder holder, int position) {
            final Campaign campaign = mCampaigns.get(position);

            // Title
            holder.mTitle.setText(campaign.title);

            // Background campaign image
            int height = getResources().getDimensionPixelSize(R.dimen.campaign_height);
            if (campaign.imagePath == null || campaign.imagePath.isEmpty()) {
                Picasso.with(CauseActivity.this).load(R.drawable.image_error).resize(0, height)
                        .into(holder.mImageView);
            } else {
                CharSequence tag = (CharSequence) holder.mImageView.getTag();
                if (! TextUtils.equals(campaign.imagePath, tag)) {
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

        @Override
        public int getItemCount() {
            return mCampaigns != null ? mCampaigns.size() : 0;
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
}
