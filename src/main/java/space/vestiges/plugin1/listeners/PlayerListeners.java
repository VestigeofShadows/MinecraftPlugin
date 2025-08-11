package space.vestiges.plugin1.listeners;


import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import space.vestiges.plugin1.player.PlayerStats;
import space.vestiges.plugin1.player.PlayerStatsManager;
import space.vestiges.plugin1.player.PlayerStatsStorage;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.equipment.EquipmentManager;

public class PlayerListeners implements Listener{
    private final PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();
    private final PlayerStatsStorage statsStorage = Plugin1.getInstance().getStorageManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get player
        Player player = event.getPlayer();

        // If player exists in storage, add to active memory, else create it and add it in active and database
        if (statsStorage.playerExists(player)) {
            Plugin1.getInstance().getLogger().info("Player stats exists");

            loadPlayerInfo(player);
        } else { // Creates player and put it in .db, this happens once, and then run load player
            Plugin1.getInstance().getLogger().info("Player stats does not exist");

            // put default into database
            PlayerStats tempStats = new PlayerStats(player);
            statsStorage.addStoredPlayer(player, tempStats);

            loadPlayerInfo(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get player
        Player player = event.getPlayer();
        statsManager.removeActivePlayer(player);
    }

    // Loads player from database on player join, fill in all fields
    public void loadPlayerInfo(Player player) {
        // add stored stats to active stats
        statsManager.addActivePlayer(player, statsStorage.getPlayerStoredStats(player));
        // find current player in the hashmap
        PlayerStats currentPlayer = statsManager.getPlayerInfo(player);
        // put new values into hashmap

        EquipmentManager equipment = new EquipmentManager();
        equipment.getCombinedStats(player);

        // CALCULATE ALL STATS FUNCTION
        // CALCULATE LEVEL (from totalxp)
        // CALCULATE maxHP (from base + gear) (no buffs)
        // CALCULATE maxMANA (from base + gear) (no buffs)
        // CALCULATE maxSTAMINA (from base + gear) (no buffs)
        // CALCULATE ARMOR (from base + gear) (no buffs)
        // CALCULATE POWER (from base + gear) (no buffs)
        // CALCULATE HASTE (from base + gear) (no buffs)

        // currentPlayer.setMaxHP();
    }
}
