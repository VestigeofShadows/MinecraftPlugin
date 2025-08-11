package space.vestiges.plugin1.equipment;

import de.tr7zw.nbtapi.NBT;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.vestiges.plugin1.Plugin1;

import java.util.ArrayList;
import java.util.List;

/*
        This class is entirely for returning equipment stats
        Checks if player has equipment
        Calculate the aggregate stats
        Return the aggregate stats
*/
public class EquipmentManager {

    private final Plugin1 plugin = Plugin1.getPlugin(Plugin1.class);

    // Return aggregated stats of equipment
    public EquipmentStats getCombinedStats(Player player) {
        // Make EquipmentStats, with all the stats at 0
        EquipmentStats totalStats = new EquipmentStats();
        // Grab all the equipment
        List<ItemStack> equipment = getAllEquipment(player);

        for (ItemStack item : equipment) {
            EquipmentStats stats = readStatsFromItem(item);
            totalStats.add(stats);
        }
        Plugin1.getInstance().getLogger().info(totalStats.toString());
        return totalStats;
    }

    // Get all equipment
    public List<ItemStack> getAllEquipment(Player player) {
        List<ItemStack> items = new ArrayList<>();

        // Armor
        for (ItemStack armorPiece : player.getInventory().getArmorContents()) {
            if (isValidItem(armorPiece)) {
                items.add(armorPiece);
            }
        }

        // Main hand
        ItemStack main = player.getInventory().getItemInMainHand();
        if (isValidItem(main)) {
            items.add(main);
        }

        // Offhand
        ItemStack off = player.getInventory().getItemInOffHand();
        if (isValidItem(off)) {
            items.add(off);
        }

        return items;
    }

    // /* ------------- Might need in the future ---------------
    // If player has ANY armor equipped
    public boolean hasAnyArmor(Player player) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (isValidItem(item)) {
                return true;
            }
        }
        return false;
    }
    // If player is holding something in main hand
    public boolean hasMainHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        return isValidItem(item);
    }
    // If player is holding something in offhand
    public boolean  hasOffHand(Player player) {
        ItemStack item = player.getInventory().getItemInOffHand();
        return isValidItem(item);
    }
    // ------------- Might need in the future --------------- */

    // (Helper function) Checks if item is valid
    private boolean isValidItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }
    // (Helper function) reading stats from nbt
    private EquipmentStats readStatsFromItem(ItemStack item) {
        EquipmentStats stats;
        double hp = NBT.get(item, nbt -> (Double) nbt.getOrDefault("hp", 0.0));
        double mana = NBT.get(item, nbt -> (Double) nbt.getOrDefault("mana", 0.0));
        double stamina = NBT.get(item, nbt -> (Double) nbt.getOrDefault("stamina", 0.0));
        double armor = NBT.get(item, nbt -> (Double) nbt.getOrDefault("armor", 0.0));
        double power = NBT.get(item, nbt -> (Double) nbt.getOrDefault("power", 0.0));
        double haste = NBT.get(item, nbt -> (Double) nbt.getOrDefault("haste", 0.0));
        stats = new EquipmentStats(hp, mana, stamina, armor, power, haste);
        return stats;
    }
}
