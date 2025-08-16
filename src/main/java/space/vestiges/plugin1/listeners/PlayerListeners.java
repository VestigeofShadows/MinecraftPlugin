package space.vestiges.plugin1.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.vestiges.plugin1.player.PlayerStats;
import space.vestiges.plugin1.player.PlayerStatsManager;
import space.vestiges.plugin1.player.PlayerStatsStorage;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.equipment.EquipmentManager;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;

import java.util.*;

public class PlayerListeners implements Listener{
    private final PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();
    private final PlayerStatsStorage statsStorage = Plugin1.getInstance().getStorageManager();
    private final Set<UUID> playerJoined = new HashSet<>();

    /**
     * This event adds base stats to the joining player only!
     *
     * @param event The PlayerJoinEvent that is used to parse player data
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Plugin1.getInstance().getLogger().info("Player Join Event Occured!");
        // Get player
        Player player = event.getPlayer();

        // Put player in hashset
        playerJoined.add(player.getUniqueId());

        // If player exists in storage, add to active memory, else create it and add it in active and database
        if (statsStorage.playerExists(player)) {
            Plugin1.getInstance().getLogger().info("Player stats exists");

            initialLoadPlayerInfo(player);
        } else { // Creates player and put it in .db, this happens once, and then run load player
            Plugin1.getInstance().getLogger().info("Player stats does not exist");

            // put default into database
            PlayerStats tempStats = new PlayerStats(player);
            statsStorage.addStoredPlayer(player, tempStats);

            // put default into hashmap

            initialLoadPlayerInfo(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get player
        Player player = event.getPlayer();
        Plugin1.getInstance().getLogger().info("Removed " + player.getName() + " from active players memory and hashset");
        statsManager.removeActivePlayer(player);
    }

    /**
     * Checks if entity is player, and then update that player's stats based on equipment.
     *
     * @param event this method is called whenever an entity changes their equipment
     */
    @EventHandler
    public void onEntityEquipmentChange(EntityEquipmentChangedEvent event) {


        // Check if entity is player, ignore all other Equipment change events
        if (!(event.getEntity() instanceof Player)) { return; }
        Plugin1.getInstance().getLogger().info("Player triggered EECE");

        // Get the player
        Player player = (Player) event.getEntity();

        if (playerJoined.contains(player.getUniqueId())) {

            //return;
        }

        // grab the player's playerstat
        PlayerStats current = statsManager.getPlayerInfo(player);

        // make the manager
        EquipmentManager equipmentManager = new EquipmentManager();

        // Check each slot, and update stats
        for (Map.Entry<EquipmentSlot, EntityEquipmentChangedEvent.EquipmentChange> entry : event.getEquipmentChanges().entrySet()) {
            EquipmentSlot slot = entry.getKey();
            EntityEquipmentChangedEvent.EquipmentChange change = entry.getValue();

            ItemStack oldItem = change.oldItem();
            ItemStack newItem = change.newItem();

            equipmentManager.handleItemChange(player, slot, oldItem, newItem);

            // Can use for the future, right now it's tied to toggleflag for debugging
            if(Plugin1.getInstance().toggleflag) {
                switch (slot) {
                    case HAND -> {
                        Plugin1.getInstance().getLogger().info("Mainhand equipment stats changed");
                    }
                    case OFF_HAND -> {
                        Plugin1.getInstance().getLogger().info("Offhand equipment stats changed");
                    }
                    case HEAD -> {
                        Plugin1.getInstance().getLogger().info("Head equipment stats changed");
                    }
                    case CHEST -> {
                        Plugin1.getInstance().getLogger().info("Chest equipment stats changed");
                    }
                    case LEGS -> {
                        Plugin1.getInstance().getLogger().info("Legs equipment stats changed");
                    }
                    case FEET -> {
                        Plugin1.getInstance().getLogger().info("Feet equipment stats changed");
                    }
                    default -> {
                        Plugin1.getInstance().getLogger().warning("Invalid equipment slot " + slot);
                    }
                }
            }
        }

        if (playerJoined.contains(player.getUniqueId())) {
            Plugin1.getInstance().getLogger().info("EECE Player Joined Event, setting current HP/MANA/STAMINA");
            current.setCurrentHP(current.getMaxHP());
            current.setCurrentMana(current.getMaxMana());
            current.setCurrentStamina(current.getMaxStamina());
        }
    }

    // Loads player from database on player join
    public void initialLoadPlayerInfo(Player player) {
        Plugin1.getInstance().getLogger().info("player join event");
        // add stored stats to active stats (base stats)
        statsManager.addActivePlayer(player, statsStorage.getPlayerStoredStats(player));
        // find current player in the hashmap
        PlayerStats currentPlayer = statsManager.getPlayerInfo(player);
        // Load base stats!
        currentPlayer.setAttackSpeed(2.0);
        currentPlayer.setCurrentHP(1.0); // just so you don't insta die lmao
        // Set playerJoined, so EECE can detect
        playerJoined.remove(player.getUniqueId());
    }
    // Only loads Equipment TODO: implement buffs
    public void loadPlayerInfo(Player player) {
        PlayerStats currentPlayer = statsManager.getPlayerInfo(player);
        EquipmentManager equipment = new EquipmentManager();
        currentPlayer.addBaseEquipmentStats(equipment.getCombinedStats(player));
    }
}
