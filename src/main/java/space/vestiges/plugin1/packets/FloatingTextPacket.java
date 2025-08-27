package space.vestiges.plugin1.packets;

import com.comphenix.protocol.ProtocolManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import space.vestiges.plugin1.Plugin1;

public final class FloatingTextPacket {

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // ------------------------------ Class Functions  ----------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * This function spawns a text displaying damage dealt at a target's location
     * Only visible to the target that did the damage
     * @param target the entity that took damage
     * @param player which player to show it to
     * @param dmg the amount of damage to display
     * @param color the color of the text
     */
    public void spawntext(Entity target, Player player, double dmg, int color) {

        Location loc = getLocation(target);

        TextDisplay text = loc.getWorld().spawn(loc, TextDisplay.class, display -> {

            // What to display
            Component dmgnumber = getComponent(dmg, color);

            display.text(dmgnumber); // dmg dealt

            display.setVisibleByDefault(false);
            TextDisplayOptions(display);

            player.showEntity(Plugin1.getInstance(), display);

            Bukkit.getScheduler().runTaskLater(Plugin1.getInstance(), display::remove, 20L);
        });
    }

    /**
     * This function spawns a text displaying damage dealt at a target's location
     * Visible to all players (overloaded)
     * @param target the entity that took damage
     * @param dmg the amount of damage to display
     * @param color the color of the text
     */
    public void spawntext(Entity target, double dmg, int color) {

        Location loc = getLocation(target);

        TextDisplay text = loc.getWorld().spawn(loc, TextDisplay.class, display -> {

            // What to display
            Component dmgnumber = getComponent(dmg, color);

            display.text(dmgnumber); // dmg dealt

            display.setVisibleByDefault(true);
            TextDisplayOptions(display);

            Bukkit.getScheduler().runTaskLater(Plugin1.getInstance(), display::remove, 20L);
        });
    }

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    // ----------------------------------------------------------------------------------
    // -------------------------     HELPER FUNCTIONS     -------------------------------
    // ----------------------------------------------------------------------------------
    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Helper function for spawntext
     * used to create templates for text display entities
     * @param display the TextDisplay to format
     */
    private void TextDisplayOptions(TextDisplay display) {
        display.setSeeThrough(true);
        display.setPersistent(false);                   // entity removed on server crashes
        display.setBillboard(Display.Billboard.CENTER); // faces the player

        double xOffset = (Math.random() - 0.5) * 0.6;   // -0.3 to +0.3
        double zOffset = (Math.random() - 0.5) * 0.6;
        double yOffset = Math.random() * 0.1;           // 0 → 0.1 higher

        Transformation end = new Transformation(
                new Vector3f((float) xOffset, 0.4f + (float) yOffset, (float) zOffset), // move up 0.5
                new Quaternionf(),
                new Vector3f(0.5f, 0.5f, 0.5f), // shrink
                new Quaternionf()
        );

        display.setTransformation(end);
    }

    /**
     * Gets the spawning location of text for a target
     * It slightly randomizes the location a bit
     * @param target the target in question
     * @return Location of the target
     */
    @NotNull
    private static Location getLocation(Entity target) {

        double spawnX = (Math.random() - 0.5); // x -0.5 to +0.5
        double spawnZ = (Math.random() - 0.5); // z -0.5 to +0.5
        double spawnY = 0.2 + (Math.random() * 0.2); // 0.2–0.4 above entity

        assert target != null;
        return target.getLocation().add(spawnX,1 + spawnY, spawnZ); // slightly up so it's not on the ground
    }

    /**
     * Helper function for spawntext
     * makes a component for the dmg number with different colors
     * 0 for player dealt damage (physical)
     * 1 for environmental damage
     * 2 for magical damage (not implemented)
     * @param dmg the amount of damage to display
     * @param color the color of the component
     * @return returns the Component
     */
    @NotNull
    private static Component getComponent(double dmg, int color) {

        Component dmgnumber;
        String damage;

        switch (color) {
            case 0:
                damage = String.format("\uD83D\uDCA5%.1f\uD83D\uDCA5", dmg);
                dmgnumber = Component.text(damage, NamedTextColor.RED);
                if (Plugin1.getInstance().toggleflag) System.out.println("physical dmg");
                break;
            case 1:
                damage = String.format("☠%.1f☠", dmg);
                dmgnumber = Component.text(damage, NamedTextColor.GOLD);
                if (Plugin1.getInstance().toggleflag) System.out.println("environment dmg");
                break;
            case 2:
                damage = String.format("✨%.1f✨", dmg);
                dmgnumber = Component.text(damage, NamedTextColor.DARK_AQUA); // can try LIGHT_PURPLE or AQUA
                if (Plugin1.getInstance().toggleflag) System.out.println("magic dmg");
                break;
            default:
                damage = String.format("%.1f", dmg);
                dmgnumber = Component.text("UnknownColorSendHelp", NamedTextColor.LIGHT_PURPLE);
                if (Plugin1.getInstance().toggleflag) System.out.println("FloatingTextPacket.getComponent Error");
        }
        return dmgnumber;
    }
}