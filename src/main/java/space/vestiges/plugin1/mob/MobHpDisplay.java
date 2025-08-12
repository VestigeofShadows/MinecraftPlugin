package space.vestiges.plugin1.mob;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MobHpDisplay {

    public Component createHealthBar(double currentHealth, double maxHealth, NamedTextColor barColor) {
        double healthPercentage = (currentHealth / maxHealth) * 100;

        Component healthBar = Component.text("[");

        int filledLength = (int) (healthPercentage / 10); // 10 blocks total
        for (int i = 0; i < filledLength; i++) {
            healthBar = healthBar.append(Component.text("█").color(barColor));
        }
        for (int i = filledLength; i < 20; i++) {
            healthBar = healthBar.append(Component.text("░").color(TextColor.color(169, 169, 169))); // dark gray
        }
        healthBar = healthBar.append(Component.text("]"));

        Component healthText = Component.text(String.format(" %.0f%% ", healthPercentage))
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD);

        return healthBar.append(healthText);
    }

    public void updateHpDisplay(LivingEntity entity, double newHealth, int type) {
        if (newHealth < 0) newHealth = 0.0;
        double maxHealth = entity.getMaxHealth();

        NamedTextColor barColor;
        switch (type) {
            case 0 -> barColor = NamedTextColor.GREEN;
            case 1 -> barColor = NamedTextColor.AQUA;
            case 2 -> barColor = NamedTextColor.LIGHT_PURPLE;
            case 3 -> barColor = NamedTextColor.RED;
            default -> barColor = NamedTextColor.WHITE;
        }

        Component healthBar = createHealthBar(newHealth, maxHealth, barColor);

        entity.customName(healthBar);
        entity.setCustomNameVisible(true);
    }
}