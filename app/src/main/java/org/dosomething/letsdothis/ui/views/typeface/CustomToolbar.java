package org.dosomething.letsdothis.ui.views.typeface;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.TextView;

import org.dosomething.letsdothis.R;

/**
 * Created by juy on 9/16/15.
 *
 * A custom toolbar that allows for us to style the title text.
 */
public class CustomToolbar extends Toolbar {

    public CustomToolbar(Context context) {
        super(context);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTitle(String title) {
        // The parent's title field should be empty
        super.setTitle("");

        TextView view = (TextView) findViewById(R.id.toolbar_title);
        view.setText(title);
    }
}
