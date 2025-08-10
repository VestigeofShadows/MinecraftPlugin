package space.vestiges.plugin1.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.PlayerStats;
import space.vestiges.plugin1.PlayerStatsManager;
import space.vestiges.plugin1.PlayerStatsStorage;
import space.vestiges.plugin1.Plugin1;

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
            player.sendMessage("Usage: /teststats <show|add|remove|reloadme> don't run remove");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "show" -> {
                PlayerStats stats = statsManager.getPlayerInfo(player);
                player.sendMessage("Your stats: " + stats.getCurrentHP());
            }
            case "add" -> {
                // Example: Add 10 points to some stat
                PlayerStats stats = statsManager.getPlayerInfo(player);
                stats.setCurrentHP(stats.getCurrentHP() + 10);
                player.sendMessage("Added 10 points! New points: " + stats.getCurrentHP());
            }
            case "remove" -> {
                statsManager.removeActivePlayer(player);
                player.sendMessage("Your stats removed from memory. Not storage though");
            }
            case "reloadme" -> {
                // read from json to active
                // statsManager.addActivePlayer(player, statsStorage.getPlayerStoredStats(player));
                Plugin1.getInstance().getLogger().info("This command doesn't do shit rn");
            }

            default -> player.sendMessage("Unknown action. Use show, add, or remove.");
        }

        return true;
    }
}