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
    protected EquipmentStats equipmentStats;
    protected double HP;
    protected double Mana;
    protected double Armor;
    protected double Power;
    protected double HPregen;
    protected double Manaregen;

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



    public double getHP() {
        // HP = (base_hp + armor_hp_modifiers + buff_hp_addmodifiers) * 1 * buff_hp_multmodifiers
        return playerStats.getBase_hp();
    }
    public double getMana() {
        return Mana;
    }
    public double getArmor() {
        return Armor;
    }
    public double getPower() {
        return Power;
    }
    public double getHPregen() {
        return HPregen;
    }
    public double getManaregen() {
        return Manaregen;
    }

}
