package space.vestiges.plugin1.equipment;

public class EquipmentStats {
    private double hp;
    private double mana;
    private double stamina;
    private double armor;
    private double power;
    private double attackSpeed;

    /**
     * This constructor returns all EquipmentStats as 0s
     */
    public EquipmentStats() {
        this.hp = 0;
        this.mana = 0;
        this.stamina = 0;
        this.armor = 0;
        this.power = 0;
        this.attackSpeed = 0;
    }

    /**
     * Constructor to create an EquipmentStats with specified values.
     *
     * @param hp set EquipmentStat's hp to this value
     * @param mana set EquipmentStat's mana to this value
     * @param stamina set EquipmentStat's stamina to this value
     * @param armor set EquipmentStat's armor to this value
     * @param power set EquipmentStat's power to this value
     * @param attackSpeed set EquipmentStat's attackSpeed to this value
     */
    EquipmentStats(double hp, double mana, double stamina, double armor, double power, double attackSpeed) {
        this.hp = hp;
        this.mana = mana;
        this.stamina = stamina;
        this.armor = armor;
        this.power = power;
        this.attackSpeed = attackSpeed;
    }

    /**
     * Add another equipmentStats to this equipmentStats (update logic)
     * thisEquipmentStats.add(values to add)
     *
     * @param stats EquipmentStats type
     */
    public void add(EquipmentStats stats) {
        this.hp += stats.getHp();
        this.mana += stats.getMana();
        this.stamina += stats.getStamina();
        this.armor += stats.getArmor();
        this.power += stats.getPower();
        this.attackSpeed += stats.getAttackSpeed();
    }

    /**
     * Prints the entire EquipmentStats object as String
     * used for debugging
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(
                """
                        EquipmentStats:
                          HP: %.2f
                          Mana: %.2f
                          Stamina: %.2f
                          Armor: %.2f
                          Power: %.2f
                          attackSpeed: %.2f""",
                hp, mana, stamina, armor, power, attackSpeed
        );
    }

    //getters
    public double getStamina() {
        return stamina;
    }
    public double getHp() {
        return hp;
    }
    public double getMana() {
        return mana;
    }
    public double getArmor() {
        return armor;
    }
    public double getPower() {
        return power;
    }
    public double getAttackSpeed() {
        return attackSpeed;
    }

    //setters
    public void setHp(double hp) {
        this.hp = hp;
    }
    public void setMana(double mana) {
        this.mana = mana;
    }
    public void setStamina(double stamina) {
        this.stamina = stamina;
    }
    public void setArmor(double armor) {
        this.armor = armor;
    }
    public void setPower(double power) {
        this.power = power;
    }
    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
}
