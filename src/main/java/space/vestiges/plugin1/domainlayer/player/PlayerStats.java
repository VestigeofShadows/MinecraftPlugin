package space.vestiges.plugin1.domainlayer.player;

import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.domainlayer.equipment.EquipmentStats;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStats {

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ----------------------------   CLASS VARIABLES    --------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    // Persistent Data
    private UUID playerId;
    private String name;
    private int last_saved;
    private double combat_xp;

    // Derived Data
    private int combatLevel;
    private double base_hp;
    private double base_mana;
    private double base_stamina;
    private double base_armor;
    private double base_power;
    private double base_hp_regen;
    private double base_mana_regen;

    // Final Derived Stats (put this somewhere else)
    private double maxHP;
    private double maxMana;
    private double maxStamina;
    private double armor;
    private double power;
    private double hpregen;
    private double manaregen;
    private double attackSpeed;

    // Runtime / Ephemeral State
    private transient double currentHP;
    private transient double currentMana;
    private transient double currentStamina;
    private transient long lastAttackTime;

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // todo--------------       EQUIPMENT STUFF MOVE THIS       -------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // For cached stats, there are 6 EquipmentSlots (helm, chest, legs, boots, hand, offhand) already initialized
    private final Map<EquipmentSlot, EquipmentStats> equipmentStatsCache = new EnumMap<>(EquipmentSlot.class);

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

    public void subtractEquipmentStats(@NotNull EquipmentStats stats) {
        this.maxHP = maxHP - stats.getHp();
        this.maxMana = maxMana - stats.getMana();
        this.maxStamina = maxStamina - stats.getStamina();
        this.armor = armor - stats.getArmor();
        this.power = power - stats.getPower();
        this.attackSpeed = attackSpeed - stats.getAttackSpeed();
    }

    public void addEquipmentStats(@NotNull EquipmentStats stats) {
        this.maxHP = maxHP + stats.getHp();
        this.maxMana = maxMana + stats.getMana();
        this.maxStamina = maxStamina + stats.getStamina();
        this.armor = armor + stats.getArmor();
        this.power = power + stats.getPower();
        this.attackSpeed = attackSpeed + stats.getAttackSpeed();
    }

    public void normalizeStats() {
        this.armor = roundToDecimals(clampToZeroIfTiny(this.armor), 3);
        this.power = roundToDecimals(clampToZeroIfTiny(this.power), 3);
        this.maxHP = roundToDecimals(clampToZeroIfTiny(this.maxHP), 3);
        this.maxMana = roundToDecimals(clampToZeroIfTiny(this.maxMana), 3);
        this.maxStamina = roundToDecimals(clampToZeroIfTiny(this.maxStamina), 3);
        this.attackSpeed = roundToDecimals(clampToZeroIfTiny(this.attackSpeed), 3);
    }

    private double clampToZeroIfTiny(double val) {
        return Math.abs(val) < 1e-10 ? 0.0 : val;
    }

    private double roundToDecimals(double value, int decimals) {
        double scale = Math.pow(10, decimals);
        return Math.round(value * scale) / scale;
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ---------------------------     CONSTRUCTORS     ---------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    public PlayerStats(UUID playerId, String name, int last_saved, double combat_xp, int combatLevel, double base_hp, double base_mana, double base_stamina, double base_armor, double base_power, double base_hp_regen, double base_mana_regen, double maxHP, double maxMana, double maxStamina, double armor, double power, double hpregen, double manaregen, double attackSpeed, double currentHP, double currentMana, double currentStamina, long lastAttackTime) {
        this.playerId = playerId;
        this.name = name;
        this.last_saved = last_saved;
        this.combat_xp = combat_xp;

        this.combatLevel = combatLevel;
        this.base_hp = base_hp;
        this.base_mana = base_mana;
        this.base_stamina = base_stamina;
        this.base_armor = base_armor;
        this.base_power = base_power;
        this.base_hp_regen = base_hp_regen;
        this.base_mana_regen = base_mana_regen;

        this.maxHP = maxHP;
        this.maxMana = maxMana;
        this.maxStamina = maxStamina;
        this.armor = armor;
        this.power = power;
        this.hpregen = hpregen;
        this.manaregen = manaregen;
        this.attackSpeed = attackSpeed;

        this.currentHP = currentHP;
        this.currentMana = currentMana;
        this.currentStamina = currentStamina;
        this.lastAttackTime = lastAttackTime;
    }
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Class Functions  ----------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Prints the entire PlayerStats object as String
     * used for debugging
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(
                """
                        PlayerStats:
                          UUID: %s
                          Name: %s
                          Last Saved: %d
                          Level: %d
                          Total XP: %.2f
                        
                          Base HP: %.2f
                          Base Mana: %.2f
                          Base Stamina: %.2f
                          Base Armor: %.2f
                          Base Power: %.2f
                          Max HP: %.2f
                          Max Mana: %.2f
                          Max Stamina: %.2f
                        
                          Current HP: %.2f
                          Current Mana: %.2f
                          Current Stamina: %.2f
                          Armor: %.2f
                          Power: %.2f
                          AtkSpd: %.2f
                        """,
                playerId, name, last_saved,
                combatLevel, combat_xp,
                base_hp, base_mana, base_stamina, base_armor, base_power,
                maxHP, maxMana, maxStamina,
                currentHP, currentMana, currentStamina,
                armor, power, attackSpeed
        );
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }
    public void setLast_saved(int last_saved) {
        this.last_saved = last_saved;
    }
    public void setCombat_xp(double combat_xp) {
        this.combat_xp = combat_xp;
    }
    public void setCombatLevel(int combatLevel) { this.combatLevel = combatLevel;}
    public void setBase_hp(double base_hp) {
        this.base_hp = base_hp;
    }
    public void setBase_mana(double base_mana) {
        this.base_mana = base_mana;
    }
    public void setBase_stamina(double base_stamina) {
        this.base_stamina = base_stamina;
    }
    public void setBase_armor(double base_armor) {
        this.base_armor = base_armor;
    }
    public void setBase_power(double base_power) {
        this.base_power = base_power;
    }
    public void setBase_hp_regen(double base_hp_regen) { this.base_hp_regen = base_hp_regen; }
    public void setBase_mana_regen(double base_mana_regen) { this.base_mana_regen = base_mana_regen; }
    public void setMaxHP(double maxHP) {
        this.maxHP = maxHP;
    }
    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
    }
    public void setMaxStamina(double maxStamina) {
        this.maxStamina = maxStamina;
    }
    public void setCurrentHP(double currentHP) {
        this.currentHP = currentHP;
    }
    public void setCurrentMana(double currentMana) {
        this.currentMana = currentMana;
    }
    public void setCurrentStamina(double currentStamina) {
        this.currentStamina = currentStamina;
    }
    public void setHpregen(double hpregen) { this.hpregen = hpregen; }
    public void setManaregen(double manaregen) { this.manaregen = manaregen; }
    public void setArmor(double armor) {
        this.armor = armor;
    }
    public void setPower(double power) {
        this.power = power;
    }
    public void setAttackSpeed(double attackSpeed) { this.attackSpeed = attackSpeed; }
    public void setLastAttackTime(long lastAttackTime) { this.lastAttackTime = lastAttackTime; }

    // Getters
    public UUID getPlayerId() {
        return playerId;
    }
    public String getName() {
        return name;
    }
    public int getLast_saved() {
        return last_saved;
    }
    public int getCombatLevel() { return combatLevel; }
    public double getCombat_xp() {
        return combat_xp;
    }
    public double getBase_hp() {
        return base_hp;
    }
    public double getBase_mana() {
        return base_mana;
    }
    public double getBase_stamina() {
        return base_stamina;
    }
    public double getBase_armor() {
        return base_armor;
    }
    public double getBase_power() {
        return base_power;
    }
    public double getBase_hp_regen() { return base_hp_regen; }
    public double getBase_mana_regen() { return base_mana_regen; }
    public double getMaxHP() {
        return maxHP;
    }
    public double getMaxMana() {
        return maxMana;
    }
    public double getMaxStamina() {
        return maxStamina;
    }
    public double getCurrentHP() { return currentHP; }
    public double getCurrentMana() { return currentMana; }
    public double getCurrentStamina() {
        return currentStamina;
    }
    public double getHpregen() { return hpregen; }
    public double getManaregen() { return manaregen; }
    public double getArmor() {
        return armor;
    }
    public double getPower() {
        return power;
    }
    public double getAttackSpeed() { return attackSpeed; }
    public long getLastAttackTime() { return lastAttackTime; }
}