package space.vestiges.plugin1.adapterlayer.visualUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import space.vestiges.plugin1.domainlayer.player.PlayerStats;
import space.vestiges.plugin1.domainlayer.player.PlayerStatsManager;
import space.vestiges.plugin1.domainlayer.utils.BaseStatsCalculation;

/**
 * Deals with player actionbars
 */
public class PlayerActionBar {

    /**
     * This method updates hud for a player, called in other sections to immediately update
     * @param player the player to update the hud for
     */
    public static void updateHud(Player player, PlayerStatsManager statsManager) {
        PlayerStats stats = statsManager.getPlayerStats(player);
        Component hud = createHud(player, stats);
        player.sendActionBar(hud);
    }

    /**
     * Create a Hud based on a certain player's information and also sends the information.
     * @return Hud Component
     */
    private static Component createHud(Player player, PlayerStats stats) {

        // level display: Lvl:1(0/0)
        double totalXP = stats.getCombat_xp();
        int level = BaseStatsCalculation.getLevelFromTotalXp(totalXP);
        double currentlvlXP = BaseStatsCalculation.getCurrLvlXp(totalXP);   // curr xp in lvl
        double totallvlXP = BaseStatsCalculation.getGapLvlXp(level);        // gap xp in lvl
        Component xp = Component.text("Lvl: " + level + "(" + currentlvlXP + "/" + totallvlXP + ")", NamedTextColor.GREEN);

        // health display:
        double currentHP = stats.getCurrentHP();
        double maxHP = stats.getMaxHP();
        Component hp = Component.text("❤" + currentHP + "/" + maxHP + "❤", NamedTextColor.RED);

        // mana display:
        double currentMana = stats.getCurrentMana();
        double maxMana = stats.getMaxMana();
        Component mana = Component.text("✦" + currentMana + "/" + maxMana + "✦", NamedTextColor.AQUA);

        return hp.append(Component.text("  "))
                .append(xp)
                .append(Component.text("  "))
                .append(mana);
    }
}
