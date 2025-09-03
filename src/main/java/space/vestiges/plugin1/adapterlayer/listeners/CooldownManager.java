package space.vestiges.plugin1.adapterlayer.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.vestiges.plugin1.adapterlayer.Plugin1;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------- classes/variables Initialization ----------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    // Unkonwn source default UUID
    private static final UUID UNKNOWN_SOURCE = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * First UUID is for mob, second UUID is for player, the Long is the last time the player hit this mob
     */
    Map<UUID, Map<UUID, Long>> mobHitTimestamps = new HashMap<>();

    /**
     * First UUID is for mob, second UUID is for item, and the attack cooldown associated with the weapon
     */
    Map<UUID, Map<ItemStack, Long>> playerWeaponCooldown = new HashMap<>();

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Class Functions  ----------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Takes in mob being hit, the current time, returns boolean for whether attack is valid or not
     *
     * @param mob takes in the entity to track
     * @return boolean true if valid attack
     */
    public boolean undefinedDmg(LivingEntity mob) {

        // mob id
        UUID mobid = mob.getUniqueId();

        // check if mob is in hashmap, and get its hashmap
        mobInitialized(mob);
        Map<UUID, Long> mobmap = mobHitTimestamps.get(mobid);

        // check if attacker exist, if not create one and put it in
        boolean attackerexist = mobmap.containsKey(UNKNOWN_SOURCE);

        // check if attacker exist, if not create one.
        if (!attackerexist) {
            // first attack is valid, then add current timestamp in
            mobmap.put(UNKNOWN_SOURCE, System.currentTimeMillis());
            return true;
        } else {
            // calculate elapsed time to see if it's valid
            long lastAttackedTime = mobmap.get(UNKNOWN_SOURCE);
            long currentTime = System.currentTimeMillis();
            long elapsed = (currentTime - lastAttackedTime);

            // return valid if >= 500, otherwise return false and cancel event
            if (elapsed >= 500) {
                mobmap.put(UNKNOWN_SOURCE, currentTime);
                return true;
            }
            return false;
        }
    }

    /**
     * Calcluate each player's attack cd on this mob
     *
     * @param mob the mob being attacked
     * @param player the attacking player
     * @return true if the attack is valid, false if not
     */
    public boolean playerDmg(LivingEntity mob, Player player) {
        // mob id
        UUID mobid = mob.getUniqueId();

        // check if mob is in hashmap, and get its hashmap
        mobInitialized(mob);
        Map<UUID, Long> mobmap = mobHitTimestamps.get(mobid);

        // check if attacker exist, if not create one and put it in mobmap
        boolean attackerexist = mobmap.containsKey(player.getUniqueId());

        // check if attacker exist, if not create one and populate contents
        if (!attackerexist) {
            // first attack is valid, then add current timestamp in
            mobmap.put(player.getUniqueId(), System.currentTimeMillis());
            return true;
        } else {
            // calculate elapsed time to see if it's valid
            long lastAttackedTime = mobmap.get(player.getUniqueId());
            long currentTime = System.currentTimeMillis();
            long elapsed = (currentTime - lastAttackedTime);

            // return valid if >= (CHECK WEAPON ATTACK SPEED), otherwise return false and cancel event
            if (elapsed >= 0) { //500 for .5s
                Plugin1.getInstance().getLogger().info(player.getName() + " did a valid attack against " + mob.customName());
                mobmap.put(player.getUniqueId(), currentTime);
                return true;
            }
            //Plugin1.getInstance().getLogger().info(player.getName() + " attack on cooldown!");
            return false;
        }
    }

    /**
     * Remove mob from mobHitTimestamps to prevent memory leak.
     *
     * @param mob the dead mob to remove
     */
    public void removeMobOnDeath(LivingEntity mob) {
        mobHitTimestamps.remove(mob.getUniqueId());
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     HELPER FUNCTIONS     -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Helper function
     * Initializes mob to put into the hashmap
     * @param mob the mob to be initialized
     */
    private void mobInitialized(LivingEntity mob) {
        // if mob doesn't exist, add it to the hashmap
        if (!(mobHitTimestamps.containsKey(mob.getUniqueId()))){
            Map<UUID, Long> map = new HashMap<>();
            mobHitTimestamps.put(mob.getUniqueId(), map);
        }
    }
}
