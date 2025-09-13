package space.vestiges.plugin1.domainlayer.equipment;

import org.bukkit.inventory.ItemStack;

public class EquipmentStats2 {

    private double hp;
    private double mana;
    private double stamina;
    private double armor;
    private double power;

    private double weaponBaseAttack;
    private double weaponAttackSpeed;

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack mainhand;
    private ItemStack offhand;

    public EquipmentStats2() {
        this.hp = 0;
        this.mana = 0;
        this.stamina = 0;
        this.armor = 0;
        this.power = 0;

        this.weaponBaseAttack = 1;
        this.weaponAttackSpeed = 2; //todo change this to the right default
    }


}
