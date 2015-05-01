package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Invite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 5/1/15.
 */
public class InvitesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final int VIEW_TYPE_INPUT_CODE = 0;
    private static final int VIEW_TYPE_FOOTER     = 1;
    private static final int VIEW_TYPE_INVITE     = 2;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private List<Object> invites = new ArrayList<>();
    private InviteAdapterClickListener inviteAdapterClickListener;
    private boolean showJoinButton;

    public InvitesAdapter(List<Invite> invites, InviteAdapterClickListener inviteAdapterClickListener)
    {
        this.inviteAdapterClickListener = inviteAdapterClickListener;
        this.invites.add("input placeholder");
        this.invites.add("footer placeholder");
        this.invites.addAll(invites);
    }

    public interface InviteAdapterClickListener
    {
        void onInviteClicked(String title, String code);

        void onJoinClicked(String code);

        void onCodeEntered(String code);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_INPUT_CODE:
                View inputCodeLayout = LayoutInflater.from(parent.getContext())
                                                  .inflate(R.layout.item_invite_code, parent,
                                                           false);
                return new InputCodeViewHolder(inputCodeLayout);
            case VIEW_TYPE_FOOTER:
                View footerLayout = LayoutInflater.from(parent.getContext())
                                                  .inflate(R.layout.item_invite_footer, parent,
                                                           false);
                return new SectionTitleViewHolder((TextView) footerLayout);
            default:
                View inviteLayout = LayoutInflater.from(parent.getContext())
                                                      .inflate(R.layout.item_invite,
                                                               parent, false);
                return new InviteViewHolder(inviteLayout);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        switch(position)
        {
            case 0:
                final InputCodeViewHolder inputCodeViewHolder = (InputCodeViewHolder) holder;
                TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener()
                {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                    {

                        inviteAdapterClickListener
                                .onCodeEntered(inputCodeViewHolder.code.getText().toString());

                        return false;
                    }
                };
                inputCodeViewHolder.code.setOnEditorActionListener(onEditorActionListener);

                if(showJoinButton)
                {
                    inputCodeViewHolder.join.setVisibility(View.VISIBLE);
                    inputCodeViewHolder.code.setText(null);
                    inputCodeViewHolder.code.setFocusable(false);
                }
                else
                {
                    inputCodeViewHolder.join.setVisibility(View.GONE);
                    inputCodeViewHolder.code.setOnTouchListener(new View.OnTouchListener()
                    {
                        @Override
                        public boolean onTouch(View v, MotionEvent event)
                        {
                            if(!inputCodeViewHolder.code.isFocusableInTouchMode())
                            {
                                inputCodeViewHolder.code.setFocusableInTouchMode(true);
                            }
                            return false;
                        }
                    });
                }

                inputCodeViewHolder.join.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        inviteAdapterClickListener.onJoinClicked(inputCodeViewHolder.code.getText().toString());
                    }
                });
                break;
            case 1:
                break;
            default:
                final Invite invite = (Invite) invites.get(position);

                final InviteViewHolder inviteViewHolder = (InviteViewHolder) holder;
                inviteViewHolder.title.setText(invite.title);
                inviteViewHolder.details.setText(invite.details);
                inviteViewHolder.code.setText(invite.code);
                inviteViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        inviteAdapterClickListener.onInviteClicked(invite.title, invite.code);
                    }
                });
                break;

        }
    }

    @Override
    public int getItemViewType(int position)
    {
        switch(position)
        {
            case 0:
                return VIEW_TYPE_INPUT_CODE;
            case 1:
                return VIEW_TYPE_FOOTER;
            default:
                return VIEW_TYPE_INVITE;
        }
    }

    @Override
    public int getItemCount()
    {
        return invites.size();
    }

    public void showJoinButton(boolean showJoinButton)
    {
        this.showJoinButton = showJoinButton;
        notifyItemChanged(0);
    }

    public static class InviteViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView title;
        protected TextView details;
        protected TextView code;

        public InviteViewHolder(View itemView)
        {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.details = (TextView) itemView.findViewById(R.id.details);
            this.code = (TextView) itemView.findViewById(R.id.code);
        }
    }

    public static class InputCodeViewHolder extends RecyclerView.ViewHolder
    {
        protected Button   join;
        protected EditText code;

        public InputCodeViewHolder(View itemView)
        {
            super(itemView);
            this.join = (Button) itemView.findViewById(R.id.join);
            //FIXME get real data
            this.join.setText("Join Super Awesome Campaign");
            this.code = (EditText) itemView.findViewById(R.id.code);
        }
    }

}
