package space.vestiges.plugin1.player;

import org.bukkit.entity.Player;

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
    private double base_haste;

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
    private transient double haste;

    // Template constructor for when player FIRST JOINS
    public PlayerStats(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.level = 1;
        this.total_xp = 0;
        this.base_hp = 100;
        this.base_mana = 100;
        this.base_stamina = 100;
        this.base_armor = 100;
        this.base_power = 0;
        this.base_haste = 0;
        this.last_saved = (int) (System.currentTimeMillis() / 1000L);
    }

    // This constructor is for loading players
    public PlayerStats(String uuid, String name, int last_saved, double total_xp, double base_hp, double base_mana, double base_stamina, double base_armor, double base_power, double base_haste) {
        this.uuid = UUID.fromString(uuid);
        this.name = name;
        this.total_xp = total_xp;
        this.base_hp = base_hp;
        this.base_mana = base_mana;
        this.base_stamina = base_stamina;
        this.base_armor = base_armor;
        this.base_power = base_power;
        this.base_haste = base_haste;
        this.last_saved = last_saved;
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
    public void setBase_haste(double base_haste) {
        this.base_haste = base_haste;
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
    public void setHaste(double haste) {
        this.haste = haste;
    }

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
    public double getBase_haste() {
        return base_haste;
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
    public double getHaste() {
        return haste;
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
}