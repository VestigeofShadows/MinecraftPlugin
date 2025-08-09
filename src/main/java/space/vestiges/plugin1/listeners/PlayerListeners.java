package space.vestiges.plugin1.listeners;


import com.google.gson.Gson;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import space.vestiges.plugin1.PlayerStats;
import space.vestiges.plugin1.PlayerStatsManager;
import space.vestiges.plugin1.PlayerStatsStorage;
import space.vestiges.plugin1.Plugin1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListeners implements Listener{

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();
        PlayerStatsStorage statsStorage = Plugin1.getInstance().getStorageManager();

        // Get player
        Player player = event.getPlayer();

        // If player exists in storage, add to active memory, else create it and add it in active and database
        if (statsStorage.playerExists(player)) {
            //debug text delete later
            Plugin1.getInstance().getLogger().info("Player stat exists");
            statsManager.addActivePlayer(player, statsStorage.getPlayerStoredStats(player));
        } else {
            //debug text delete later
            Plugin1.getInstance().getLogger().info("Player stat does not exist");

            PlayerStats tempStats = new PlayerStats(player.getName());
            statsManager.addActivePlayer(player, tempStats);
            statsStorage.addStoredPlayer(player, tempStats);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get player
        Player player = event.getPlayer();

    }
}
