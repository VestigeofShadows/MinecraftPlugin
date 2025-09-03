package space.vestiges.plugin1.domainlayer.utils;

/**
 * All calculation of XP goes here
 */
public final class BaseStatsCalculation {

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -----------------------------      Constants     ---------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    //Level constants,
    private static final double BASE = 100;
    private static final double EXP = 2;
    private static final int MAX_LEVEL = 50;

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Class Functions  ----------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ------------------------------ LVL CALCULATION  ----------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    /**
     * This function takes total xp, and returns the level based on it
     * @param totalXp the amount of xp a player has
     * @return level in int
     */
    public static int getLevelFromTotalXp(double totalXp) {
        // Level = 1 + (totalXP/BASE)^(1/EXP)

        // if xp is 0, return level 1
        if (totalXp <= 0) return 1;

        double root = Math.pow(totalXp / BASE, 1.0 / EXP);
        int level = 1 + (int)Math.floor(root);

        // check if level is over the level cap
        return Math.min(level, MAX_LEVEL);
    }

    /**
     * Given current total XP for current level (current xp = totalxp - xp from required to reach this lvl).
     * @param totalXp total_xp a player has
     * @return current xp within a level
     */
    public static double getCurrLvlXp(double totalXp) {
        // get current level
        int level = getLevelFromTotalXp(totalXp);
        // find xp required for current level
        double xpforlvl = totalXPNeededForLevel(level);
        // find xp required for next level NOT USED
        double xpfornxtlvl = totalXPNeededForLevel(level + 1); //not used rn

        // currentxp
        double currentxp = totalXp - xpforlvl;

        //check if it passes levelcap
        if (level >= MAX_LEVEL) {
            currentxp = 0;
        }

        return currentxp;
    }

    /**
     * Given a level, calculate the XP required from that level to the next level.
     * @param level the level input
     * @return XP required from this level to the next
     */
    public static double getGapLvlXp(int level) {
        // max_level check
        if  (level >= MAX_LEVEL) {
            return 0;
        }
        return totalXPNeededForLevel(level + 1) - totalXPNeededForLevel(level);
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ------------------------------- HP CALCULATION  ----------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    /**
     * Get base hp based on level
     * @param level the player's level
     * @return the value of base hp as a double
     */
    public static double getBaseHp(int level) {

        // max level check
        if (level >= MAX_LEVEL) {
            level = MAX_LEVEL;
        }

        // k is small linear boost
        // r is exponential factor
        // L is level
        // baseHP is starting HP

        // HP(L) = round(baseHP * r^(L-1) + k * L)
        return (double) Math.round(100 * Math.pow(1.08, level - 1) + 10 * (level - 1));
    }

    /**
     * can change later, rn it's just 1% of basehp
     * @param level the level input
     * @return the amount of basehp per regen update
     */
    public static double getBaseHpRegen(int level) {
        double maxhp = getBaseHp(level);
        return maxhp/100;
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------- MANA CALCULATION  ----------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    /**
     * Get base mana based on level
     * @param level the player's level
     * @return the value of base mana as a double
     */
    public static double getBaseMana(int level) {

        // max level check
        if (level >= MAX_LEVEL) {
            level = MAX_LEVEL;
        }

        // Mana(L) = round(baseMana * r^(L-1) + k * L)
        return (double) Math.round(50 * Math.pow(1.06, level - 1) + 3 * (level - 1));
    }

    /**
     * can change later, rn it's just 1% of basemana
     * @param level the level input
     * @return the amount of basemana per regen update
     */
    public static double getBaseManaRegen(int level) {
        double maxmana = getBaseMana(level);
        return maxmana/100;
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     HELPER FUNCTIONS     -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Given a level, calculate the totalXP required to reach that level.
     * This ignores level cap
     * @param level the level to calculate for
     * @return the total xp required
     */
    private static double totalXPNeededForLevel(int level) {
        return BASE * Math.pow((level - 1), EXP);
    }
}
