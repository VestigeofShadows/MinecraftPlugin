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
        //make gson lol
        Gson gson = new Gson();
        PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();

        // Get player
        Player player = event.getPlayer();

        //check if player exists in stored player first
        if (!statsManager.isPlayerStored(player)) {
            // Create temporary stats
            PlayerStats tempstats = new PlayerStats(player.getName());

            // Put it in StoredPlayers
            statsManager.addStoredPlayer(player, tempstats);

            String json = gson.toJson(statsManager.getStoredPlayers());
            Plugin1.getInstance().getLogger().info("Player StoredPlayers json info \n" + json);

            // TODO: Put new entry in active player
            statsManager.addActivePlayer(player, tempstats);
            // TODO: Put new entry in stored players, and update the database
            statsManager.addStoredPlayer(player, tempstats);

        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get player
        Player player = event.getPlayer();
        Plugin1.getInstance().getLogger().info("Player " + player.getName() +  " Quit!!");
    }
}
