package space.vestiges.plugin1.domainlayer.model.mob;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.LivingEntity;
import space.vestiges.plugin1.adapterlayer.Plugin1;

import java.util.HashMap;
import java.util.UUID;

public class MobHpDisplay {

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     Class variables     --------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    private static final BukkitAPIHelper mythicHelper = new BukkitAPIHelper();
    private final HashMap<UUID, Component> mobsBaseName = new HashMap<>();

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // --------------------------    Class functions     --------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Updates an entity's current hp, and displays it.
     *
     * @param entity LivingEntity to display hp
     * @param currentHp the entity's current health as double
     */
    public void updateHpDisplay(LivingEntity entity, double currentHp) {
        mobsBaseNameAdd(entity);
        double maxhp = HealthUtils.getMaxHealth(entity);
        Component healthDisplay = healthDisplay(entity, currentHp, maxhp);
        entity.customName(healthDisplay);
        entity.setCustomNameVisible(true);
    }

    /**
     * returns boolean of if the mob basename exist in hashmap
     *
     * @param entity input mob
     * @return if mob basename exist bool
     */
    public boolean mobsBaseNameExist(LivingEntity entity) {
        return mobsBaseName.containsKey(entity.getUniqueId());
    }

    /**
     * removes basename from hashmap to save memory
     * @param entity entity to remove
     */
    public void mobsBaseNameDelete(LivingEntity entity) {
        mobsBaseName.remove(entity.getUniqueId()); //don't need to check if it exists first
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------- HELPER FUNCTION / UNUSED -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Helper function for updateHpDisplay
     * Put an entity's basename component into hashmap if it doesn't exist yet
     *
     * @param entity put this entity's component name into the hashmap
     */
    private void mobsBaseNameAdd(LivingEntity entity) {
        // Check if name exist yet, if not return error
        if (mobsBaseNameExist(entity)) {
            if (Plugin1.getInstance().toggleflag) Plugin1.getInstance().getLogger().warning("MobHpDisplay.mobsBaseNameAdd already exist");
            return;
        }

        // initiate variable
        Component name;

        // check if mob is mythic
        if (mythicHelper.isMythicMob(entity)) {
            String mythicName = mythicHelper.getMythicMobInstance(entity).getName();
            name = Component.text(mythicName);
            if (Plugin1.getInstance().toggleflag) Plugin1.getInstance().getLogger().warning("MobHpDisplay.mobsBaseNameAdd: mythic mob detected: " + name);
        } else {
            //Check if entity already have a custom name made by players
            name = entity.customName();
            // If it doesn't have a custom name already, use vanilla name
            if (name == null) {
                String vanillaName = entity.getType().name();
                vanillaName = vanillaName.toLowerCase().replace("_"," ");
                vanillaName = vanillaName.substring(0,1).toUpperCase() + vanillaName.substring(1);
                name = Component.text(vanillaName).color(NamedTextColor.GREEN);
                if (Plugin1.getInstance().toggleflag) Plugin1.getInstance().getLogger().info("MobHpDisplay.mobsBaseNameAdd: vanilla mob detected: " + name);
            } else {
                if (Plugin1.getInstance().toggleflag) Plugin1.getInstance().getLogger().info("MobHpDisplay.mobsBaseNameAdd: named vanilla mob detected: " + name);
            }
        }

        mobsBaseName.put(entity.getUniqueId(), name);

    }

    /**
     * Helper function for updateHpDisplay
     * Creates a component name for a mob based on their hp.
     *
     * @param entity the entity to display hp for
     * @param currentHp what hp to display for current hp
     * @param maxHp what hp to display for max hp
     * @return component of the name
     */
    private Component healthDisplay(LivingEntity entity, double currentHp, double maxHp) {

        if (!mobsBaseNameExist(entity)) { // DEBUG DEBUG DEBUG DEBUG DEBUG
            if (Plugin1.getInstance().toggleflag) System.out.println("Mobname doesn't exist but updateHpDisplay is called");
        }
        Component basename = mobsBaseName.get(entity.getUniqueId());

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
}