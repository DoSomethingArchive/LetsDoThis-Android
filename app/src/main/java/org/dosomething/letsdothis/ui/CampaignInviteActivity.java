package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Invite;

/**
 * Created by izzyoji :) on 5/13/15.
 */
public class CampaignInviteActivity extends BaseActivity
{

    public static final String EXTRA_CAMPAIGN_NAME = "campaign_name";
    public static final String EXTRA_INVITE_CODE   = "invite_code";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_campaign_invite);

        final String campaignName = getIntent().getStringExtra(EXTRA_CAMPAIGN_NAME);
        final String inviteCode = getIntent().getStringExtra(EXTRA_INVITE_CODE);

        initUI(campaignName, inviteCode);
    }

    private void initUI(final String campaignName, final String inviteCode)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("R.string.app_name");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_campaign_invite, campaignName));

        TextView details = (TextView) findViewById(R.id.details);
        details.setText(getString(R.string.desc_campaign_invite, campaignName));

        TextView code = (TextView) findViewById(R.id.code);
        code.setText(inviteCode);

        findViewById(R.id.invite).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(Invite.buildShareIntent(getResources(), campaignName, inviteCode));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent getLaunchIntent(Context context, String campaignName, String inviteCode)
    {
        return new Intent(context, CampaignInviteActivity.class)
                .putExtra(EXTRA_CAMPAIGN_NAME, campaignName)
                .putExtra(EXTRA_INVITE_CODE, inviteCode);
    }
}
