package space.vestiges.plugin1.adapterlayer.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.vestiges.plugin1.adapterlayer.schedulers.PlayerHud;
import space.vestiges.plugin1.domainlayer.model.player.PlayerStats;
import space.vestiges.plugin1.applicationlayer.PlayerStatsManager;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.applicationlayer.EquipmentManager;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;

import java.util.*;

public class PlayerListener implements Listener{


    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------- classes/variables Initialization ----------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    private final PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();
    private final PlayerHud playerHud = Plugin1.getInstance().getPlayerHud();


    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Class Functions  ----------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * This event adds base stats to the joining player!
     *
     * @param event The PlayerJoinEvent that is used to parse player data
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (Plugin1.getInstance().toggleflag) Plugin1.getInstance().getLogger().info("Player Join Event Occured!");
        Player player = event.getPlayer();

        // If player exists in storage, add to active memory
        if (statsManager.playerExists(player.getUniqueId())) {
            Plugin1.getInstance().getLogger().info("Player stats exists");
            statsManager.addActivePlayer(player);
        } else { // Creates player and put it in .db, this happens once, and then run load player
            Plugin1.getInstance().getLogger().info("Player stats does not exist");
            // put create and put default into database
            statsManager.saveDefaultPlayer(player);
            statsManager.addActivePlayer(player);
        }
    }

    /**
     * Remove player from active memory when they leave the server
     * @param event PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get player
        Player player = event.getPlayer();
        Plugin1.getInstance().getLogger().info("Removed " + player.getName() + " from active players memory and hashset");
        statsManager.removeActivePlayer(player);

    }

    /**
     * Update player hud whenever player is damaged
     * @param event EntityDamageEvent
     */
    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        playerHud.updateHud(player);
    }

    /**
     * Checks if entity is player, and then update that player's stats based on equipment.
     *
     * @param event this method is called whenever an entity changes their equipment
     */
    @EventHandler
    public void onEntityEquipmentChange(EntityEquipmentChangedEvent event) {

        // Check if entity is player, ignore all other Equipment change events
        if (!(event.getEntity() instanceof Player player)) { return; }
        Plugin1.getInstance().getLogger().info("Player triggered EECE");

        // grab the player's playerstat
        PlayerStats current = statsManager.getPlayerInfo(player);

        // make the manager
        EquipmentManager equipmentManager = new EquipmentManager();

        // Check each slot, and update stats
        for (Map.Entry<EquipmentSlot, EntityEquipmentChangedEvent.EquipmentChange> entry : event.getEquipmentChanges().entrySet()) {
            EquipmentSlot slot = entry.getKey();
            EntityEquipmentChangedEvent.EquipmentChange change = entry.getValue();

            ItemStack newItem = change.newItem();

            equipmentManager.handleItemChange(player, slot, newItem);

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
    }

    //todo A way to detect mana change, and updatehud

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     HELPER FUNCTIONS     -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

}
