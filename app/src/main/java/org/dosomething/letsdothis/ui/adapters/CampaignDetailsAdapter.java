package org.dosomething.letsdothis.ui.adapters;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class CampaignDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int VIEW_TYPE_CAMPAIGN        = 0;
    public static final int VIEW_TYPE_CAMPAIGN_FOOTER = 1;
    public static final int VIEW_TYPE_REPORT_BACK     = 2;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> dataSet = new ArrayList<>();
    private DetailsAdapterClickListener detailsAdapterClickListener;
    private Campaign                    currentCampaign;
    private Uri selectedImageUri;

    public CampaignDetailsAdapter(DetailsAdapterClickListener detailsAdapterClickListener)
    {
        super();
        this.detailsAdapterClickListener = detailsAdapterClickListener;
    }

    public void updateCampaign(Campaign campaign)
    {
        if(currentCampaign == null)
        {
            currentCampaign = campaign;
            dataSet.add(campaign);
            dataSet.add("footer item_placeholder");
            notifyItemInserted(0);
        }
        else
        {
            currentCampaign = campaign;
            dataSet.set(0, currentCampaign);
            notifyItemChanged(0);
        }
    }

    public void addAll(List<ReportBack> reportBacks)
    {
        dataSet.addAll(reportBacks);
        notifyItemRangeInserted(dataSet.size() - reportBacks.size(), dataSet.size() - 1);
    }

    public void refreshTestImage(Uri selectedImageUri)
    {
        this.selectedImageUri = selectedImageUri;
        notifyItemChanged(0);
    }

    public interface DetailsAdapterClickListener
    {
        void onScrolledToBottom();

        void proveShareClicked();

        void inviteClicked();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_CAMPAIGN_FOOTER:
                View footerLayout = LayoutInflater.from(parent.getContext())
                                                  .inflate(R.layout.item_campaign_footer, parent,
                                                           false);
                return new SectionTitleViewHolder((TextView) footerLayout);
            case VIEW_TYPE_REPORT_BACK:
                View reportBackLayout = LayoutInflater.from(parent.getContext())
                                                      .inflate(R.layout.item_report_back_expanded,
                                                               parent, false);
                return new ReportBackViewHolder(reportBackLayout);
            default:
                View campaignLayout = LayoutInflater.from(parent.getContext())
                                                    .inflate(R.layout.item_campaign_details, parent,
                                                             false);
                return new CampaignViewHolder(campaignLayout);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if(dataSet.size() >= 24 && position == dataSet.size() - 3)
        {
            detailsAdapterClickListener.onScrolledToBottom();
        }

        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;

            int height = campaignViewHolder.imageView.getContext().getResources().getDimensionPixelSize(
                    R.dimen.campaign_height_expanded);
            Picasso.with(campaignViewHolder.imageView.getContext())
                   .load(campaign.imagePath)
                   .resize(0, height)
                   .into(campaignViewHolder.imageView);
            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);
            campaignViewHolder.problemFact.setText(campaign.problemFact);
            if(BuildConfig.DEBUG && campaign.solutionCopy != null) //FIXME this is null sometime
            {
                String cleanText = campaign.solutionCopy.replace("\n", "");
                campaignViewHolder.solutionCopy.setText(Html.fromHtml(cleanText));
            }
            else
            {
                campaignViewHolder.solutionCopy.setVisibility(View.GONE);
            }
            if(BuildConfig.DEBUG && campaign.solutionSupport != null) //FIXME this is null sometime
            {
                //FIXME also this is a problem. might need to filter the text as soon as we get in from the response.
                Spanned spanned = Html.fromHtml(campaign.solutionSupport);
                String cleanText = spanned.toString().replace("\n", "");
                campaignViewHolder.solutionSupport.setText(cleanText);
            }
            else
            {
                campaignViewHolder.solutionCopy.setVisibility(View.GONE);
            }

            int webOrange = campaignViewHolder.solutionWrapper.getContext().getResources()
                                                          .getColor(R.color.web_orange);
            SlantedBackgroundDrawable background = new SlantedBackgroundDrawable(true, webOrange);
            campaignViewHolder.solutionWrapper.setBackground(background);

            campaignViewHolder.proveShare.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    detailsAdapterClickListener.proveShareClicked();
                }
            });

            campaignViewHolder.invite.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    detailsAdapterClickListener.inviteClicked();
                }
            });

            //FIXME this is for testing
            if(selectedImageUri != null)
            {
                Picasso.with(campaignViewHolder.debugImage.getContext()).load(selectedImageUri)
                       .resize(0, height).into(campaignViewHolder.debugImage);
            }


        }
        else if(getItemViewType(position) == VIEW_TYPE_REPORT_BACK)
        {
            final ReportBack reportBack = (ReportBack) dataSet.get(position);
            ReportBackViewHolder reportBackViewHolder = (ReportBackViewHolder) holder;

            Picasso.with(reportBackViewHolder.imageView.getContext()).
                    load(reportBack.getImagePath()).into(reportBackViewHolder.imageView);

            reportBackViewHolder.name.setText(reportBack.user.id);
            reportBackViewHolder.timestamp.setText(TimeUtils.getTimeSince(
                    reportBackViewHolder.timestamp.getContext(), reportBack.createdAt * 1000));
            reportBackViewHolder.caption.setText(reportBack.caption);
        }
        else if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_FOOTER)
        {
            SectionTitleViewHolder sectionTitleViewHolder = (SectionTitleViewHolder) holder;
            sectionTitleViewHolder.textView.setText(sectionTitleViewHolder.textView.getContext()
                                                                                   .getString(
                                                                                           R.string.people_doing_it));
        }

    }

    @Override
    public int getItemViewType(int position)
    {
        Object currentObject = dataSet.get(position);
        if(currentObject instanceof Campaign)
        {
            return VIEW_TYPE_CAMPAIGN;

        }
        else if(currentObject instanceof ReportBack)
        {
            return VIEW_TYPE_REPORT_BACK;
        }
        else
        {
            return VIEW_TYPE_CAMPAIGN_FOOTER;
        }
    }


    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    private class CampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView  solutionSupport;
        protected TextView  solutionCopy;
        protected TextView  problemFact;
        protected ImageView imageView;
        protected ImageView debugImage;
        protected TextView  title;
        protected TextView  callToAction;
        protected Button    proveShare;
        protected Button    invite;
        public    View      solutionWrapper;

        public CampaignViewHolder(View itemView)
        {
            super(itemView);
            this.solutionWrapper = itemView.findViewById(R.id.solutionWrapper);
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            this.debugImage = (ImageView) itemView.findViewById(R.id.test);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
            this.problemFact = (TextView) itemView.findViewById(R.id.problemFact);
            this.solutionCopy = (TextView) itemView.findViewById(R.id.solutionCopy);
            this.solutionSupport = (TextView) itemView.findViewById(R.id.solutionSupport);
            this.proveShare = (Button) itemView.findViewById(R.id.prove_share);
            this.invite = (Button) itemView.findViewById(R.id.invite);
        }
    }

    private class ReportBackViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView avatar;
        protected ImageView imageView;
        protected TextView  name;
        protected TextView  timestamp;
        protected TextView  caption;

        public ReportBackViewHolder(View view)
        {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            this.avatar = (ImageView) view.findViewById(R.id.avatar);
            this.name = (TextView) view.findViewById(R.id.name);
            this.timestamp = (TextView) view.findViewById(R.id.timestamp);
            this.caption = (TextView) view.findViewById(R.id.caption);
        }
    }

}
