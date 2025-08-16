package space.vestiges.plugin1.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public final class HealthUtils {

    private HealthUtils() {} // prevent instantiation

    public static double getMaxHealth(LivingEntity entity) {
        AttributeInstance attr = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) {
            throw new IllegalStateException(entity.getName() + " has no MAX_HEALTH attribute!");
        }
        return attr.getValue();
    }

    public static void setMaxHealth(LivingEntity entity, double value) {
        AttributeInstance attr = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) {
            throw new IllegalStateException(entity.getName() + " has no MAX_HEALTH attribute!");
        }
        attr.setBaseValue(value);
    }
}