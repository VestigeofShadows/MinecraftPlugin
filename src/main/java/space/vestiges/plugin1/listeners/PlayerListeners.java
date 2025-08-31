package space.vestiges.plugin1.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import space.vestiges.plugin1.player.PlayerHud;
import space.vestiges.plugin1.player.PlayerStats;
import space.vestiges.plugin1.player.PlayerStatsManager;
import space.vestiges.plugin1.player.PlayerStatsStorage;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.equipment.EquipmentManager;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import space.vestiges.plugin1.utils.BaseStatsCalculation;

import java.util.*;

public class PlayerListeners implements Listener{


    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------- classes/variables Initialization ----------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    private final PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();
    private final PlayerStatsStorage statsStorage = Plugin1.getInstance().getStorageManager();
    private final PlayerHud playerHud= Plugin1.getInstance().getPlayerHud();
    private final Set<UUID> playerJoined = new HashSet<>();


    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Class Functions  ----------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

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

        // If player exists in storage, add to active memory
        if (statsStorage.playerExists(player)) {
            if (Plugin1.getInstance().toggleflag) Plugin1.getInstance().getLogger().info("Player stats exists");

            initialLoadPlayerInfo(player);
        } else { // Creates player and put it in .db, this happens once, and then run load player
            if (Plugin1.getInstance().toggleflag)Plugin1.getInstance().getLogger().info("Player stats does not exist");

            // put create and put default into database
            statsStorage.addStoredPlayer(player, new PlayerStats(player));

            // put default into hashmap
            initialLoadPlayerInfo(player);
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
        playerJoined.remove(player.getUniqueId());
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

    //todo A way to detect mana change, and updatehud

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     HELPER FUNCTIONS     -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

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
        if (Plugin1.getInstance().toggleflag) Plugin1.getInstance().getLogger().info("player join event, initial load player info called");

        // Load from database
        statsManager.addActivePlayer(player, statsStorage.getPlayerStoredStats(player));

        // Load all stats
        PlayerStats currentPlayer = statsManager.getPlayerInfo(player);
        // uuid, name, last_saved, total_xp already loaded
        int combatLevel = BaseStatsCalculation.getLevelFromTotalXp(currentPlayer.getCombat_xp());
        currentPlayer.setCombatLevel(combatLevel);
        double basehp = BaseStatsCalculation.getBaseHp(combatLevel);
        currentPlayer.setBase_hp(basehp);
        double basemana = BaseStatsCalculation.getBaseMana(combatLevel);
        currentPlayer.setBase_mana(basemana);
        double basestamina = 0;
        currentPlayer.setBase_stamina(basestamina);
        double basearmor = 0;
        currentPlayer.setBase_armor(basearmor);
        double basepower = 0;
        currentPlayer.setBase_power(basepower);

        double maxHP = basehp;
        currentPlayer.setMaxHP(maxHP);
        double maxMana = basemana;
        currentPlayer.setMaxMana(maxMana);
        double maxStamina = basestamina;
        currentPlayer.setMaxStamina(maxStamina);

        double currentHP = maxHP;
        currentPlayer.setCurrentHP(currentHP);
        double currentMana = maxMana;
        currentPlayer.setCurrentMana(currentMana);
        double currentStamina = maxStamina;
        currentPlayer.setCurrentStamina(currentStamina);
        double armor = basearmor;
        currentPlayer.setBase_armor(armor);
        double power = basepower;
        currentPlayer.setBase_power(power);

        double attackSpeed = 2.0;
        currentPlayer.setAttackSpeed(attackSpeed);
        long lastAttackTime = System.currentTimeMillis();
        currentPlayer.setLastAttackTime(lastAttackTime);

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
