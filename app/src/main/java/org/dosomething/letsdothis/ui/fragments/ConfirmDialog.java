package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 5/13/15.
 */
public class ConfirmDialog extends android.app.DialogFragment
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String ARG_MESSAGE = "message";
    public static final String TAG         = ConfirmDialog.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ConfirmListener listener;

    public ConfirmDialog()
    {
        // Required empty public constructor
    }

    public static ConfirmDialog newInstance(String message)
    {
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        TextView prompt = (TextView) view.findViewById(R.id.prompt);
        prompt.setText(getArguments().getString(ARG_MESSAGE));
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });

        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(listener != null)
                {
                    listener.onConfirmClick();
                }
                dismiss();
            }
        });

    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    public void setListener(ConfirmListener listener)
    {
        this.listener = listener;
    }

    public interface ConfirmListener
    {
        void onConfirmClick();
    }
}
