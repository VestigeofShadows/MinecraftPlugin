package space.vestiges.plugin1.mob;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.LivingEntity;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.utils.HealthUtils;

import java.util.HashMap;
import java.util.UUID;

public class MobHpDisplay {

    private static final BukkitAPIHelper mythicHelper = new BukkitAPIHelper();
    private final HashMap<UUID, Component> mobsName = new HashMap<UUID, Component>();

    public static Component healthDisplay(LivingEntity entity, double currentHp, double maxHp, Component basename) {

        int level = 1;

        // get mob's level
        if(mythicHelper.isMythicMob(entity.getUniqueId())) {
            level = (int) mythicHelper.getMythicMobInstance(entity).getLevel();
            //Plugin1.getInstance().getLogger().info("mythic mob detected level: " + level);
        }

        Component levelPart = Component.text("[Level " + level + "] ")
                .color(NamedTextColor.YELLOW)
                .decorate(TextDecoration.BOLD);

        Component namePart;
        if (basename == null) {
            namePart = Component.text(entity.getType().name());
            Plugin1.getInstance().getLogger().warning("Major bug happening on the name");
        } else {
            namePart = basename.append(Component.text(" "));
        }

        Component hpPart = Component.text(String.format("%.1f/%.0f", currentHp, maxHp)) //Change decimals here
                .color(NamedTextColor.RED);

        return levelPart.append(namePart).append(hpPart);
    }

    /**
     * Updates an entity's hp, and displays it.
     *
     * @param entity LivingEntity to display hp
     * @param currentHp the entity's current health as double
     * @param basename the base name of the mob as Component
     */
    public void updateHpDisplay(LivingEntity entity, double currentHp, Component basename) {
        double maxhp = HealthUtils.getMaxHealth(entity);
        Component healthDisplay = healthDisplay(entity, currentHp, maxhp, basename);
        entity.customName(healthDisplay);
        entity.setCustomNameVisible(true);
    }
}