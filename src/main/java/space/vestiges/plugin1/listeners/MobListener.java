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
        // debug flag
        boolean flag = Plugin1.getInstance().toggleflag;
        // ignore players, armor stands, and entity-caused damage
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;
        if (event instanceof EntityDamageByEntityEvent) return;
        if (processing.contains(event.getEntity().getUniqueId())) return;

        if (!(event.getEntity() instanceof LivingEntity mob)) return;

        double newHealth = Math.max(0, mob.getHealth() - event.getFinalDamage());

        if (mythicHelper.isMythicMob(mob)) {
            if (flag) Plugin1.getInstance().getLogger().info("Mythic mob hit!");
            hpdisplay.updateHpDisplay(mob, newHealth, 0);
        } else {
            if (flag) Plugin1.getInstance().getLogger().info("Vanilla mob hit!");
            hpdisplay.updateHpDisplay(mob, newHealth, 0);
        }
    }
    // Damage event dealt by a player.
    @EventHandler
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {

        // ignore invalid entities, players, and dmg event caused by this event
        if (!(event.getEntity() instanceof LivingEntity target)) return; // LivingEntity target
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;
        if (processing.contains(target.getUniqueId())) return;

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
                Bukkit.getScheduler().runTask(Plugin1.getInstance(), () -> {
                    target.damage(dmg_dealt, attacker);
                    processing.remove(target.getUniqueId());
                    hpdisplay.updateHpDisplay(target, target.getHealth(), 0);
                });
            } else {
                target.setHealth(target.getHealth() - dmg_dealt);
                hpdisplay.updateHpDisplay(target, target.getHealth(), 0);
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
