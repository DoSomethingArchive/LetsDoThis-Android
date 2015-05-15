package org.dosomething.letsdothis.data;
import org.dosomething.letsdothis.BuildConfig;

/**
 * Created by izzyoji :) on 5/15/15.
 */
public enum Kudo
{
    KUDOS01("", BuildConfig.DEBUG
            ? 638
            : 1271),
    KUDOS02("", BuildConfig.DEBUG
            ? 639
            : 1272),
    KUDOS03("", BuildConfig.DEBUG
            ? 640
            : 1273),
    KUDOS04("", BuildConfig.DEBUG
            ? 641
            : 1274),
    KUDOS05("", BuildConfig.DEBUG
            ? 642
            : 1275),
    KUDOS06("", BuildConfig.DEBUG
            ? 643
            : 1276),
    KUDOS07("", BuildConfig.DEBUG
            ? 644
            : 1277),
    KUDOS08("", BuildConfig.DEBUG
            ? 645
            : 1278),
    KUDOS09("", BuildConfig.DEBUG
            ? 646
            : 1279),
    KUDOS10("", BuildConfig.DEBUG
            ? 647
            : 1280),
    KUDOS11("", BuildConfig.DEBUG
            ? 648
            : 1281),
    KUDOS12("", BuildConfig.DEBUG
            ? 649
            : 1282),
    KUDOS13("", BuildConfig.DEBUG
            ? 650
            : 1283),
    KUDOS14("", BuildConfig.DEBUG
            ? 651
            : 1284),
    KUDOS15("", BuildConfig.DEBUG
            ? 652
            : 1285);

    public String name;
    public  int    id;

    private Kudo(String name, int id)
    {
        this.name = name;
        this.id = id;
    }
}
