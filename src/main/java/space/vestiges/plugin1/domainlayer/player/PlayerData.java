package space.vestiges.plugin1.domainlayer.player;

import org.bukkit.inventory.EquipmentSlot;
import space.vestiges.plugin1.domainlayer.equipment.EquipmentStats;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ----------------------------   CLASS VARIABLES    --------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    protected UUID playerId;
    protected PlayerStats playerStats;
    //TODO, make immunity table class
    //private ImmunityTable immunityTable;
    //private ActionBar
    //private Scoreboard
    //private BuffBar
    // For cached stats, there are 6 EquipmentSlots (helm, chest, legs, boots, hand, offhand) already initialized
    private final Map<EquipmentSlot, EquipmentStats> equipmentStatsCache = new EnumMap<>(EquipmentSlot.class);


    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ----------------------------    CLASS METHODS     --------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Accessor to get a cached stats for a EquipmentSlot
     * @param slot the slot to get stats from the cache for
     * @return EquipmentStats (in the cache)
     */
    public EquipmentStats getCachedStats(EquipmentSlot slot) {
        // Default is all 0
        return equipmentStatsCache.getOrDefault(slot, new EquipmentStats());
    }

    /**
     * Set a player's equipment slot value in the cache.
     * @param slot the EquipmentSlot enum
     * @param stats the stats the slot will hold
     */
    public void setCachedStats(EquipmentSlot slot, EquipmentStats stats) {
        equipmentStatsCache.put(slot, stats);
    }

    /**
     * Clear a player's equipment slot value in the cache
     * @param slot the equipment slot to clear
     */
    public void clearCachedStats(EquipmentSlot slot) {
        equipmentStatsCache.remove(slot);
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ---------------------   Getters/Setters/Constructors    --------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    // constructor
    public PlayerData(UUID playerId, PlayerStats playerstats) {
        this.playerId = playerId;
        this.playerStats = playerstats;
    }

    // getters
    public UUID getPlayerId() {
        return playerId;
    }
    public PlayerStats getPlayerStats() {
        return playerStats;
    }
}
