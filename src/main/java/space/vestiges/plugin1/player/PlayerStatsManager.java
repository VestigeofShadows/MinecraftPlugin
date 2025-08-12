package space.vestiges.plugin1.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.equipment.EquipmentStats;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsManager {

    private final HashMap<UUID, PlayerStats> activePlayers;

    public PlayerStatsManager() {
        activePlayers = new HashMap<>();
    }
    // returns the entire HashMap (not sure why you need this)
    public HashMap<UUID, PlayerStats> getActivePlayers() {
        return activePlayers;
    }
    // put (replaces) stats of a player (not very useful)
    public void addActivePlayer(@NotNull Player player, PlayerStats playerstats) {
        activePlayers.put(player.getUniqueId(), playerstats);
    }
    // use player from memory on leave to save memory
    public void removeActivePlayer(Player player) {
        activePlayers.remove(player.getUniqueId());
    }
    /**
     * Return playerstats to change
     *
     * @param player the player info to grab
     * @return PlayerStats for the player
     */
    public PlayerStats getPlayerInfo(Player player) {
        UUID uuid = player.getUniqueId();
        return activePlayers.get(uuid);
    }


    /**
     * Adds mainhand nbt data to playerstats
     */
    public void addMainhandStat(EquipmentStats stats) {

    }

    // TODO: Initialize player stats on join from database and put it in active players armor bonuses

}
