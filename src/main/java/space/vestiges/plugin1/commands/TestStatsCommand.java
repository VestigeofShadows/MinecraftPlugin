package space.vestiges.plugin1.commands;

import de.tr7zw.nbtapi.NBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.equipment.EquipmentStats;
import space.vestiges.plugin1.player.PlayerStats;
import space.vestiges.plugin1.player.PlayerStatsManager;
import space.vestiges.plugin1.player.PlayerStatsStorage;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.equipment.EquipmentManager;

import java.util.ArrayList;
import java.util.List;

public class TestStatsCommand implements CommandExecutor {

    private final PlayerStatsManager statsManager;
    private final PlayerStatsStorage statsStorage;
    private final EquipmentManager equipmentManager;

    public TestStatsCommand(PlayerStatsManager statsManager, PlayerStatsStorage statsStorage) {
        this.statsManager = statsManager;
        this.statsStorage = statsStorage;
        this.equipmentManager = new EquipmentManager();
    }

    /**
     * use /p
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return boolean, but it just returns true for now...
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage: /p <show|getkit|showarmor|showstats|loadarmor|toggle|maxatkspd> don't run remove");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "show" -> {
                PlayerStats stats = statsManager.getPlayerInfo(player);
                player.sendMessage("uuid: " + stats.getUuid());
                player.sendMessage("name: " + stats.getName());
                player.sendMessage("last saved: " + stats.getLast_saved());
                player.sendMessage("level: " + stats.getLevel());
                player.sendMessage("total xp:  " + stats.getTotal_xp());
                player.sendMessage("base hp: " + stats.getBase_hp());
                player.sendMessage("base mana:  " + stats.getBase_mana());
                player.sendMessage("base stamina:  " + stats.getBase_stamina());
                player.sendMessage("base armor:  " + stats.getBase_armor());
                player.sendMessage("base power:  " + stats.getBase_power());
                player.sendMessage("max HP: " + stats.getMaxHP());
                player.sendMessage("max Mana: " + stats.getMaxMana());
                player.sendMessage("max Stamina: " + stats.getMaxStamina());
                player.sendMessage("current HP: " + stats.getCurrentHP());
                player.sendMessage("current Mana: " + stats.getCurrentMana());
                player.sendMessage("current stamina: " + stats.getCurrentStamina());
                player.sendMessage("armor: " + stats.getArmor());
                player.sendMessage("power: " + stats.getPower());
                player.sendMessage("attackSpeed: " + stats.getAttackSpeed());
            }
            case "getkit" -> {
                ItemStack helm = createRandomStatItem(Material.DIAMOND_HELMET);
                ItemStack chest = createRandomStatItem(Material.DIAMOND_CHESTPLATE);
                ItemStack legs = createRandomStatItem(Material.DIAMOND_LEGGINGS);
                ItemStack boots = createRandomStatItem(Material.DIAMOND_BOOTS);
                ItemStack swordMain = createRandomStatItem(Material.DIAMOND_SWORD);
                ItemStack swordOffhand = createRandomStatItem(Material.DIAMOND_SWORD);

                player.getInventory().addItem(helm, chest, legs, boots, swordMain, swordOffhand);
                player.sendMessage("You received a kit with random stats!");
            }
            case "showarmor" -> {
                EquipmentStats estats = equipmentManager.getCombinedStats(player);
                Plugin1.getInstance().getLogger().info(estats.toString());
            }
            case "showstats" -> {
                PlayerStats pstats = statsManager.getPlayerInfo(player);
                Plugin1.getInstance().getLogger().info(pstats.toString());
                player.sendMessage(pstats.toString());
            }
            case "loadarmor" -> {
                // TODO: this is a method I should put in a class see loadPlayerInfo in listeners
                PlayerStats currentPlayer = statsManager.getPlayerInfo(player);
                EquipmentManager equipment = new EquipmentManager();
                currentPlayer.addBaseEquipmentStats(equipment.getCombinedStats(player));
            }
            case "toggle" -> {
                Plugin1.getInstance().toggleflag = !Plugin1.getInstance().toggleflag;
            }
            case "maxatkspd" -> {
                AttributeInstance atkSpd = player.getAttribute(Attribute.ATTACK_SPEED);
                assert atkSpd != null; //eh
                if (atkSpd.getBaseValue() == 4.0) {
                    Plugin1.getInstance().getLogger().info("changing default to 1024");
                    atkSpd.setBaseValue(1024.0); // Very high number to remove cooldown practically
                } else {
                    Plugin1.getInstance().getLogger().info("changing 1024 to default");
                    atkSpd.setBaseValue(4.0);
                }
            }

            default -> player.sendMessage("Unknown action. Use show, add, or remove.");
        }

        return true;
    }

    private ItemStack createRandomStatItem(Material material) {
        ItemStack item = new ItemStack(material);

        double hp = Math.round(Math.random() * 5.0 * 1000.0) / 1000.0;
        double mana = Math.round(Math.random() * 5.0 * 1000.0) / 1000.0;
        double stamina = Math.round(Math.random() * 5.0 * 1000.0) / 1000.0;
        double armor = Math.round(Math.random() * 5.0 * 1000.0) / 1000.0;
        double power = Math.round(Math.random() * 5.0 * 1000.0) / 1000.0;
        double atkspd = Math.round(Math.random() * 1.0 * 1000.0) / 1000.0;

        NBT.modify(item, nbt -> {
            nbt.setDouble("eqHp", hp);
            nbt.setDouble("eqMana", mana);
            nbt.setDouble("eqStamina", stamina);
            nbt.setDouble("eqArmor", armor);
            nbt.setDouble("eqPower", power);
            nbt.setDouble("eqAtkspd", atkspd);
        });

        List<Component> lore2 = new ArrayList<>();
        lore2.add(Component.text("HP: " + String.format("%.2f", hp)));
        lore2.add(Component.text("Mana: " + String.format("%.2f", mana)));
        lore2.add(Component.text("Stamina: " + String.format("%.2f", stamina)));
        lore2.add(Component.text("Armor: " + String.format("%.2f", armor)));
        lore2.add(Component.text("Power: " + String.format("%.2f", power)));
        lore2.add(Component.text("AtkSpd: " + String.format("%.2f", atkspd)));

        item.lore(lore2);

        return item;
    }
}