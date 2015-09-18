package org.dosomething.letsdothis.data;import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by izzyoji :) on 4/23/15.
 */
public class ReportBack
{
    public int       id;
    public Campaign  campaign;
    @SerializedName("created_at")
    public long      createdAt;
    public String    caption;
    public Media     media;
    public User      user;
    public ReportBackData reportback;
    public KudosData kudos;
    public boolean   kudosed;

    public String getImagePath()
    {
        return media.uri;
    }

    public ArrayList<KudosMeta> getSanitizedKudosList(int drupalId)
    {
        ArrayList<KudosMeta> sortedList = new ArrayList<>();
        if(kudos == null)
        {
            for(Kudos kudos : Kudos.values())
            {
                KudosMeta meta = new KudosMeta(kudos);
                meta.kudos = kudos;
                sortedList.add(meta);
            }
        }
        else
        {
            for(ReportBack.KudosData.KudosWrapper kudosWrapper : kudos.data)
            {
                Kudos kudos = kudosWrapper.term.getKudo();
                int total = kudosWrapper.kudos_items.total;
                boolean selected = kudosWrapper.kudos_items.selectedByMe(String.valueOf(drupalId));
                KudosMeta meta = new KudosMeta(kudos, total, selected);
                if(selected)
                {
                    kudosed = true;
                }
                sortedList.add(meta);
            }

            Collections.sort(sortedList);

            for(Kudos kudos : Kudos.values())
            {
                KudosMeta meta = new KudosMeta(kudos);
                if(!sortedList.contains(meta))
                {
                    sortedList.add(meta);
                }
            }
        }

        return sortedList;
    }

    private static class Media
    {
        public String uri;
        public String type;
    }

    public class ReportBackData {
        public int quantity;
    }

    public class KudosData
    {
        public KudosWrapper[] data;

        public class KudosWrapper
        {
            public KudosTermWrapper  term;
            public KudosItemsWrapper kudos_items;

            public class KudosTermWrapper
            {
                private int    id;
                private String name;

                public Kudos getKudo()
                {
                    return Kudos.valueOf(name);
                }
            }

            public class KudosItemsWrapper
            {
                public  int         total;
                private KudosItem[] data;

                private class KudosItem
                {
                    private User user;
                }

                public boolean selectedByMe(String userId)
                {
                    for(KudosItem kudosItem : data)
                    {
                        if(kudosItem.user.id.equals(userId))
                        {
                            return true;
                        }
                    }

                    return false;
                }
            }
        }
    }
}
