package space.vestiges.plugin1.domainlayer.model.mob;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

/**
 * This Class is used for obtaining a mob's hp value because it's not very straight forward
 */
public final class HealthUtils {

    private HealthUtils() {} // prevent instantiation

    /**
     * Gets a mob's hp value
     * @param entity the entity to get the hp value from
     * @return the hp value as a double
     */
    public static double getMaxHealth(LivingEntity entity) {
        AttributeInstance attr = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) {
            throw new IllegalStateException(entity.getName() + " has no MAX_HEALTH attribute!");
        }
        return attr.getValue();
    }
}