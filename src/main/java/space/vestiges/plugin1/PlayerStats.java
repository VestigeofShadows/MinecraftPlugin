package space.vestiges.plugin1;

public class PlayerStats {

    // Base is used to read from json and initializing json
    private String name;
    private double baseLevel = 1;
    private double baseHP = 100;
    private double baseMana = 100;
    private double baseStamina = 100;
    private double baseArmor = 0;
    private double baseDamage = 1;

    // Used in memory for calculations
    private transient double maxHP;
    private transient double maxMana;
    private transient double maxStamina;
    private transient double maxArmor;
    private transient double maxDamage;

    // These need to be reupdated
    private double currentHP = baseHP;
    private double currentMana = baseMana;
    private double currentStamina =  baseStamina;
    private double currentArmor = baseArmor;
    private double currentDamage =  baseDamage;

    public PlayerStats(String name) {
        this.name = name;
    }
    // Setters
    public void setName(String name) { this.name = name;}
    public void setBaseLevel(double baseLevel) {
        this.baseLevel = baseLevel;
    }
    public void setBaseHP(double baseHP) {
        this.baseHP = baseHP;
    }
    public void setBaseMana(double baseMana) {
        this.baseMana = baseMana;
    }
    public void setBaseStamina(double baseStamina) {
        this.baseStamina = baseStamina;
    }
    public void setBaseArmor(double baseArmor) {
        this.baseArmor = baseArmor;
    }
    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
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
    public void setMaxArmor(double maxArmor) {
        this.maxArmor = maxArmor;
    }
    public void setMaxDamage(double maxDamage) {
        this.maxDamage = maxDamage;
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
    public void setCurrentArmor(double currentArmor) {
        this.currentArmor = currentArmor;
    }
    public void setCurrentDamage(double currentDamage) {
        this.currentDamage = currentDamage;
    }

    // Getters
    public String getName() { return name;}
    public double getBaseLevel() {
        return baseLevel;
    }
    public double getBaseHP() {
        return baseHP;
    }
    public double getBaseMana() {
        return baseMana;
    }
    public double getBaseStamina() {
        return baseStamina;
    }
    public double getBaseArmor() {
        return baseArmor;
    }
    public double getBaseDamage() {
        return baseDamage;
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
    public double getMaxArmor() {
        return maxArmor;
    }
    public double getMaxDamage() {
        return maxDamage;
    }
    public double getCurrentHP() {
        return currentHP;
    }
    public double getCurrentMana() {
        return currentMana;
    }
    public double getCurrentStamina() {
        return currentStamina;
    }
    public double getCurrentArmor() {
        return currentArmor;
    }
    public double getCurrentDamage() {
        return currentDamage;
    }
}