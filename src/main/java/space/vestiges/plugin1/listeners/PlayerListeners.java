package space.vestiges.plugin1.listeners;


import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListeners implements Listener{

    // Pass in main plugin
    private final JavaPlugin plugin;
    public PlayerListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // Get player
        Player player = event.getPlayer();
        plugin.getLogger().info("Player " +  " Joined!!");
        // Check if user is in file, if not, create an entry. then send everything to memory
        //if (/*no file*/) {

        //}

        // Load player initial values

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getLogger().info("Player Quit!!");
        // Get player
        Player player = event.getPlayer();
    }
}
