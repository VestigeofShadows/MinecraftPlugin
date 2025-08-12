package space.vestiges.plugin1.equipment;

import de.tr7zw.nbtapi.NBT;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.player.PlayerStats;
import space.vestiges.plugin1.player.PlayerStatsManager;

import java.util.ArrayList;
import java.util.List;

/*
        This class is entirely for returning equipment stats
        Checks if player has equipment
        Calculate the aggregate stats
        Return the aggregate stats
        Use the aggregate stats in PlayerStatsManager
*/
public class EquipmentManager {

    private final Plugin1 plugin = Plugin1.getPlugin(Plugin1.class);

    /**
     * Return aggregated stats of equipment
     * TODO: Change this method / Update it
     *
     * @param player updates a player's stats based on all of its equipments
     * @return return a EquipmentStats with all the combined stats
     */
    public EquipmentStats getCombinedStats(Player player) {
        // Make EquipmentStats, with all the stats at 0
        EquipmentStats totalStats = new EquipmentStats();

        // Grab all the equipment
        List<ItemStack> equipment = getAllEquipment(player);

        for (ItemStack item : equipment) {
            EquipmentStats stats = readStatsFromItem(item);
            totalStats.add(stats);
        }
        return totalStats;
    }

    /**
     * This method returns an EquipmentStats with item mainhand stats
     * defaults attackspeed to 2
     *
     * @param player the player to grab from
     * @return EquipmentStats to use (attackspeed defaults to 2)
     */
    public EquipmentStats getMainhand(Player player) {
        // Initialize EquipmentStats, with all the stats at 0
        EquipmentStats totalStats = new EquipmentStats();
        ItemStack mainhandItem = hasMainHand(player);
        if (mainhandItem != null) {
            totalStats.add(readStatsFromItem(mainhandItem));
            totalStats.setAttackSpeed(2.0);
        } else {
            //This should never happen...?
            Plugin1.getInstance().getLogger().warning("Main hand is null - EquipmentManager");
        }
        return totalStats;
    }

    /**
     * This method returns an EquipmentStats with item mainhand stats
     * defaults attackspeed to 2
     *
     * @param player the player to grab from
     * @return EquipmentStats to use (attackspeed defaults to 2)
     */
    public EquipmentStats getOffhand(Player player) {
        // Initialize EquipmentStats, with all the stats at 0
        EquipmentStats totalStats = new EquipmentStats();
        ItemStack offhandItem = hasOffHand(player);
        if (offhandItem != null) {
            totalStats.add(readStatsFromItem(offhandItem));
        } else {
            //This should never happen...?
            Plugin1.getInstance().getLogger().warning("Off hand is null - EquipmentManager");
        }
        return totalStats;
    }

    /**
     * (Helper Function) Grab all equipment from a player and return it as a list of ItemStack
     *
     * @param player grab this player's equipment
     * @return a list of ItemStacks from player
     */
    public List<ItemStack> getAllEquipment(@NotNull Player player) {
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

    /**
     * (Helper Function) Grabs a player's mainhand item
     *
     * @param player checks this player's mainhand item
     * @return null if item is not valid, otherwise return the item
     */
    public ItemStack hasMainHand(Player player) {
        if(isValidItem(player.getInventory().getItemInMainHand())) {
            return player.getInventory().getItemInMainHand();
        }
        else {
            return null;
        }
    }

    /**
     * (Helper Function) Grabs a player's offhand item
     *
     * @param player checks this player's offhand item
     * @return null if the item is not valid, otherwise return the item
     */
    public ItemStack hasOffHand(Player player) {
        if(isValidItem(player.getInventory().getItemInOffHand())) {
            return player.getInventory().getItemInOffHand();
        }
        else {
            return null;
        }
    }

    /**
     * (Helper Function) Grabs a player's armor
     *
     * @param player check this player's armor
     * @return return all items in this guy's inventory
     */
    public List<ItemStack> getAllArmor(Player player) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack armorPiece : player.getInventory().getArmorContents()) {
            if (isValidItem(armorPiece)) {
                items.add(armorPiece);
            }
        }
        return items;
    }

    /**
     * (Helper Function) Used to check if an item is valid or not.
     *
     * @param item check if this item is valid
     * @return true if item is not null and not air
     */
    public boolean isValidItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    /**
     * (Helper Function) Used to read stats from a single equipment's nbt values
     * reading stats from nbt, and returning EquipmentStats
     * @param item ItemStack
     * @return EquipmentStats
     */
    public static EquipmentStats readStatsFromItem(ItemStack item) {
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

    /**
     * Updates a player's stats based on a singular equipment change
     *
     * @param player the player's stats to change
     * @param oldItem the old item ItemStack
     * @param newItem the new item ItemStack
     */
    public void handleItemChange(Player player, EquipmentSlot slot, ItemStack oldItem, ItemStack newItem) {

        PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();
        PlayerStats playerStats = statsManager.getPlayerInfo(player);

        EquipmentStats oldCachedStats = playerStats.getCachedStats(slot);
        EquipmentStats newStats;

        // Subtract cached old stats
        playerStats.subtractEquipmentStats(oldCachedStats);

        // Parse new stats only once if newItem valid
        if (isValidItem(newItem)) {
            newStats = readStatsFromItem(newItem);
        } else {
            newStats = new EquipmentStats();
        }

        // Add new stats
        playerStats.addEquipmentStats(newStats);

        // Update cache
        playerStats.setCachedStats(slot, newStats);

        // Normalize at the end
        playerStats.normalizeStats();

        /*
        boolean oldValid = oldItem != null && oldItem.getType() != Material.AIR;
        boolean newValid = newItem != null && newItem.getType() != Material.AIR;

        if (!oldValid && !newValid) {
            return; // Nothing changed
        }
        if (oldValid && !newValid) {
            // Removed
            EquipmentStats oldStats = readStatsFromItem(oldItem);
            playerStats.subtractEquipmentStats(oldStats);
            playerStats.normalizeStats();
        }
        else if (!oldValid && newValid) {
            // Added
            EquipmentStats newStats = readStatsFromItem(newItem);
            playerStats.addEquipmentStats(newStats);
            playerStats.normalizeStats();
        }
        else {
            // Swapped
            EquipmentStats oldStats = readStatsFromItem(oldItem);
            EquipmentStats newStats = readStatsFromItem(newItem);
            playerStats.subtractEquipmentStats(oldStats);
            playerStats.addEquipmentStats(newStats);
            playerStats.normalizeStats();
        }
        */
    }
}
