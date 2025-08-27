package space.vestiges.plugin1.listeners;

import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.api.skills.SkillHolder;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import io.lumine.mythic.bukkit.events.MythicSkillEvent;
import io.lumine.mythic.bukkit.events.MythicTriggerEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.mob.MobHpDisplay;
import space.vestiges.plugin1.packets.FloatingTextPacket;
import space.vestiges.plugin1.player.PlayerStats;
import space.vestiges.plugin1.player.PlayerStatsManager;
import net.kyori.adventure.text.Component;
import space.vestiges.plugin1.utils.HealthUtils;

import java.util.*;

public class MobListener implements Listener {



    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------- classes/variables Initialization ----------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    // Initialized private variables
    private final BukkitAPIHelper mythicHelper = new BukkitAPIHelper();
    private final MobHpDisplay hpdisplay = new MobHpDisplay();
    PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();

    // Hashmaps
    private final HashMap<UUID, Component> mobsName = new HashMap<UUID, Component>();
    private final CooldownManager cooldownManager = new CooldownManager();
    // private final Set<UUID> processing = new HashSet<>();

    //Testing Block
    FloatingTextPacket ftp = new FloatingTextPacket();

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Class Functions  ----------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * currently not used, because I'm trying to save memory
     *
     * @param event pass in the entityspawnevent
     */
    @EventHandler
    public void onEntitySpawn(@NotNull EntitySpawnEvent event) {
        // ignore invalid entities, players, armorstands, and initializes name if mythic mob
        if (!(event.getEntity() instanceof LivingEntity mob)) return;
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;
    }

    /**
     * On mythic mob spawn, immediate display their name using hpdisplay
     *
     * @param event MythicMobSpawnEvent
     */
    @EventHandler
    public void onMythicMobSpawn(MythicMobSpawnEvent event) {
        // check invalid entities, but I don't think it matters?
        if (!(event.getEntity() instanceof final LivingEntity mob)) return;

        // Scheduler to display hp on the next tick because mythic mobs initializes the mob to be a mythic mob after the mob spawn event...
        // Can't cancel the event or the mob won't spawn, not even sure what this EventHandler is good for
        Bukkit.getScheduler().runTask(Plugin1.getInstance(), () -> {
            putCustomNameInMap(mob);
            hpdisplay.updateHpDisplay(mob, HealthUtils.getMaxHealth(mob), mobsName.get(event.getEntity().getUniqueId()));
        });
    }

    /**
     * This event prevents memory-leak by removing mobs from hashmaps.
     *
     * @param event EntityDeathEvent
     */
    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        // Memory-leak management, remove mob from hashmaps on their death.
        cooldownManager.removeMobOnDeath(event.getEntity());
        mobsName.remove(event.getEntity().getUniqueId());
    }

    /**
     * Damage event dealt by environment and other entitiies.
     * Used for immunity
     *
     * @param event EntityDamageEvent
     */
    @EventHandler
    public void onEntityDamageEnvironment(@NotNull EntityDamageEvent event) {
        // ignore players, armor stands, and entity-caused damage, initializes name if not there
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;
        if (event instanceof EntityDamageByEntityEvent) return;
        if (!(event.getEntity() instanceof LivingEntity mob)) return;
        putCustomNameInMap(mob);

        //------------------- Set mob to have no dmg ticks --------------------
        if (!(mob.getMaximumNoDamageTicks() == 0)) {
            mob.setMaximumNoDamageTicks(0);
        }
        //------------------- Set mob to have no dmg ticks --------------------


        //------------------------- Immunity Table -------------------------
        if (!cooldownManager.undefinedDmg(mob)) {
            event.setCancelled(true);
            return;
        }
        //------------------------- Immunity Table -------------------------

        //------------------------- Health Display Section -------------------------
        // Actual display of health, need this if you want any damage to trigger health display
        // (fishes are bugged to take dmg on spawn, this may or may not affect it)
         /*
        // double newHealth = Math.max(0, mob.getHealth() - event.getFinalDamage());
        /*
        if (mythicHelper.isMythicMob(mob)) {
            hpdisplay.updateHpDisplay(mob, newHealth, mobsName.get(event.getEntity().getUniqueId()));
        } else {
            hpdisplay.updateHpDisplay(mob, newHealth, mobsName.get(event.getEntity().getUniqueId()));
        }

          */
        //------------------------- Health Display Section -------------------------
    }

    /**
     * Damage event dealt by a player to a LivingEntity
     * This is the original method, cancel event, manually set mob's current hp to use my own math formulas
     *
     * @param event EntityDamagedByEntityEvent
     */
    // onEntityDamageByEntity
     /*
    @EventHandler
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {

        // Ignore invalid entities, players, and dmg event caused by this event
        if (!(event.getEntity() instanceof LivingEntity target)) return; // LivingEntity target
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;
        if (processing.contains(target.getUniqueId())) {
            processing.remove(target.getUniqueId());
            return;
        } // ignore events generated by custom logic

        // Grab the Player attacker (projectile or not)
        Player attacker = getPlayer(event);
        if (attacker == null) return;
        // Put mob's name in hashmap if not exist
        putCustomNameInMap(target);

        // Ignore attackers that aren't valid
        boolean flag = Plugin1.getInstance().toggleflag;
        if (flag) {
            Plugin1.getInstance().getLogger().info(attacker.getName() + " hit a mob!");
        }

        // attack cooldown, cancel event if still on cd
        PlayerStats playerStats = statsManager.getPlayerInfo(attacker);
        double cooldownInMiliseconds = 1000.0 / playerStats.getAttackSpeed();
        long elapsed = System.currentTimeMillis() - playerStats.getLastAttackTime();
        if (elapsed < cooldownInMiliseconds) {
            event.setCancelled(true);
            return;
        }

        // cancel dmg event
        event.setCancelled(true);

        PlayerStats stats = statsManager.getPlayerInfo(attacker);
        Plugin1.getInstance().getLogger().info("Your Power: " + stats.getPower());
        double dmg_dealt = stats.getPower();

        // Update successful lastAttackTime
        playerStats.setLastAttackTime(System.currentTimeMillis());

        double newHealth = Math.max(0, target.getHealth() - dmg_dealt);
        boolean lethal = target.getHealth() - dmg_dealt < 0;

        if (lethal) {
            processing.add(target.getUniqueId());
            hpdisplay.updateHpDisplay(target, newHealth, mobs.get(target.getUniqueId()));
            target.damage(dmg_dealt * 2, attacker); //dmg*2 to make sure the target dies

            mobs.remove(target.getUniqueId());
        } else {
            hpdisplay.updateHpDisplay(target, newHealth, mobs.get(target.getUniqueId()));
            target.setHealth(target.getHealth() - dmg_dealt);
        }
    }
      */

    // Test grabbing weapon stats here!
     /*
    @EventHandler(priority = EventPriority.HIGHEST)
    public void attackSpeedCalc(@NotNull EntityDamageByEntityEvent event) {
        // Ignore invalid entities, players, and dmg event caused by this event
        if (!(event.getEntity() instanceof LivingEntity target)) return; // LivingEntity target
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;

        target.setMaximumNoDamageTicks(0);

        // CANCEL FIRST RUN, DO SECOND RUN
        if (processing.contains(target.getUniqueId())) { // SECOND RUN
            putCustomNameInMap(target);
            PlayerStats stats = statsManager.getPlayerInfo(Objects.requireNonNull(getPlayer(event)));
            Plugin1.getInstance().getLogger().info("Your base_dmg " + stats.getPower());
            Plugin1.getInstance().getLogger().info("Your getFinalDmg " + event.getFinalDamage());
            hpdisplay.updateHpDisplay(target, Math.max(0, target.getHealth() - event.getFinalDamage()), mobs.get(target.getUniqueId()));
            processing.remove(target.getUniqueId());
        } else { // FIRST RUN

            // get player attacker stats
            Player player = getPlayer(event);
            PlayerStats stats = statsManager.getPlayerInfo(Objects.requireNonNull(player));

            // calculate new dmg
            double base_dmg = stats.getPower(); //FORMULA HERE FOR BASE DMG

            processing.add(event.getEntity().getUniqueId());
            event.setCancelled(true); // cancel this event
            target.setNoDamageTicks(0);
            target.damage(base_dmg, player); // call the next event
        }
    } */

    // Current iteration of EntityDamagedByEntityEvent (no attack speed/dmg implementation yet)
    @EventHandler
    public void onPlayerDamageEntity(@NotNull EntityDamageByEntityEvent event) {
        // Ignore invalid entities, players, and dmg event caused by this event, initializes name if not there
        if (!(event.getEntity() instanceof LivingEntity target)) return;
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;
        if (!(getPlayer(event) instanceof Player attacker)) return;
        putCustomNameInMap(target);

        //------------------- Set mob to have no dmg ticks --------------------
        if (!(target.getMaximumNoDamageTicks() == 0)) {
            target.setMaximumNoDamageTicks(0);
        }
        //------------------- Set mob to have no dmg ticks --------------------

        //------------------------- Immunity Table -------------------------
        if (!cooldownManager.playerDmg(target, attacker)) {
            event.setCancelled(true);
            return;
        }
        //------------------------- Immunity Table -------------------------

        //----------------------- Damage Calculation -----------------------
        // TODO: lol, im tired please help
        PlayerStats stats = statsManager.getPlayerInfo(attacker);
        double finalDmg = event.getFinalDamage();
        //----------------------- Damage Calculation -----------------------

        //----------------------- Health Display ---------------------------
        hpdisplay.updateHpDisplay(target, Math.max(0, target.getHealth() - event.getFinalDamage()), mobsName.get(target.getUniqueId()));
        ftp.spawntext(target, attacker, finalDmg);
        //----------------------- Health Display ---------------------------
    }



    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------- HELPER FUNCTION / UNUSED -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Used to save projectile data, or the weapon that launched the projectile
     * Projectiles should hold the weapon base dmg info
     * Melee weapons don't need to, just save in player stats
     *
     * @param event ProjectileLaunchEvent
     */
    @EventHandler
    public void projectileLaunch(@NotNull ProjectileLaunchEvent event) {

        Entity e = event.getEntity();
        Plugin1.getInstance().getLogger().info("Projectile launched: " + e.toString());
    }

    /**
     * Obtains the Player entity that caused the damage (including projectiles)
     *
     * @param event pass this event into the function
     * @return finds the Player entity that caused the source of the damage
     */
    @Nullable
    private static Player getPlayer(@NotNull EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Player attacker;

        if (damager instanceof Player) {
            attacker = (Player) damager;
        } else if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();
            // remove the projectile after getting the shooter
            damager.remove();
            if (shooter instanceof Player) {
                attacker = (Player) shooter;
            } else {
                attacker = null;
            }
        } else {
            attacker = null;
        }
        return attacker;
    }

    /**
     * Checks if entity's name is already in the map, only put entity in map if it's damaged by a player
     *
     */
    private boolean isCustomNameInMap(LivingEntity entity) {
        return mobsName.containsKey(entity.getUniqueId());
    }

    /**
     * Puts an entity's name into the hashmap if not exist
     *
     * @param entity put this entity's component name into the hashmap
     */
    private void putCustomNameInMap(LivingEntity entity) {
        
        if (isCustomNameInMap(entity)) { return; }

        Component name;
        // Check if mythic
        if (mythicHelper.isMythicMob(entity)) {
            String mythicname = mythicHelper.getMythicMobInstance(entity).getName();
            name = Component.text(mythicname);
            Plugin1.getInstance().getLogger().info("putCustomName called mythic mob detected: " + name.toString());
        } else {
            name = entity.customName();
            if (name == null) { //if not custom, create component name version of it
                String vanillaName = entity.getType().name();
                vanillaName = vanillaName.toLowerCase().replace("_"," ");
                vanillaName = vanillaName.substring(0,1).toUpperCase() + vanillaName.substring(1);
                name = Component.text(vanillaName).color(NamedTextColor.GREEN);
                Plugin1.getInstance().getLogger().info("putCustomName called vanilla mob name: " + name.toString());
            }
        }
        mobsName.put(entity.getUniqueId(), name);
    }
}
