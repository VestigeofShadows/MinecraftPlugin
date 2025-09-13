package space.vestiges.plugin1.domainlayer.equipment;

import de.tr7zw.nbtapi.NBT;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.domainlayer.player.PlayerStats;
import space.vestiges.plugin1.domainlayer.player.PlayerStatsManager;

/**
 * This class returns equipment stats
 * Checks if player has equipment
 * Calculate aggregate stats to return
 * Uses the stats in playerstatsmanager
 */
public class EquipmentManager {

    /**
     * Updates a player's stats based on a singular equipment change
     *
     * @param player the player's stats to change
     * @param newItem the new item ItemStack
     */
    public void handleItemChange(Player player, EquipmentSlot slot, ItemStack newItem) {

        PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();
        PlayerStats playerStats = statsManager.getPlayerStats(player);
        EquipmentStats oldCachedStats = playerStats.getCachedStats(slot); //0s if old item is not valid
        EquipmentStats newStats;

        // read new stats only if it matches
        if (matchesSlot(newItem, slot)) {
            playerStats.subtractEquipmentStats(oldCachedStats);
            newStats = readStatsFromItem(newItem);

            playerStats.addEquipmentStats(newStats);
            playerStats.normalizeStats();

            playerStats.setCachedStats(slot, newStats); //set the cache
        } else { //remove old stats
            playerStats.subtractEquipmentStats(oldCachedStats);
            playerStats.normalizeStats();

            playerStats.clearCachedStats(slot);         //clear the cache
        }
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     HELPER FUNCTIONS     -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * (Helper Function) Used to check if an item is valid or not.
     *
     * @param item check if this item is valid
     * @return true if item is not null and not air
     */
    private boolean isValidItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    /**
     * This function checks if item type matches the slot type (helmet needs to be in the helmet slot)
     * also returns false if item isn't valid
     * todo add new types of valid items
     * @param item the item to check
     * @param slot the slot to check
     * @return true if they match, false if they don't match
     */
    private boolean matchesSlot(ItemStack item, EquipmentSlot slot) {
        if (!isValidItem(item)) return false;

        Material type = item.getType();
        switch (slot) {
            case HEAD:
                if (type.name().endsWith("_HELMET") ||
                    type == Material.CARVED_PUMPKIN ||
                    type == Material.PLAYER_HEAD) {
                    Plugin1.getInstance().getLogger().info("valid helmet");
                    return true;
                } else {
                    Plugin1.getInstance().getLogger().info("invalid helmet");
                    return false;
                }
            case CHEST:
                if (type.name().endsWith("_CHESTPLATE") ||
                    type == Material.ELYTRA) {
                    Plugin1.getInstance().getLogger().info("valid chestplate");
                    return true;
                } else {
                    Plugin1.getInstance().getLogger().info("invalid chestplate");
                    return false;
                }
            case LEGS:
                if (type.name().endsWith("_LEGGINGS")) {
                    Plugin1.getInstance().getLogger().info("valid leggings");
                    return true;
                } else {
                    Plugin1.getInstance().getLogger().info("invalid leggings");
                    return false;
                }
            case FEET:
                if (type.name().endsWith("_BOOTS")) {
                    Plugin1.getInstance().getLogger().info("valid boots");
                    return true;
                } else {
                    Plugin1.getInstance().getLogger().info("invalid boots");
                    return false;
                }
            case HAND:
                // allow only valid weapon/tools that might have nbt data
                if (isWeaponOrTool(type)) {
                    Plugin1.getInstance().getLogger().info("valid weapon/tool");
                    return true;
                } else {
                    Plugin1.getInstance().getLogger().info("invalid weapon/tool");
                    return false;
                }
            case OFF_HAND:
                if (isWeaponOrTool(type)) {
                    Plugin1.getInstance().getLogger().info("valid offhand weapon/tool");
                    return true;
                } else {
                    Plugin1.getInstance().getLogger().info("invalid offhand weapon/tool");
                    return false;
                }
            default:
                return false;
        }
    }

    /**
     * Detect if a held item is a weapon or tool
     * Helper function for matchesSlot
     * todo: CHANGE THIS TO ADD NEW VALID TYPES
     * @param type the type of item to check
     * @return boolean
     */
    private static boolean isWeaponOrTool(Material type) {
        String name = type.name();
        return name.endsWith("_SWORD") ||
                name.endsWith("_AXE") ||
                name.endsWith("_PICKAXE") ||
                name.endsWith("_SHOVEL") ||
                name.endsWith("_HOE") ||
                type == Material.TRIDENT ||
                type == Material.BOW ||
                type == Material.CROSSBOW ||
                type == Material.SHIELD;
    }

    /**
     * (Helper Function) Used to read stats from a single equipment's nbt values
     * reading stats from nbt, and returning EquipmentStats
     * @param item ItemStack
     * @return EquipmentStats
     */
    private EquipmentStats readStatsFromItem(ItemStack item) {
        EquipmentStats stats;
        double hp = NBT.get(item, nbt -> (Double) nbt.getOrDefault("eqHp", 0.0));
        double mana = NBT.get(item, nbt -> (Double) nbt.getOrDefault("eqMana", 0.0));
        double stamina = NBT.get(item, nbt -> (Double) nbt.getOrDefault("eqStamina", 0.0));
        double armor = NBT.get(item, nbt -> (Double) nbt.getOrDefault("eqArmor", 0.0));
        double power = NBT.get(item, nbt -> (Double) nbt.getOrDefault("eqPower", 0.0));
        double attackSpeed = NBT.get(item, nbt -> (Double) nbt.getOrDefault("eqAtkspd", 0.0));
        stats = new EquipmentStats(hp, mana, stamina, armor, power, attackSpeed);
        return stats;
    }
}
