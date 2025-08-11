package space.vestiges.plugin1.commands;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.PlayerStats;
import space.vestiges.plugin1.PlayerStatsManager;
import space.vestiges.plugin1.PlayerStatsStorage;
import space.vestiges.plugin1.Plugin1;
import space.vestiges.plugin1.equipment.EquipmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestStatsCommand implements CommandExecutor {

    private final PlayerStatsManager statsManager;
    private final PlayerStatsStorage statsStorage;

    public TestStatsCommand(PlayerStatsManager statsManager, PlayerStatsStorage statsStorage) {
        this.statsManager = statsManager;
        this.statsStorage = statsStorage;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage: /p <show|add|remove|reloadme> don't run remove");
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
                player.sendMessage("base haste:  " + stats.getBase_haste());
                player.sendMessage("max HP: " + stats.getMaxHP());
                player.sendMessage("max Mana: " + stats.getMaxMana());
                player.sendMessage("max Stamina: " + stats.getMaxStamina());
                player.sendMessage("current HP: " + stats.getCurrentHP());
                player.sendMessage("current Mana: " + stats.getCurrentMana());
                player.sendMessage("current stamina: " + stats.getCurrentStamina());
                player.sendMessage("armor: " + stats.getArmor());
                player.sendMessage("power: " + stats.getPower());
                player.sendMessage("haste: " + stats.getHaste());
            }
            case "add" -> {
                // Example: Add 10 points to some stat don't use
                PlayerStats stats = statsManager.getPlayerInfo(player);
                stats.setCurrentHP(stats.getCurrentHP() + 10);
                player.sendMessage("Added 10 points! New points: " + stats.getCurrentHP());
            }
            case "remove" -> {
                // don't use unless u want the thing break lmao
                statsManager.removeActivePlayer(player);
                player.sendMessage("Your stats removed from memory. Not storage though");
            }
            case "reloadme" -> {
                // read from json to active
                // statsManager.addActivePlayer(player, statsStorage.getPlayerStoredStats(player));
                Plugin1.getInstance().getLogger().info("This command doesn't do shit rn");
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
                EquipmentManager equipment = new EquipmentManager();
                equipment.getCombinedStats(player);
            }

            default -> player.sendMessage("Unknown action. Use show, add, or remove.");
        }

        return true;
    }

    private ItemStack createRandomStatItem(Material material) {
        ItemStack item = new ItemStack(material);

        double hp = Math.random()*5;
        double mana = Math.random()*5;
        double stamina = Math.random()*5;
        double armor = Math.random()*5;
        double power = Math.random()*5;
        double haste = Math.random()*5;

        NBT.modify(item, nbt -> {
            nbt.setDouble("hp", hp);
            nbt.setDouble("mana", mana);
            nbt.setDouble("stamina", stamina);
            nbt.setDouble("armor", armor);
            nbt.setDouble("power", power);
            nbt.setDouble("haste", haste);
        });

        List<String> lore = new ArrayList<>();
        lore.add("HP: " + String.format("%.2f", hp));
        lore.add("Mana: " + String.format("%.2f", mana));
        lore.add("Stamina: " + String.format("%.2f", stamina));
        lore.add("Armor: " + String.format("%.2f", armor));
        lore.add("Power: " + String.format("%.2f", power));
        lore.add("Haste: " + String.format("%.2f", haste));

        item.setLore(lore);

        return item;
    }
}