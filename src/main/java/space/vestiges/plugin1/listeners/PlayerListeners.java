package space.vestiges.plugin1.listeners;


import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import space.vestiges.plugin1.PlayerStats;
import space.vestiges.plugin1.PlayerStatsManager;
import space.vestiges.plugin1.PlayerStatsStorage;
import space.vestiges.plugin1.Plugin1;

public class PlayerListeners implements Listener{
    private final PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();
    private final PlayerStatsStorage statsStorage = Plugin1.getInstance().getStorageManager();

    public void loadPlayerInfo(Player player) {
        statsManager.addActivePlayer(player, statsStorage.getPlayerStoredStats(player));
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get player
        Player player = event.getPlayer();

        // If player exists in storage, add to active memory, else create it and add it in active and database
        if (statsStorage.playerExists(player)) {
            Plugin1.getInstance().getLogger().info("Player stat exists");
            loadPlayerInfo(player);
        } else { // Creates player and put it in .db, this happens once, and then run load player
            Plugin1.getInstance().getLogger().info("Player stat does not exist");

            PlayerStats tempStats = new PlayerStats(player);
            statsStorage.addStoredPlayer(player, tempStats);
            loadPlayerInfo(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get player
        Player player = event.getPlayer();

    }
}
