package org.dosomething.letsdothis.data;
import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 6/23/15.
 */
public enum InterestGroup {
    A(R.string.interest_0, 1300, 1300, 667),
    B(R.string.interest_1, 1301, 1301, 668),
    C(R.string.interest_2, 1302, 1302, 669),
    D(R.string.interest_3, 1303, 1303, 670);

    // Initial display name resource id
    public int nameResId;

    // Group term id
    public int id;

    /**
     * Constructor
     *
     * @param nameResId int String resource id for this group's default name
     * @param prodId int Term id on the production server
     * @param thorId int Term id on the thor server
     * @param qaId int Term id on the qa server
     */
    InterestGroup(int nameResId, int prodId, int thorId, int qaId) {
        this.nameResId = nameResId;

        if (BuildConfig.BUILD_TYPE.equals("release")) {
            this.id = prodId;
        }
        else if (BuildConfig.BUILD_TYPE.equals("internal")) {
            this.id = thorId;
        }
        else {
            this.id = qaId;
        }
    }
}
