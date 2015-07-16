package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;
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
    public static final  int BUTTON_STATE_SEARCH  = 0;
    public static final  int BUTTON_STATE_CANCEL  = 1;
    public static final  int BUTTON_STATE_GONE    = 2;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private List<Object> invites = new ArrayList<>();
    private InviteAdapterClickListener inviteAdapterClickListener;
    private int buttonState = BUTTON_STATE_GONE;

    public InvitesAdapter(InviteAdapterClickListener inviteAdapterClickListener)
    {
        this.inviteAdapterClickListener = inviteAdapterClickListener;
    }

    public interface InviteAdapterClickListener
    {
        void onInviteClicked(String title, String code);

        void onSearchClicked(String code);

        void onCancelClicked();
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
                                                  .inflate(R.layout.item_invite, parent, false);
                return new InviteViewHolder(inviteLayout);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        switch(position)
        {
            case VIEW_TYPE_INPUT_CODE:
                final InputCodeViewHolder inputCodeViewHolder = (InputCodeViewHolder) holder;

                TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener()
                {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                    {
                        if(buttonState == BUTTON_STATE_SEARCH)
                        {
                            handleSearchClick(inputCodeViewHolder);
                        }

                        return false;
                    }
                };

                inputCodeViewHolder.word3.setOnEditorActionListener(onEditorActionListener);

                final TextWatcher codeWatcher = new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {

                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        if(buttonState != BUTTON_STATE_CANCEL)
                        {
                            updateCodeHolder(inputCodeViewHolder);
                        }
                    }
                };

                addTextWatcher(codeWatcher, inputCodeViewHolder.word1,
                               inputCodeViewHolder.word2,
                               inputCodeViewHolder.word3 );

                switch(buttonState)
                {
                    case BUTTON_STATE_GONE:
                        inputCodeViewHolder.join.setVisibility(View.GONE);
                        break;
                    case BUTTON_STATE_CANCEL:
                        removeTextWatcherAndClearText(codeWatcher, inputCodeViewHolder.word1,
                                                      inputCodeViewHolder.word2,
                                                      inputCodeViewHolder.word3);
                        addTextWatcher(codeWatcher, inputCodeViewHolder.word1,
                                       inputCodeViewHolder.word2,
                                       inputCodeViewHolder.word3 );
                        inputCodeViewHolder.join.setText(R.string.cancel_search);
                        inputCodeViewHolder.join.setBackgroundResource(R.drawable.bg_gray_rounded_rect_filled);
                        inputCodeViewHolder.join.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                inviteAdapterClickListener.onCancelClicked();
                                setButtonState(BUTTON_STATE_GONE);
                            }
                        });

                        break;
                    case BUTTON_STATE_SEARCH:
                        inputCodeViewHolder.join.setText(R.string.search_for_group);
                        inputCodeViewHolder.join.setBackgroundResource(R.drawable.bg_cerulean_rounded_rect_filled);
                        inputCodeViewHolder.join.setVisibility(View.VISIBLE);
                        inputCodeViewHolder.join.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                //to kill the keyboard, so the fragment animation isn't funky
                                inputCodeViewHolder.word3.onEditorAction(EditorInfo.IME_ACTION_DONE);
                            }
                        });
                        break;
                }
                break;
            case VIEW_TYPE_FOOTER:
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
                        if(buttonState != BUTTON_STATE_CANCEL)
                        {
                            inviteAdapterClickListener.onInviteClicked(invite.title, invite.code);
                        }
                    }
                });
                break;

        }
    }

    private void handleSearchClick(InputCodeViewHolder inputCodeViewHolder)
    {
        String code = getCode(inputCodeViewHolder.word1, inputCodeViewHolder.word2,
                              inputCodeViewHolder.word3);
        inviteAdapterClickListener.onSearchClicked(code);
    }

    private void removeTextWatcherAndClearText(TextWatcher watcher, EditText... editTexts)
    {
        for(EditText editText : editTexts)
        {
            editText.removeTextChangedListener(watcher);
            editText.setText(null);
        }
    }

    private void addTextWatcher(TextWatcher watcher, EditText... editTexts)
    {
        for(EditText editText : editTexts)
        {
            editText.addTextChangedListener(watcher);
        }
    }

    public static String getCode(EditText... editTexts)
    {
        String code = "";
        for(EditText editText : editTexts)
        {
            code += WordUtils.capitalize(editText.getText().toString());
        }
        return code;
    }

    private void updateCodeHolder(InputCodeViewHolder holder)
    {
        boolean allFilled = ! TextUtils
                .isEmpty(holder.word1.getText()) && ! TextUtils
                .isEmpty(holder.word2.getText()) && ! TextUtils
                .isEmpty(holder.word3.getText());
        if(allFilled)
        {
            buttonState = BUTTON_STATE_SEARCH;
        }
        else
        {
            buttonState = BUTTON_STATE_GONE;
        }

        notifyItemChanged(0);
    }

    public void setData(ArrayList<Invite> invites)
    {
        this.invites.clear();
        this.invites.add("input placeholder");
        this.invites.add("footer placeholder");
        this.invites.addAll(invites);
        notifyDataSetChanged();
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

    public void setButtonState(int state)
    {
        this.buttonState = state;
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
        protected EditText word1;
        protected EditText word2;
        protected EditText word3;

        public InputCodeViewHolder(View itemView)
        {
            super(itemView);
            this.join = (Button) itemView.findViewById(R.id.join);
            this.word1 = (EditText) itemView.findViewById(R.id.word1);
            this.word2 = (EditText) itemView.findViewById(R.id.word2);
            this.word3 = (EditText) itemView.findViewById(R.id.word3);
        }
    }

}
