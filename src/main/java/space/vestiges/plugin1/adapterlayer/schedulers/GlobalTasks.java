package space.vestiges.plugin1.adapterlayer.schedulers;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.adapterlayer.visualUtils.PlayerHud;
import space.vestiges.plugin1.domainlayer.player.PlayerStatsManager;

/**
 * Update values for players, such as hud and regeneration.
 */
public class GlobalTasks {

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
     *
     * @param plugin the server plugin
     */

    public void startGlobalActionBarTask(Plugin1 plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerHud.updateHud(player, statsManager);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }


    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -----------------------------  Helper Functions  ---------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    private Scoreboard createScoreBoard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        // Register objective
        Objective objective = scoreboard.registerNewObjective("test", "dummy", ChatColor.GREEN + "My Stats");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Add some scores (lines)
        Score score1 = objective.getScore(ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + "10");
        //score1.setScore(2);

        Score score2 = objective.getScore(ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE + "5");
        //score2.setScore(1);

        return scoreboard;
    }
}