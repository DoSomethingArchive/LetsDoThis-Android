package org.dosomething.letsdothis.data;
import android.content.Context;

import com.google.gson.annotations.SerializedName;

import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

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
    public KudosData kudos;

    public String getImagePath()
    {
        return media.uri;
    }

    public LinkedHashMap<Kudo, Integer> getSanitizedKudosMap(Context context)
    {
        LinkedHashMap<Kudo, Integer> sortedMap = new LinkedHashMap<>();
        if(kudos == null)
        {
            for(Kudo kudo : Kudo.values())
            {
                sortedMap.put(kudo, 0);
            }
        }
        else
        {
            TreeMap<Integer, Kudo> map = new TreeMap<>(Collections.reverseOrder());
            for(ReportBack.KudosData.KudosWrapper kudosWrapper : kudos.data)
            {
                Kudo kudo = kudosWrapper.term.getKudo();
                kudo.selected = kudosWrapper.kudos_items.selectedByMe(AppPrefs.getInstance(context).getCurrentUserId());
                map.put(kudosWrapper.kudos_items.total, kudo);
            }


            for(Map.Entry<Integer, Kudo> entry : map.entrySet())
            {
                sortedMap.put(entry.getValue(), entry.getKey());
            }

            for(Kudo kudo : Kudo.values())
            {
                if(!sortedMap.containsKey(kudo))
                {
                    sortedMap.put(kudo, 0);
                }
            }
        }


        return sortedMap;
    }

    private static class Media
    {
        public String uri;
        public String type;
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

                public Kudo getKudo()
                {
                    return Kudo.valueOf(name);
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
                        //i think these are drupal id's ;(
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
