package space.vestiges.plugin1.packets;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class FloatingTextPacket {

    private final ProtocolManager pm = ProtocolLibrary.getProtocolManager();

    // Spawn a client-side-only “hologram” for one viewer. Returns the fake entityId you should keep.
    public int show(Player viewer, Location loc, String text) throws Exception {
        final int entityId = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        final UUID uuid = UUID.randomUUID();

        // 1) Spawn entity packet (generic)
        PacketContainer spawn = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawn.getIntegers().write(0, entityId);
        spawn.getUUIDs().write(0, uuid);
        spawn.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        spawn.getDoubles().write(0, loc.getX());
        spawn.getDoubles().write(1, loc.getY());
        spawn.getDoubles().write(2, loc.getZ());

        // 2) Metadata: invisible, custom name visible, no gravity, marker
        List<WrappedDataValue> metadata = new ArrayList<>();

        // index 0: Entity flags (bit 5 = invisible)
        metadata.add(new WrappedDataValue(
                0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x20));

        // index 2: Custom name (Optional<Component>)
        metadata.add(new WrappedDataValue(
                2, WrappedDataWatcher.Registry.getChatComponentSerializer(true),
                Optional.of(WrappedChatComponent.fromText(text).getHandle())));

        // index 3: Custom name visible (boolean)
        metadata.add(new WrappedDataValue(
                3, WrappedDataWatcher.Registry.get(Boolean.class), true));

        // index 5: No gravity (boolean)
        metadata.add(new WrappedDataValue(
                5, WrappedDataWatcher.Registry.get(Boolean.class), true));

        // ArmorStand-specific flags at index 15 (byte). bit 0x10 = marker
        metadata.add(new WrappedDataValue(
                15, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x10));

        PacketContainer meta = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        meta.getIntegers().write(0, entityId);
        meta.getDataValueCollectionModifier().write(0, metadata);

        pm.sendServerPacket(viewer, spawn);
        pm.sendServerPacket(viewer, meta);

        return entityId;
    }

    // Update the text on an existing fake armor stand
    public void updateText(Player viewer, int entityId, String newText) throws Exception {
        List<WrappedDataValue> list = new ArrayList<>();
        list.add(new WrappedDataValue(
                2, WrappedDataWatcher.Registry.getChatComponentSerializer(true),
                Optional.of(WrappedChatComponent.fromText(newText).getHandle())));
        list.add(new WrappedDataValue(
                3, WrappedDataWatcher.Registry.get(Boolean.class), true));

        PacketContainer meta = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        meta.getIntegers().write(0, entityId);
        meta.getDataValueCollectionModifier().write(0, list);
        pm.sendServerPacket(viewer, meta);
    }

    // Move the fake entity
    public void teleport(Player viewer, int entityId, Location to) throws Exception {
        PacketContainer tp = pm.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        tp.getIntegers().write(0, entityId);
        tp.getDoubles().write(0, to.getX());
        tp.getDoubles().write(1, to.getY());
        tp.getDoubles().write(2, to.getZ());
        tp.getBytes().write(0, (byte) 0); // yaw
        tp.getBytes().write(1, (byte) 0); // pitch
        tp.getBooleans().write(0, false); // onGround
        pm.sendServerPacket(viewer, tp);
    }

    // Destroy for that viewer only
    public void destroy(Player viewer, int entityId) throws Exception {
        PacketContainer destroy = pm.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroy.getIntegerArrays().write(0, new int[] { entityId });
        pm.sendServerPacket(viewer, destroy);
    }
}