package org.dosomething.letsdothis.data;
import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 5/15/15.
 */
public enum Kudos
{
    crown(BuildConfig.DEBUG
                  ? 638
                  : 1271, R.drawable.kudos_crown),
    diamond(BuildConfig.DEBUG
                    ? 639
                    : 1272, R.drawable.kudos_diamond),
    fire(BuildConfig.DEBUG
                 ? 640
                 : 1273, R.drawable.kudos_fire),
    heart(BuildConfig.DEBUG
                  ? 641
                  : 1274, R.drawable.kudos_heart),
    kudos(BuildConfig.DEBUG
                  ? 642
                  : 1275, R.drawable.kudos),
    lightbulb(BuildConfig.DEBUG
                      ? 643
                      : 1276, R.drawable.kudos_lightbulb),
    pizza(BuildConfig.DEBUG
                  ? 644
                  : 1277, R.drawable.kudos_pizza),
    planet(BuildConfig.DEBUG
                   ? 645
                   : 1278, R.drawable.kudos_planet),
    poo(BuildConfig.DEBUG
                ? 646
                : 1279, R.drawable.kudos_poo),
    skull(BuildConfig.DEBUG
                  ? 647
                  : 1280, R.drawable.kudos_skull),
    trophy(BuildConfig.DEBUG
                   ? 648
                   : 1281, R.drawable.kudos_trophy);

    public int     id;
    public int     imageResId;

    Kudos(int id, int imageResId)
    {
        this.id = id;
        this.imageResId = imageResId;
    }
}
