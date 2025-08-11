package space.vestiges.plugin1.equipment;

public class EquipmentStats {
    private double hp;
    private double mana;
    private double stamina;
    private double armor;
    private double power;
    private double haste;

    //constructor
    EquipmentStats() {
        this.hp = 0;
        this.mana = 0;
        this.stamina = 0;
        this.armor = 0;
        this.power = 0;
        this.haste = 0;
    }
    EquipmentStats(double hp, double mana, double stamina, double armor, double power, double haste) {
        this.hp = hp;
        this.mana = mana;
        this.stamina = stamina;
        this.armor = armor;
        this.power = power;
        this.haste = haste;
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
    public double getHaste() {
        return haste;
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
    public void setHaste(double haste) {
        this.haste = haste;
    }

    //adders
    public void add(EquipmentStats stats) {
        this.hp += stats.getHp();
        this.mana += stats.getMana();
        this.stamina += stats.getStamina();
        this.armor += stats.getArmor();
        this.power += stats.getPower();
        this.haste += stats.getHaste();
    }

    //toString for debug
    @Override
    public String toString() {
        return String.format(
                "EquipmentStats:\n" +
                        "  HP: %.2f\n" +
                        "  Mana: %.2f\n" +
                        "  Stamina: %.2f\n" +
                        "  Armor: %.2f\n" +
                        "  Power: %.2f\n" +
                        "  Haste: %.2f",
                hp, mana, stamina, armor, power, haste
        );
    }
}
