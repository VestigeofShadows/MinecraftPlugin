package space.vestiges.plugin1.player;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.equipment.EquipmentStats;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStats {

    // Base is used to read from json and initializing json
    private UUID uuid;
    private String name;
    private int last_saved;

    private int level;
    private double total_xp;
    private double base_hp;
    private double base_mana;
    private double base_stamina;
    private double base_armor;
    private double base_power;

    // Max stats for display
    private transient double maxHP;
    private transient double maxMana;
    private transient double maxStamina;

    // Current stats for calculations
    private transient double currentHP;
    private transient double currentMana;
    private transient double currentStamina;
    private transient double armor;
    private transient double power;

    // For attack cooldown
    private transient double attackSpeed;  //determines how fast the player can attack (attacks per second)
    private transient long lastAttackTime; //The tick or millisecond timestamp of last successful attack

    // For cached stats
    private final Map<EquipmentSlot, EquipmentStats> equipmentStatsCache = new EnumMap<>(EquipmentSlot.class);

    // Accessor to get cached stats for a slot
    public EquipmentStats getCachedStats(EquipmentSlot slot) {
        // Default is all 0
        return equipmentStatsCache.getOrDefault(slot, new EquipmentStats());
    }

    public void setCachedStats(EquipmentSlot slot, EquipmentStats stats) {
        equipmentStatsCache.put(slot, stats);
    }

    // Template constructor for storage only! when player FIRST JOINS!
    public PlayerStats(@NotNull Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.total_xp = 0;
        this.base_hp = 100;
        this.base_mana = 100;
        this.base_stamina = 100;
        this.base_armor = 0;
        this.base_power = 0;
        this.last_saved = (int) (System.currentTimeMillis() / 1000L);
    }

    // This constructor is for loading players in general (divide this into armor / mainhand later)
    public PlayerStats(String uuid, String name, int last_saved, double total_xp, double base_hp, double base_mana, double base_stamina, double base_armor, double base_power) {
        this.uuid = UUID.fromString(uuid);
        this.name = name;
        this.total_xp = total_xp;
        this.base_hp = base_hp;
        this.base_mana = base_mana;
        this.base_stamina = base_stamina;
        this.base_armor = base_armor;
        this.base_power = base_power;
        this.last_saved = last_saved;
    }

    // Change
    public void addBaseEquipmentStats(@NotNull EquipmentStats stats) {
        this.maxHP = base_hp + stats.getHp();
        this.maxMana = base_mana + stats.getMana();
        this.maxStamina = base_stamina + stats.getStamina();
        this.armor = base_armor + stats.getArmor();
        this.power = base_power + stats.getPower();
        this.attackSpeed = attackSpeed + stats.getAttackSpeed();
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
                uuid, name, last_saved,
                level, total_xp,
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
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setLast_saved(int last_saved) {
        this.last_saved = last_saved;
    }
    public void setLevel(int level) { this.level = level;}
    public void setTotal_xp(double total_xp) {
        this.total_xp = total_xp;
    }
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
    public void setArmor(double armor) {
        this.armor = armor;
    }
    public void setPower(double power) {
        this.power = power;
    }
    public void setAttackSpeed(double attackSpeed) { this.attackSpeed = attackSpeed; }
    public void setLastAttackTime(long lastAttackTime) { this.lastAttackTime = lastAttackTime; }

    // Getters
    public String getName() {
        return name;
    }
    public int getLast_saved() {
        return last_saved;
    }
    public int getLevel() { return level; }
    public double getTotal_xp() {
        return total_xp;
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
    public double getMaxHP() {
        return maxHP;
    }
    public double getMaxMana() {
        return maxMana;
    }
    public double getMaxStamina() {
        return maxStamina;
    }
    public double getCurrentMana() {
        return currentMana;
    }
    public double getCurrentStamina() {
        return currentStamina;
    }
    public double getArmor() {
        return armor;
    }
    public double getPower() {
        return power;
    }
    public double getCurrentHP() {
        return currentHP;
    }
    public UUID getUuid() {
        return uuid;
    }
    public double getAttackSpeed() { return attackSpeed; }
    public long getLastAttackTime() { return lastAttackTime; }
}