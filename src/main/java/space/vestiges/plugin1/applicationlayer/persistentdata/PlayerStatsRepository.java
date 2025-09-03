package space.vestiges.plugin1.applicationlayer.persistentdata;

import java.util.UUID;

/**
 * Contract of communication between applicationlayer and repository layer
 */
public interface PlayerStatsRepository {
    void initStorage();
    void save(PlayerStatsData statsData);
    PlayerStatsData load(UUID uuid);
    boolean playerExists(UUID uuid);
}
