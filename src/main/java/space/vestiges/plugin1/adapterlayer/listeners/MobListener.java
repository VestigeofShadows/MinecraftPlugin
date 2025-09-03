package space.vestiges.plugin1.adapterlayer.listeners;

import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.domainlayer.model.mob.MobHpDisplay;
import space.vestiges.plugin1.adapterlayer.visual.FloatingTextPacket;
import space.vestiges.plugin1.domainlayer.model.mob.HealthUtils;

public class MobListener implements Listener {



    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------- classes/variables Initialization ----------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    // Initialized private variables
    private final MobHpDisplay mobHpDisplay = new MobHpDisplay();
    private final CooldownManager mobImmunityManager = new CooldownManager();
    private final FloatingTextPacket ftp = new FloatingTextPacket();

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Class Functions  ----------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

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
        Bukkit.getScheduler().runTask(Plugin1.getInstance(), () -> mobHpDisplay.updateHpDisplay(mob, HealthUtils.getMaxHealth(mob)));
    }

    /**
     * This event prevents memory-leak by removing mobs from hashmaps.
     *
     * @param event EntityDeathEvent
     */
    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        // Memory-leak management, remove mob from hashmaps on their death.
        mobImmunityManager.removeMobOnDeath(event.getEntity());
        mobHpDisplay.mobsBaseNameDelete(event.getEntity());
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

        //------------------- Set mob to have no dmg ticks --------------------
        if (!(mob.getMaximumNoDamageTicks() == 0)) {
            mob.setMaximumNoDamageTicks(0);
        }
        //------------------- Set mob to have no dmg ticks --------------------


        //------------------------- Immunity Table -------------------------
        if (!mobImmunityManager.undefinedDmg(mob)) {
            event.setCancelled(true);
            return;
        }
        //------------------------- Immunity Table -------------------------

        //------------------------- Health Display Section -------------------------
        // shows damage number
        double eventDmg = event.getFinalDamage();
        ftp.spawntext(mob, eventDmg, 1);
        // (fishes are bugged to take dmg on spawn, this may or may not affect it)
        // display updated health if basename exist, otherwise don't.
        if (mobHpDisplay.mobsBaseNameExist(mob)) {
            double newHealth = Math.max(0, mob.getHealth() - event.getFinalDamage());
            mobHpDisplay.updateHpDisplay(mob, newHealth);
        }
        //------------------------- Health Display Section -------------------------
    }

    // Current iteration of EntityDamagedByEntityEvent (no attack speed/dmg implementation yet)
    @EventHandler
    public void onPlayerDamageEntity(@NotNull EntityDamageByEntityEvent event) {
        // Ignore invalid entities, players, and dmg event caused by this event, initializes name if not there
        if (!(event.getEntity() instanceof LivingEntity target)) return;
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity() instanceof ArmorStand) return;
        if (!(getDamageSource(event) instanceof Player attacker)) return;

        //------------------- Set mob to have no dmg ticks --------------------
        if (!(target.getMaximumNoDamageTicks() == 0)) {
            target.setMaximumNoDamageTicks(0);
        }
        //------------------- Set mob to have no dmg ticks --------------------

        //------------------------- Immunity Table -------------------------
        if (!mobImmunityManager.playerDmg(target, attacker)) {
            event.setCancelled(true);
            return;
        }
        //------------------------- Immunity Table -------------------------

        //----------------------- Damage Calculation -----------------------
        // TODO: lol, im tired please help
        // PlayerStats stats = statsManager.getPlayerInfo(attacker);
        double finalDmg = event.getFinalDamage();
        //----------------------- Damage Calculation -----------------------

        //----------------------- Health Display ---------------------------
        double newHealth = Math.max(0, target.getHealth() - event.getFinalDamage());
        mobHpDisplay.updateHpDisplay(target, newHealth);
        ftp.spawntext(target, attacker, finalDmg, 0);
        //----------------------- Health Display ---------------------------
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     HELPER FUNCTIONS     -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&



    /**
     * Obtains the Player entity that caused the damage (including projectiles)
     *
     * @param event pass this event into the function
     * @return finds the Player entity that caused the source of the damage
     */
    @Nullable
    private static Player getDamageSource(@NotNull EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Player attacker;

        if (damager instanceof Player) {
            attacker = (Player) damager;
        } else if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();
            // remove the projectile after getting the shooter
            // damager.remove();
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
