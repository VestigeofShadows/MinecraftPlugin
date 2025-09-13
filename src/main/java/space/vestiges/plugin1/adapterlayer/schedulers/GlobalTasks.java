package space.vestiges.plugin1.adapterlayer.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.adapterlayer.visualUtils.PlayerActionBar;
import space.vestiges.plugin1.adapterlayer.visualUtils.PlayerScoreBoard;
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
    private final PlayerScoreBoard boardsManager = Plugin1.getInstance().getBoardsManager();

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
                    PlayerActionBar.updateHud(player, statsManager);
                    boardsManager.updateScoreBoard(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}