package space.vestiges.plugin1.domainlayer.player.playerPersistence;

import java.util.UUID;

/**
 * Data transfer object that defines what the repository gives back, infrastructure just fills it.
 * Carry raw database values between infrastructure and application
 */
public class PlayerStatsData {
    private final UUID uuid;
    private final String playername;
    private final int last_saved;
    private final double total_xp;

    // Constructor
    public PlayerStatsData(String uuid, String playername, int last_saved, double total_xp) {
        this.uuid = UUID.fromString(uuid);
        this.playername = playername;
        this.last_saved = last_saved;
        this.total_xp = total_xp;
    }

    // Getters
    public UUID getUuid() {
        return uuid;
    }
    public String getPlayername() {
        return playername;
    }
    public int getLast_saved() {
        return last_saved;
    }
    public double getTotal_xp() {
        return total_xp;
    }
}
