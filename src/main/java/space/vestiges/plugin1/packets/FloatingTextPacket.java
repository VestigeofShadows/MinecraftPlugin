package space.vestiges.plugin1.packets;


import com.comphenix.protocol.ProtocolManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import space.vestiges.plugin1.Plugin1;

public final class FloatingTextPacket {
    private final ProtocolManager pm = Plugin1.getInstance().getProtocolManager();

    public void spawntext(Entity target, Player player, double dmg) {

        double spawnX = (Math.random() - 0.5); // -0.5 to +0.5
        double spawnZ = (Math.random() - 0.5); // y -0.5 to +0.5
        double spawnY = 0.5 + (Math.random() * 0.5); // 0.5–1.0 above entity

        Location loc = target.getLocation().add(spawnX,1.45 + spawnY, spawnZ); // move it a tiny bit above

        TextDisplay text = loc.getWorld().spawn(loc, TextDisplay.class, display -> {

            String damage = String.format("\uD83D\uDCA5%.1f\uD83D\uDCA5", dmg);
            Component dmgnumber = Component.text(damage, NamedTextColor.YELLOW);
            display.text(dmgnumber); // dmg dealt

            display.setVisibleByDefault(false);
            display.setPersistent(false);
            display.setBillboard(Display.Billboard.CENTER); // faces the player
            display.setSeeThrough(true);

            double xOffset = (Math.random() - 0.5) * 0.6; // -0.3 to +0.3
            double zOffset = (Math.random() - 0.5) * 0.6;
            double yOffset = Math.random() * 0.2;        // 0 → 0.2 higher

            Transformation end = new Transformation(
                    new Vector3f((float) xOffset, 0.4f + (float) yOffset, (float) zOffset), // move up 0.5
                    new Quaternionf(),
                    new Vector3f(0.5f, 0.5f, 0.5f), // shrink
                    new Quaternionf()
            );

            display.setTransformation(end);

            player.showEntity(Plugin1.getInstance(), display);
            Bukkit.getScheduler().runTaskLater(Plugin1.getInstance(), display::remove, 40L);
        });
    }
}