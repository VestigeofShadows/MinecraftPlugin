package space.vestiges.plugin1.mob;

import org.bukkit.entity.LivingEntity;

public class MobHpDisplay {
    // Unused Display Methods
     /*
    // Vanilla mob HP display - called on entity damaged
    public void displayVanillaMobHp(){

    }

    // Mythic mob HP display - called on entity damaged
    public void displayMythicMobHp(){

    }
      */
    //
    public void updateHpDisplay(LivingEntity entity, double currHealth) {
        if (currHealth < 0) currHealth = 0.0;
        String name = String.format("§cHP: %.2f§7/%.1f", currHealth, entity.getMaxHealth());
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);
    }
}
