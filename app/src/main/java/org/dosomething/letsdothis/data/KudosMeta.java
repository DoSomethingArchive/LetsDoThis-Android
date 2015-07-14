package org.dosomething.letsdothis.data;
import org.jetbrains.annotations.NotNull;

/**
 * Created by izzyoji :) on 7/7/15.
 */
public class KudosMeta implements Comparable<KudosMeta>
{
    public Kudos   kudos;
    public int     total;
    public boolean selected;

    public KudosMeta(Kudos kudos)
    {
        this.kudos = kudos;
    }

    public KudosMeta(Kudos kudos, int total, boolean selected)
    {
        this.kudos = kudos;
        this.total = total;
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof KudosMeta && kudos.equals(((KudosMeta)o).kudos);
    }


    @Override
    public int compareTo(@NotNull KudosMeta another)
    {
        return another.total - total;
    }
}
