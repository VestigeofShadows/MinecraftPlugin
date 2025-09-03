package space.vestiges.plugin1.applicationlayer.persistentdata;

import space.vestiges.plugin1.infrastructurelayer.SQLitePlayerStatsRepository;

import java.util.UUID;

/**
 * Orchestrates repository operations for the interface layer.
 * This is to separate Plugin1 from infrastructure.
 */
public class PersistenceManager {

    private final PlayerStatsRepository repo = new SQLitePlayerStatsRepository();

    public void savePlayer(PlayerStatsData data) {
        repo.save(data);
    }

    public PlayerStatsData loadPlayer(UUID playerId) {
        return repo.load(playerId);
    }

    public boolean playerExists(UUID playerId) {
        return repo.playerExists(playerId);
    }
}
