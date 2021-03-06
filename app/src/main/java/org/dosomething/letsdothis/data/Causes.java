package org.dosomething.letsdothis.data;

import org.dosomething.letsdothis.R;

/**
 * Helper class for defining the available causes and info like display colors and static backend IDs.
 *
 * Created by juy on 1/26/16.
 */
public class Causes {
    // Array of causes. Order in array intended to be order in which they get displayed.
    public static final String[] LIST_ORDER = {
            Causes.ANIMALS,
            Causes.BULLYING,
            Causes.DISASTERS,
            Causes.DISCRIMINATION,
            Causes.EDUCATION,
            Causes.ENVIRONMENT,
            Causes.HOMELESSNESS,
            Causes.MENTAL_HEALTH,
            Causes.PHYSICAL_HEALTH,
            Causes.POVERTY,
            Causes.RELATIONSHIPS,
            Causes.SEX,
            Causes.VIOLENCE
    };

    public static final String ANIMALS = "Animals";
    public static final String BULLYING = "Bullying";
    public static final String DISASTERS = "Disasters";
    public static final String DISCRIMINATION = "Discrimination";
    public static final String EDUCATION = "Education";
    public static final String ENVIRONMENT = "Environment";
    public static final String HOMELESSNESS = "Homelessness";
    public static final String MENTAL_HEALTH = "Mental Health";
    public static final String PHYSICAL_HEALTH = "Physical Health";
    public static final String POVERTY = "Poverty";
    public static final String RELATIONSHIPS = "Relationships";
    public static final String SEX = "Sex";
    public static final String VIOLENCE = "Violence";

    private static final int ANIMALS_ID = 16;
    private static final int BULLYING_ID = 13;
    private static final int DISASTERS_ID = 12;
    private static final int DISCRIMINATION_ID = 14;
    private static final int EDUCATION_ID = 2;
    private static final int ENVIRONMENT_ID = 4;
    private static final int HOMELESSNESS_ID = 6;
    private static final int MENTAL_HEALTH_ID = 19;
    private static final int PHYSICAL_HEALTH_ID = 5;
    private static final int POVERTY_ID = 15;
    private static final int RELATIONSHIPS_ID = 21;
    private static final int SEX_ID = 1;
    private static final int VIOLENCE_ID = 17;

    /**
     * Returns the drawable resource id for a given cause name.
     *
     * @param causeName
     * @return int drawable resource id
     */
    public static int getBackgroundRes(final String causeName) {
        switch (causeName) {
            case ANIMALS:
                return R.drawable.bg_animals;
            case BULLYING:
                return R.drawable.bg_bullying;
            case DISASTERS:
                return R.drawable.bg_disasters;
            case DISCRIMINATION:
                return R.drawable.bg_discrimination;
            case EDUCATION:
                return R.drawable.bg_education;
            case ENVIRONMENT:
                return R.drawable.bg_environment;
            case HOMELESSNESS:
                return R.drawable.bg_homelessness;
            case MENTAL_HEALTH:
                return R.drawable.bg_health;
            case PHYSICAL_HEALTH:
                return R.drawable.bg_health;
            case POVERTY:
                return R.drawable.bg_poverty;
            case RELATIONSHIPS:
                return R.drawable.bg_relationships;
            case SEX:
                return R.drawable.bg_sex;
            case VIOLENCE:
                return R.drawable.bg_violence;
            default:
                return R.drawable.bg_onboarding;
        }
    }

    /**
     * Returns the color resource id for a given cause name.
     * Example usage: Causes.getColor(Causes.ANIMALS);
     *
     * @param causeName
     * @return int color resource id
     */
    public static int getColorRes(final String causeName) {
        switch (causeName) {
            case ANIMALS:
                return R.color.cause_animals;
            case BULLYING:
                return R.color.cause_bullying;
            case DISASTERS:
                return R.color.cause_disasters;
            case DISCRIMINATION:
                return R.color.cause_discrimination;
            case EDUCATION:
                return R.color.cause_education;
            case ENVIRONMENT:
                return R.color.cause_environment;
            case HOMELESSNESS:
                return R.color.cause_homelessness;
            case MENTAL_HEALTH:
                return R.color.cause_mental_health;
            case PHYSICAL_HEALTH:
                return R.color.cause_physical_health;
            case POVERTY:
                return R.color.cause_poverty;
            case RELATIONSHIPS:
                return R.color.cause_relationships;
            case SEX:
                return R.color.cause_sex;
            case VIOLENCE:
                return R.color.cause_violence;
            default:
                return R.color.cause_default;
        }
    }

    /**
     * Returns the Phoenix ID for a given cause name.
     * Example usage: Causes.getId(Causes.ANIMALS);
     *
     * @param causeName
     * @return int
     */
    public static int getId(final String causeName) {
        switch (causeName) {
            case ANIMALS:
                return ANIMALS_ID;
            case BULLYING:
                return BULLYING_ID;
            case DISASTERS:
                return DISASTERS_ID;
            case DISCRIMINATION:
                return DISCRIMINATION_ID;
            case EDUCATION:
                return EDUCATION_ID;
            case ENVIRONMENT:
                return ENVIRONMENT_ID;
            case HOMELESSNESS:
                return HOMELESSNESS_ID;
            case MENTAL_HEALTH:
                return MENTAL_HEALTH_ID;
            case PHYSICAL_HEALTH:
                return PHYSICAL_HEALTH_ID;
            case POVERTY:
                return POVERTY_ID;
            case RELATIONSHIPS:
                return RELATIONSHIPS_ID;
            case SEX:
                return SEX_ID;
            case VIOLENCE:
                return VIOLENCE_ID;
            default:
                return -1;
        }
    }

    /**
     * Returns the cause description string resource ID.
     *
     * @param causeName
     * @return int
     */
    public static int getDescriptionRes(final String causeName) {
        switch (causeName) {
            case ANIMALS:
                return R.string.cause_animals_desc;
            case BULLYING:
                return R.string.cause_bullying_desc;
            case DISASTERS:
                return R.string.cause_disasters_desc;
            case DISCRIMINATION:
                return R.string.cause_discrimination_desc;
            case EDUCATION:
                return R.string.cause_education_desc;
            case ENVIRONMENT:
                return R.string.cause_environment_desc;
            case HOMELESSNESS:
                return R.string.cause_homelessness_desc;
            case MENTAL_HEALTH:
                return R.string.cause_mental_health_desc;
            case PHYSICAL_HEALTH:
                return R.string.cause_physical_health_desc;
            case POVERTY:
                return R.string.cause_poverty_desc;
            case RELATIONSHIPS:
                return R.string.cause_relationships_desc;
            case SEX:
                return R.string.cause_sex_desc;
            case VIOLENCE:
                return R.string.cause_violence_desc;
            default:
                return R.string.cause_default_desc;
        }
    }
}
