package space.vestiges.plugin1.domainlayer.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.domainlayer.equipment.EquipmentManager2;
import space.vestiges.plugin1.domainlayer.player.playerPersistence.PersistenceManager;
import space.vestiges.plugin1.domainlayer.player.playerPersistence.PlayerStatsData;
import space.vestiges.plugin1.domainlayer.utils.BaseStatsCalculation;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsManager {

    private final PersistenceManager pm;
    private final EquipmentManager2 em2;
    private final HashMap<UUID, PlayerData> activePlayers;

    public PlayerStatsManager() {
        pm = new PersistenceManager();
        em2 = new EquipmentManager2();
        activePlayers = new HashMap<>();
    }

    /**
     * This method checks if player exist in the database, and loads it into the active memory if it does.
     * If it doesn't exist, it adds a new copy of into the database, and loads default player.
     * @param player the player to load
     */
    public void addActivePlayer(Player player) {

        if (playerExists(player)) {
            Plugin1.getInstance().getLogger().info("Player stats exists");
            loadActivePlayer(player);
        } else {
            Plugin1.getInstance().getLogger().info("Player stats does not exist");
            saveDefaultPlayer(player);
            loadActivePlayer(player);
        }
    }

    /**
     * use player from memory on leave to save memory (autochecks if it exists)
     * @param player the player to remove from memory
     */
    public void removeActivePlayer(@NotNull Player player) {
        activePlayers.remove(player.getUniqueId());
    }

    /**
     * Calls database to see if player exists already or not
     * @param player the player to check
     * @return boolean of if player exists in database or not
     */
    public boolean playerExists(Player player) {
        return pm.playerExists(player.getUniqueId());
    }

    /**
     * Return playerstats to change
     *
     * @param player the player info to grab
     * @return PlayerStats for the player
     */
    public PlayerStats getPlayerStats(Player player) {
        UUID uuid = player.getUniqueId();
        return activePlayers.get(uuid).getPlayerStats();
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Helper Functions  ---------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Helper function that loads an activePlayer into the hashmap
     * @param player the player to put into the hashmap
     */
    private void loadActivePlayer(Player player) {
        PlayerStatsData DTO = pm.loadPlayer(player.getUniqueId());

        UUID uuid = DTO.getUuid();
        String name = DTO.getPlayername();
        int last_saved = DTO.getLast_saved();
        double total_xp = DTO.getTotal_xp();

        int level = BaseStatsCalculation.getLevelFromTotalXp(total_xp);
        double base_hp = BaseStatsCalculation.getBaseHp(level);
        double base_mana = BaseStatsCalculation.getBaseMana(level);
        double base_stamina = 0;
        double base_armor = 0;
        double base_power = 0;
        double base_hp_regen = BaseStatsCalculation.getBaseHpRegen(level);
        double base_mana_regen = BaseStatsCalculation.getBaseManaRegen(level);

        //todo add initial equipment stats here
        double maxhp = base_hp;
        double maxmana = base_mana;
        double maxstamina = base_stamina;
        double armor = base_armor;
        double power = base_power;
        double hpregen = base_hp_regen;
        double manaregen = base_mana_regen;
        double attackSpeed = 2.0;

        double currentHP = maxhp;
        double currentMana = maxmana;
        double currentStamina = maxstamina;
        long lastAttackTime = System.currentTimeMillis();

        PlayerStats stats = new PlayerStats(
                uuid, name, last_saved, total_xp,
                level, base_hp, base_mana, base_stamina, base_armor, base_power, base_hp_regen, base_mana_regen,
                maxhp, maxmana, maxstamina, armor, power, hpregen, manaregen, attackSpeed,
                currentHP, currentMana, currentStamina, lastAttackTime
        );

        PlayerData data = new PlayerData(uuid, stats);
        activePlayers.put(player.getUniqueId(), data);

    }

    /**
     * Saves a default player stats data into infrastructure layer (persistency)
     * @param player the player to save
     */
    private void saveDefaultPlayer(Player player) {
        PlayerStatsData DTO = new PlayerStatsData(
                player.getUniqueId().toString(),
                player.getName(),
                (int) (System.currentTimeMillis() / 1000L),
                0
        );
        pm.savePlayer(DTO);
    }
}
