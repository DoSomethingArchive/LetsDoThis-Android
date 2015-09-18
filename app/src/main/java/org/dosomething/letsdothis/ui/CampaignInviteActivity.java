package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Invite;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;
import org.dosomething.letsdothis.utils.Hashery;

/**
 * Created by izzyoji :) on 5/13/15.
 */
public class CampaignInviteActivity extends BaseActivity
{

    public static final String EXTRA_CAMPAIGN_NAME = "campaign_name";
    public static final String EXTRA_SIGNUP_GROUP  = "signup_group";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_campaign_invite);

        final String campaignName = getIntent().getStringExtra(EXTRA_CAMPAIGN_NAME);
        final int inviteCode = getIntent().getIntExtra(EXTRA_SIGNUP_GROUP, - 1);

        initUI(campaignName, inviteCode);
    }

    private void initUI(final String campaignName, final int groupId)
    {
        CustomToolbar toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(campaignName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_campaign_invite, campaignName));

        TextView details = (TextView) findViewById(R.id.details);
        details.setText(getString(R.string.desc_campaign_invite, campaignName));

        TextView code = (TextView) findViewById(R.id.code);
        final String hashedCode = Hashery.getInstance(this).encode(groupId);
        code.setText(hashedCode);

        findViewById(R.id.invite).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(Invite.buildShareIntent(getResources(), campaignName, hashedCode));
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

    public static Intent getLaunchIntent(Context context, String campaignName, int signupGroup)
    {
        return new Intent(context, CampaignInviteActivity.class)
                .putExtra(EXTRA_CAMPAIGN_NAME, campaignName)
                .putExtra(EXTRA_SIGNUP_GROUP, signupGroup);
    }
}
