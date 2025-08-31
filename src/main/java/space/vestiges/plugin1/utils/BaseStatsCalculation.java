package space.vestiges.plugin1.utils;

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
        return 1 + (int)Math.floor(root);
    }

    /**
     * Given current total XP for current level (current xp = totalxp - xp from required to reach this lvl).
     * @param totalXp
     * @return Xp needed for next level
     */
    public static double getCurrLvlXp(double totalXp) {
        // get current level
        int level = getLevelFromTotalXp(totalXp);
        // find xp required for current level
        double xpforlvl = totalXPNeededForLevel(level);
        // find xp required for next level
        double xpfornxtlvl = totalXPNeededForLevel(level + 1);
        //current xp in level
        return totalXp - xpforlvl;
    }

    /**
     * Given a level, calculate the XP required from that level to the next level.
     * @param level the level input
     * @return XP required from this level to the next
     */
    public static double getGapLvlXp(int level) {
        return totalXPNeededForLevel(level + 1) - totalXPNeededForLevel(level);
    }

    /**
     * Get base hp based on level
     * @param level the player's level
     * @return the value of base hp as a double
     */
    public static double getBaseHp(int level) {
        // k is small linear boost
        // r is exponential factor
        // L is level
        // baseHP is starting HP

        // HP(L) = round(baseHP * r^(L-1) + k * L)
        return (double) Math.round(100 * Math.pow(1.08, level - 1) + 10 * (level - 1));
    }

    /**
     * Get base mana based on level
     * @param level the player's level
     * @return the value of base mana as a double
     */
    public static double getBaseMana(int level) {
        // Mana(L) = round(baseMana * r^(L-1) + k * L)
        return (double) Math.round(50 * Math.pow(1.06, level - 1) + 3 * (level - 1));
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     HELPER FUNCTIONS     -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Given a level, calculate the totalXP required to reach that level.
     * @param level the level to calculate for
     * @return the total xp required
     */
    private static double totalXPNeededForLevel(int level) {
        return BASE * Math.pow((level - 1), EXP);
    }
}
