package org.dosomething.letsdothis.data;

import java.util.ArrayList;

/**
 * Data representing a user's campaign report back.
 *
 * Created by juy on 9/24/15.
 */
public class UserReportBack {
    // Report back id
    public String id;

    // Quantity reported back
    public int quantity;

    // Items of the individual photos submitted
    private ArrayList<ReportBackItem> items;

    public UserReportBack() {
        items = new ArrayList<>();
    }

    public void addItem(String id, String caption, String imagePath) {
        ReportBackItem item = new ReportBackItem();
        item.id = id;
        item.caption = caption;
        item.imagePath = imagePath;

        items.add(item);
    }

    public ArrayList<ReportBackItem> getItems() {
        return items;
    }

    /**
     * A single ReportBackItem is a single submitted photo and corresponding caption.
     */
    public class ReportBackItem {
        // Report back item id
        private String id;

        // Photo caption
        private String caption;

        // URL path to the submitted photo
        private String imagePath;

        public String getId() {
            return id;
        }

        public String getCaption() {
            return caption;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}
