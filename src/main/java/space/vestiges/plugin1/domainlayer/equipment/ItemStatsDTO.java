package space.vestiges.plugin1.domainlayer.equipment;

public class ItemStatsDTO {
    private double hp = 0;
    private double hp_regen;
    private double mana = 0;
    private double mana_regen;
    private double stamina = 0;
    private double stamina_regen;
    private double crit_chance;
    private double crit_damage;
    private double armor = 0;
    private double power = 0;
    private double weaponBaseAttack = 0;
    private double weaponAttackSpeed = 0;

    public ItemStatsDTO() {}
    public ItemStatsDTO(double hp, double hp_regen, double mana, double mana_regen, double stamina, double stamina_regen, double crit_chance, double crit_damage, double armor, double power, double weaponBaseAttack, double weaponAttackSpeed) {
        this.hp = hp;
        this.hp_regen = hp_regen;
        this.mana = mana;
        this.mana_regen = mana_regen;
        this.stamina = stamina;
        this.stamina_regen = stamina_regen;
        this.crit_chance = crit_chance;
        this.crit_damage = crit_damage;
        this.armor = armor;
        this.power = power;
        this.weaponBaseAttack = weaponBaseAttack;
        this.weaponAttackSpeed = weaponAttackSpeed;
    }
    public double getHp() {
        return hp;
    }
    public double getHp_regen() {
        return hp_regen;
    }
    public double getMana() {
        return mana;
    }
    public double getMana_regen() {
        return mana_regen;
    }
    public double getStamina() {
        return stamina;
    }
    public double getStamina_regen() {
        return stamina_regen;
    }
    public double getCrit_chance() {
        return crit_chance;
    }
    public double getCrit_damage() {
        return crit_damage;
    }
    public double getArmor() {
        return armor;
    }
    public double getPower() {
        return power;
    }
    public double getWeaponBaseAttack() {
        return weaponBaseAttack;
    }
    public double getWeaponAttackSpeed() {
        return weaponAttackSpeed;
    }
}
