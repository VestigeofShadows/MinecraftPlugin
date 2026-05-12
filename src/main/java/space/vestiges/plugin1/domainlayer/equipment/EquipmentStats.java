package space.vestiges.plugin1.domainlayer.equipment;

public class EquipmentStats {

    private double hp;
    private double hp_regen;
    private double mana;
    private double mana_regen;
    private double stamina;
    private double stamina_regen;
    private double crit_chance;
    private double crit_damage;
    private double armor;
    private double power;
    private double weaponBaseAttack;
    private double weaponAttackSpeed;

    // Constructor
    public EquipmentStats() {
        this.hp = 0;
        this.hp_regen = 0;
        this.mana = 0;
        this.mana_regen = 0;
        this.stamina = 0;
        this.stamina_regen = 0;
        this.crit_chance = 0;
        this.crit_damage = 0;
        this.armor = 0;
        this.power = 0;
        this.weaponBaseAttack = 1;
        this.weaponAttackSpeed = 2;
    }

    // Mutator
    public void add(ItemStatsDTO dto) {
        this.hp += dto.getHp();
        this.hp_regen += dto.getHp_regen();
        this.mana += dto.getMana();
        this.mana_regen += dto.getMana_regen();
        this.stamina += dto.getStamina();
        this.stamina_regen += dto.getStamina_regen();
        this.crit_chance += dto.getCrit_chance();
        this.crit_damage += dto.getCrit_damage();
        this.armor += dto.getArmor();
        this.power += dto.getPower();
        this.weaponBaseAttack += dto.getWeaponBaseAttack();
        this.weaponAttackSpeed += dto.getWeaponAttackSpeed();
        normalizeStats();
    }
    public void sub(ItemStatsDTO dto) {
        this.hp -= dto.getHp();
        this.hp_regen -= dto.getHp_regen();
        this.mana -= dto.getMana();
        this.mana_regen -= dto.getMana_regen();
        this.stamina -= dto.getStamina();
        this.stamina_regen -= dto.getStamina_regen();
        this.crit_chance -= dto.getCrit_chance();
        this.crit_damage -= dto.getCrit_damage();
        this.armor -= dto.getArmor();
        this.power -= dto.getPower();
        this.weaponBaseAttack -= dto.getWeaponBaseAttack();
        this.weaponAttackSpeed -= dto.getWeaponAttackSpeed();
        normalizeStats();
    }

    // Getter
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

    // Helper, normalize values
    private void normalizeStats() {
        this.hp = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.hp_regen = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.mana = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.mana_regen = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.stamina = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.stamina_regen = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.crit_chance = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.crit_damage = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.armor = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.power = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.weaponBaseAttack = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
        this.weaponAttackSpeed = roundToDecimals(clampToZeroIfTiny(this.hp), 3);
    }
    private double clampToZeroIfTiny(double val) {
        return Math.abs(val) < 1e-10 ? 0.0 : val;
    }
    private double roundToDecimals(double val, int decimals) {
        double scale = Math.pow(10, decimals);
        return Math.round(val * scale) / scale;
    }
}
