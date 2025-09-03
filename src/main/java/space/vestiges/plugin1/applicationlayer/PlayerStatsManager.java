package space.vestiges.plugin1.applicationlayer;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.applicationlayer.persistentdata.PersistenceManager;
import space.vestiges.plugin1.applicationlayer.persistentdata.PlayerStatsData;
import space.vestiges.plugin1.domainlayer.model.player.PlayerStats;
import space.vestiges.plugin1.domainlayer.utils.BaseStatsCalculation;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsManager {

    private final PersistenceManager pm;
    private final HashMap<UUID, PlayerStats> activePlayers;

    public PlayerStatsManager() {
        pm = new PersistenceManager();
        activePlayers = new HashMap<>();
    }

    public void addActivePlayer(Player player) {

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

        activePlayers.put(player.getUniqueId(), stats);
    }

    // use player from memory on leave to save memory
    public void removeActivePlayer(@NotNull Player player) {
        activePlayers.remove(player.getUniqueId());
    }

    /**
     * Saves new xp value into database
     * @param player the player to update xp value for
     */
    public void saveActivePlayer(@NotNull Player player) {
        PlayerStats stats = activePlayers.get(player.getUniqueId());
        PlayerStatsData DTO = new PlayerStatsData(stats.getUuid().toString(), stats.getName(), stats.getLast_saved(), stats.getCombat_xp());
        pm.savePlayer(DTO);
    }
    /**
     * Return playerstats to change
     *
     * @param player the player info to grab
     * @return PlayerStats for the player
     */
    public PlayerStats getPlayerInfo(Player player) {
        UUID uuid = player.getUniqueId();
        return activePlayers.get(uuid);
    }

    public boolean playerExists(UUID playerId) {
        return pm.playerExists(playerId);
    }

    /**
     * Saves a default player stats data into infrastructure layer (persistency)
     * @param player the player to save
     */
    public void saveDefaultPlayer(Player player) {
        PlayerStatsData DTO = new PlayerStatsData(
                player.getUniqueId().toString(),
                player.getName(),
                (int) (System.currentTimeMillis() / 1000L),
                0
        );
        pm.savePlayer(DTO);
    }

    // TODO: Initialize player stats on join from database and put it in active players armor bonuses

}
