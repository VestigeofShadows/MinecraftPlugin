package space.vestiges.plugin1;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsManager {

    private final HashMap<UUID, PlayerStats> activePlayers;
    private final HashMap<UUID, PlayerStats> storedPlayers;

    // TODO: Constructor makes using stored players access json file
    PlayerStatsManager() {
        activePlayers = new HashMap<>();
        storedPlayers = new HashMap<>();
        // TODO: Initialize stored players from json file

    }

    public HashMap<UUID, PlayerStats> getActivePlayers() {
        return activePlayers;
    }
    public HashMap<UUID, PlayerStats> getStoredPlayers() {
        return storedPlayers;
    }
    public void addActivePlayer(Player player, PlayerStats playerstats) {
        activePlayers.put(player.getUniqueId(), playerstats);
    }
    public void addStoredPlayer(Player player, PlayerStats playerstats) {
        storedPlayers.put(player.getUniqueId(), playerstats);
        // TODO: Update Storage
    }
    public void removeActivePlayer(Player player) {
        activePlayers.remove(player.getUniqueId());
    }
    public void removeStoredPlayer(Player player) {
        storedPlayers.remove(player.getUniqueId());
    }
    public boolean isPlayerStored(Player player) {
        return storedPlayers.containsKey(player.getUniqueId());
    }
}
