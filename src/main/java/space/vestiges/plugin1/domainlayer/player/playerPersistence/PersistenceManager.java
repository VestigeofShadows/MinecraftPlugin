package space.vestiges.plugin1.domainlayer.player.playerPersistence;

import space.vestiges.plugin1.infrastructurelayer.SQLitePlayerStatsRepository;

import java.util.UUID;

/**
 * Orchestrates repository operations for the interface layer.
 * This is to separate Plugin1 from infrastructure.
 */
public class PersistenceManager {

    private final PlayerStatsRepository repo = new SQLitePlayerStatsRepository();

    /**
     * This updates persistent storage if it exists, or creates a new entry if it doesn't exist.
     * @param data The PlayerStatsData to save
     */
    public void savePlayer(PlayerStatsData data) {
        repo.save(data);
    }

    /**
     * This returns a player information if it exists
     * @param playerId which player's information to extract
     * @return PlayerStatsData
     */
    public PlayerStatsData loadPlayer(UUID playerId) {
        return repo.load(playerId);
    }

    public boolean playerExists(UUID playerId) {
        return repo.playerExists(playerId);
    }
}
