package org.dosomething.letsdothis.data;
import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 5/15/15.
 */
public enum Kudo
{
    KUDOS01("crown", BuildConfig.DEBUG
            ? 638
            : 1271, R.drawable.kudos_crown),
    KUDOS02("diamond", BuildConfig.DEBUG
            ? 639
            : 1272, R.drawable.kudos_diamond),
    KUDOS03("fire", BuildConfig.DEBUG
            ? 640
            : 1273, R.drawable.kudos_fire),
    KUDOS04("heart", BuildConfig.DEBUG
            ? 641
            : 1274, R.drawable.kudos_heart),
    KUDOS05("kudos", BuildConfig.DEBUG
            ? 642
            : 1275, R.drawable.kudos),
    KUDOS06("lightbulb", BuildConfig.DEBUG
            ? 643
            : 1276, R.drawable.kudos_lightbulb),
    KUDOS07("pizza", BuildConfig.DEBUG
            ? 644
            : 1277, R.drawable.kudos_pizza),
    KUDOS08("planet", BuildConfig.DEBUG
            ? 645
            : 1278, R.drawable.kudos_planet),
    KUDOS09("poo", BuildConfig.DEBUG
            ? 646
            : 1279, R.drawable.kudos_poo),
    KUDOS10("skull", BuildConfig.DEBUG
            ? 647
            : 1280, R.drawable.kudos_skull),
    KUDOS11("trophy", BuildConfig.DEBUG
            ? 648
            : 1281, R.drawable.kudos_trophy);

    public String name;
    public int    id;
    public int    imageResId;

    Kudo(String name, int id, int imageResId)
    {
        this.name = name;
        this.id = id;
        this.imageResId = imageResId;
    }
}
