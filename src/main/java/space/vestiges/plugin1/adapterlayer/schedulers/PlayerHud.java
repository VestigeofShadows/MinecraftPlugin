package space.vestiges.plugin1.adapterlayer.schedulers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.domainlayer.model.player.PlayerStats;
import space.vestiges.plugin1.domainlayer.utils.BaseStatsCalculation;
import space.vestiges.plugin1.applicationlayer.PlayerStatsManager;

/**
 * Update values for players, such as hud and regeneration.
 */
public class PlayerHud {

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -----------------------------      Constants     ---------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    private final PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ---------------------------    Class Functions   ---------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Create a runnable task that loops and send hud over and over again
     * @param plugin the server plugin
     */
    /*
    public void startGlobalActionBarTask(Plugin1 plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Component hud = createHud(player);
                    player.sendActionBar(hud);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    } */

    /**
     * This method updates hud for a player, called in other sections to immediately update
     * @param player the player to update the hud for
     */
    public void updateHud(Player player) {
        Component hud = createHud(player);
        player.sendActionBar(hud);
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -----------------------------  Helper Functions  ---------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Create a Hud based on a certain player's information.
     * @return Hud Component
     */
    private Component createHud(Player player) {

        PlayerStats stats = statsManager.getPlayerInfo(player);

        // level display: Lvl:1(0/0)
        double totalXP = stats.getCombat_xp();
        int level = BaseStatsCalculation.getLevelFromTotalXp(totalXP);
        double currentlvlXP = BaseStatsCalculation.getCurrLvlXp(totalXP);   // curr xp in lvl
        double totallvlXP = BaseStatsCalculation.getGapLvlXp(level);   // gap xp in lvl
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
