package org.dosomething.letsdothis.data;
import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 6/23/15.
 */
public enum InterestGroup
{
    A(R.string.interest_0, BuildConfig.DEBUG
            ? 667
            : 1300),

    B(R.string.interest_1, BuildConfig.DEBUG
            ? 668
            : 1301),

    C(R.string.interest_2, BuildConfig.DEBUG
            ? 669
            : 1302),

    D(R.string.interest_3, BuildConfig.DEBUG
            ? 670
            : 1303);

    public int nameResId;
    public int id;

    private InterestGroup(int nameResId, int id)
    {
        this.nameResId = nameResId;
        this.id = id;
    }
}
