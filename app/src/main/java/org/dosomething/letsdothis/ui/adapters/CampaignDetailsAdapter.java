package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.Kudos;
import org.dosomething.letsdothis.data.KudosMeta;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.ui.views.KudosView;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class CampaignDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int VIEW_TYPE_CAMPAIGN        = 0;
    public static final int VIEW_TYPE_CAMPAIGN_FOOTER = 1;
    public static final int VIEW_TYPE_REPORT_BACK     = 2;

    private final int webOrange;
    private final int shadowColor;
    private final int slantHeight;
    private final int widthOvershoot;
    private final int heightShadowOvershoot;
    private final int drupalId;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> dataSet = new ArrayList<>();
    private DetailsAdapterClickListener detailsAdapterClickListener;

    private Campaign currentCampaign;
    private int                    selectedPosition = - 1;
    private Random                 random           = new Random();
    private HashMap<Integer, Kudos> kudosedMap       = new HashMap<>();

    public CampaignDetailsAdapter(DetailsAdapterClickListener detailsAdapterClickListener, Resources resources, int drupalId)
    {
        super();
        this.detailsAdapterClickListener = detailsAdapterClickListener;
        this.drupalId = drupalId;
        webOrange = resources.getColor(R.color.web_orange);
        shadowColor = resources.getColor(R.color.black_10);
        slantHeight = resources.getDimensionPixelSize(R.dimen.height_xtiny);
        widthOvershoot = resources.getDimensionPixelSize(R.dimen.space_50);
        heightShadowOvershoot = resources.getDimensionPixelSize(R.dimen.padding_tiny);
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

    public Campaign getCampaign()
    {
        return currentCampaign;
    }

    public interface DetailsAdapterClickListener
    {
        void onScrolledToBottom();

        void proveShareClicked();

        void inviteClicked();

        void onUserClicked(String id);

        void onKudosClicked(ReportBack reportBack, Kudos kudos);
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

            int height = campaignViewHolder.imageView.getContext().getResources()
                                                     .getDimensionPixelSize(
                                                             R.dimen.campaign_height_expanded);
            Picasso.with(campaignViewHolder.imageView.getContext()).load(campaign.imagePath)
                   .resize(0, height).into(campaignViewHolder.imageView);
            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);
            campaignViewHolder.problemFact.setText(campaign.problemFact.replaceAll("\\r\\n", ""));
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

            SlantedBackgroundDrawable background = new SlantedBackgroundDrawable(true, webOrange,
                                                                                 shadowColor,
                                                                                 slantHeight,
                                                                                 widthOvershoot,
                                                                                 heightShadowOvershoot);
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
        }
        else if(getItemViewType(position) == VIEW_TYPE_REPORT_BACK)
        {
            final ReportBack reportBack = (ReportBack) dataSet.get(position);
            final ReportBackViewHolder reportBackViewHolder = (ReportBackViewHolder) holder;

            //FIXME get real avatar
            reportBackViewHolder.avatar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    detailsAdapterClickListener.onUserClicked(reportBack.user.id);
                }
            });

            final Context context = reportBackViewHolder.itemView.getContext();
            Picasso.with(context).
                    load(reportBack.getImagePath()).into(reportBackViewHolder.imageView);

            reportBackViewHolder.name.setText(reportBack.user.id);
            reportBackViewHolder.timestamp.setText(TimeUtils.getTimeSince(
                    reportBackViewHolder.timestamp.getContext(), reportBack.createdAt * 1000));
            reportBackViewHolder.caption.setText(reportBack.caption);
            final boolean selected = position == selectedPosition;
            reportBackViewHolder.kudosToggle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(selected)
                    {
                        selectedPosition = - 1;
                        notifyItemChanged(position);
                    }
                    else
                    {
                        selectedPosition = position;
                        notifyDataSetChanged();
                    }
                }
            });

            ArrayList<KudosMeta> sanitizedKudosList = reportBack.getSanitizedKudosList(drupalId);
            if(selected)
            {
                reportBackViewHolder.kudosToggle.setImageResource(R.drawable.ic_close_kudos);
                reportBackViewHolder.kudosBar.setVisibility(View.VISIBLE);

                // is there a better way to do this?
                for(int i = 0, size = sanitizedKudosList.size(); i < size; i++)
                {
                    final KudosView kudoView = (KudosView) reportBackViewHolder.kudosBar
                            .getChildAt(i);
                    final KudosMeta kudosMeta = sanitizedKudosList.get(i);
                    kudoView.setKudos(kudosMeta);
                    if(kudosedMap.containsKey(reportBack.id))
                    {
                        Kudos mapKudos = kudosedMap.get(reportBack.id);
                        if(mapKudos == kudosMeta.kudos)
                        {
                            kudoView.setSelected(true);
                            kudoView.setCountNum(kudoView.getCountNum() + 1);
                        }
                    }

                    kudoView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Kudos kudos = kudoView.getKudos();
                            if(! kudosMeta.selected && ! reportBack.kudosed)
                            {
                                reportBack.kudosed = true;
                                detailsAdapterClickListener.onKudosClicked(reportBack, kudos);
                                kudosedMap.put(reportBack.id, kudos);

                                int updatedCount = kudoView.getCountNum() + 1;
                                kudoView.setCountNum(updatedCount);
                                kudoView.getImage().startAnimation(
                                        AnimationUtils.loadAnimation(context, R.anim.scale_bounce));
                            }

                        }
                    });

                }

            }
            else
            {
                reportBackViewHolder.kudosBar.setVisibility(View.GONE);

                //fun
                if(sanitizedKudosList.get(0).total == 0)
                {
                    Kudos[] values = Kudos.values();
                    reportBackViewHolder.kudosToggle
                            .setImageResource(values[random.nextInt(values.length)].imageResId);
                }
                else
                {
                    reportBackViewHolder.kudosToggle
                            .setImageResource(sanitizedKudosList.get(0).kudos.imageResId);
                }
            }
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
        protected ImageView kudosToggle;
        protected ViewGroup kudosBar;

        public ReportBackViewHolder(View view)
        {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            this.avatar = (ImageView) view.findViewById(R.id.avatar);
            this.name = (TextView) view.findViewById(R.id.name);
            this.timestamp = (TextView) view.findViewById(R.id.timestamp);
            this.caption = (TextView) view.findViewById(R.id.caption);
            this.kudosToggle = (ImageView) view.findViewById(R.id.kudos_toggle);
            this.kudosToggle.setVisibility(View.VISIBLE);
            this.kudosBar = (ViewGroup) view.findViewById(R.id.kudos_bar);
            view.findViewById(R.id.title).setVisibility(View.GONE);
        }
    }

}
