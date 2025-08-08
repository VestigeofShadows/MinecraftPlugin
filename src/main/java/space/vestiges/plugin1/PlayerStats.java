package space.vestiges.plugin1;

public class PlayerStats {

    // Base is only used for initializing Json / Read from
    private double baseLevel = 1;
    private double baseHP = 100;
    private double baseMana = 100;
    private double baseStamina = 100;
    private double baseArmor = 0;
    private double baseDamage = 1;

    // Used in memory for calculations
    private double maxHP;
    private double maxMana;
    private double maxStamina;
    private double maxArmor;
    private double maxDamage;

    private double currentHP;
    private double currentMana;
    private double currentStamina;
    private double currentArmor;
    private double currentDamage;

    private double bonusHP;
    private double bonusMP;
    private double bonusStamina;
    private double bonusArmor;
    private double bonusDamage;

    // Setters
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
    public void setBonusHP(double bonusHP) {
        this.bonusHP = bonusHP;
    }
    public void setBonusMP(double bonusMP) {
        this.bonusMP = bonusMP;
    }
    public void setBonusStamina(double bonusStamina) {
        this.bonusStamina = bonusStamina;
    }
    public void setBonusArmor(double bonusArmor) {
        this.bonusArmor = bonusArmor;
    }
    public void setBonusDamage(double bonusDamage) {
        this.bonusDamage = bonusDamage;
    }

    // Getters
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
    public double getBonusHP() {
        return bonusHP;
    }
    public double getBonusMP() {
        return bonusMP;
    }
    public double getBonusStamina() {
        return bonusStamina;
    }
    public double getBonusArmor() {
        return bonusArmor;
    }
    public double getBonusDamage() {
        return bonusDamage;
    }
}