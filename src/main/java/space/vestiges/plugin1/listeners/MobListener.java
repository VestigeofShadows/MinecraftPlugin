package space.vestiges.plugin1.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import space.vestiges.plugin1.Plugin1;

public class MobListener implements Listener {

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        // toggle flag for cringe
        if (Plugin1.getInstance().toggleflag) {
            Plugin1.getInstance().getLogger().info("Something spawned");
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (Plugin1.getInstance().toggleflag) {
            Plugin1.getInstance().getLogger().info("Something died");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        LivingEntity mob =  (LivingEntity) event.getEntity();
        // ignore armor stands explicitly LOL
        if ((mob instanceof ArmorStand)) return;
        // ignore players
        if ((mob instanceof Player)) return;

        if (Plugin1.getInstance().toggleflag) {
            Plugin1.getInstance().getLogger().info("Something got hit");
        }

        //vanilla hp
        double newHealth = mob.getHealth() - event.getFinalDamage();
        if (newHealth < 0) newHealth = 0;

        String name = String.format("HP: %.1f / %.1f", newHealth, mob.getMaxHealth());
        mob.setCustomName(name);
        mob.setCustomNameVisible(true);
    }
}
