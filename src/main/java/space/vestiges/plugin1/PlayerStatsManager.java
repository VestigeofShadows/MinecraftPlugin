package space.vestiges.plugin1;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsManager {

    private final HashMap<UUID, PlayerStats> activePlayers;

    // TODO: Constructor makes using stored players access json file
    PlayerStatsManager() {
        activePlayers = new HashMap<>();
        // TODO: Initialize stored players from json file

    }
    public HashMap<UUID, PlayerStats> getActivePlayers() {
        return activePlayers;
    }
    // put stats into a player
    public void addActivePlayer(Player player, PlayerStats playerstats) {
        activePlayers.put(player.getUniqueId(), playerstats);
    }
    public void removeActivePlayer(Player player) {
        activePlayers.remove(player.getUniqueId());
    }
    public PlayerStats getPlayerInfo(Player player) {
        UUID uuid = player.getUniqueId();
        return activePlayers.get(uuid);
    }
}
