package space.vestiges.plugin1.listeners;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.events.MythicDamageEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.mob.MobHpDisplay;
import space.vestiges.plugin1.player.PlayerStats;
import space.vestiges.plugin1.player.PlayerStatsManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MobListener implements Listener {

    private final Set<UUID> processing = new HashSet<>();
    private final BukkitAPIHelper mythicHelper = new BukkitAPIHelper();
    private final MobHpDisplay hpdisplay = new MobHpDisplay();
    PlayerStatsManager statsManager = Plugin1.getInstance().getStatsManager();

    // Damage event dealt by environment and other entities.
    @EventHandler
    public void onEntityDamage(@NotNull EntityDamageEvent event) {
        boolean flag = Plugin1.getInstance().toggleflag;

        // ignore armor stands explicitly LOL
        if ((event.getEntity() instanceof ArmorStand)) return;
        // ignore players
        if ((event.getEntity() instanceof Player)) return;
        // ignore player caused damage (handled separately)
        if (event.getEntity() instanceof EntityDamageByEntityEvent) return;

        LivingEntity mob = (LivingEntity) event.getEntity();
        // Check if it's a mythicmob
        if(mythicHelper.isMythicMob(mob)){
            ActiveMob mm = mythicHelper.getMythicMobInstance(mob);
            if (mm != null) {
                if (flag) {
                    Plugin1.getInstance().getLogger().info("Mythic mob hit!");
                }

                //mythic mobs hp display
                hpdisplay.updateHpDisplay(mob, mob.getHealth());
            }
        } else {
            //vanilla stuff
            if (flag) {
                Plugin1.getInstance().getLogger().info("Vanilla mob hit!");
            }
            //vanilla hp display
            hpdisplay.updateHpDisplay(mob, mob.getHealth());
        }
    }
    // Damage event dealt by a player.
    @EventHandler
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {

        //Ignore targets that aren't valid (including other players?)
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;

        LivingEntity target = (LivingEntity) event.getEntity();

        if (processing.contains(target.getUniqueId())) {
            return; // EXIT OUT IF TARGET's UUID IS SET (avoid loop)
        }
        Player attacker = getPlayer(event);

        //Ignore attackers that aren't valid
        if (attacker != null) {
            boolean flag = Plugin1.getInstance().toggleflag;
            if (flag) {
                Plugin1.getInstance().getLogger().info(attacker.getName() + " hit a mob!");
            }

            // attack cooldown
            PlayerStats playerStats = statsManager.getPlayerInfo(attacker);
            double cooldownInMiliseconds = 1000.0 / playerStats.getAttackSpeed();
            long elapsed = System.currentTimeMillis() - playerStats.getLastAttackTime();
            if (elapsed < cooldownInMiliseconds) {
                event.setCancelled(true);
                return;
            }

            // cancel dmg event
            event.setCancelled(true);

            double dmg_dealt;
            PlayerStats stats = statsManager.getPlayerInfo(attacker);
            Plugin1.getInstance().getLogger().info("Your Power: " + stats.getPower());
            dmg_dealt = stats.getPower();

            // Update lastAttackTime
            playerStats.setLastAttackTime(System.currentTimeMillis());

            boolean lethal = target.getHealth() - dmg_dealt < 0;

            if (lethal) {
                processing.add(target.getUniqueId());
                double finalDmg_dealt = dmg_dealt;
                Bukkit.getScheduler().runTask(Plugin1.getInstance(), () -> {
                    target.damage(finalDmg_dealt, attacker);
                    processing.remove(target.getUniqueId());
                    hpdisplay.updateHpDisplay(target, target.getHealth());
                });
            } else {
                target.setHealth(target.getHealth() - dmg_dealt);
                hpdisplay.updateHpDisplay(target, target.getHealth());
            }

        }
    }
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
}
