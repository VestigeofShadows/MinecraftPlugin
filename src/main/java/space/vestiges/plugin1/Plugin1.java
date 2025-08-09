package space.vestiges.plugin1;

import space.vestiges.plugin1.commands.TestStatsCommand;
import space.vestiges.plugin1.listeners.PlayerListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin1 extends JavaPlugin {
    private static Plugin1 instance;
    private PlayerStatsManager statsManager;
    private PlayerStatsStorage statsStorage;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialization
        instance = this;
        statsStorage = new PlayerStatsStorage();
        statsManager = new PlayerStatsManager();
        statsStorage.initStorage(); // Creates Folder + player_stats.db if not exist

        //Listeners
        PlayerListeners listener = new PlayerListeners();
        getServer().getPluginManager().registerEvents(listener, this);

        //Commands
        this.getCommand("teststats").setExecutor(new TestStatsCommand(statsManager, statsStorage));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // TODO: Save from memory stats to json?? Not sure if I actually need to
    }

    public static Plugin1 getInstance() {
        return instance;
    }
    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }
    public PlayerStatsStorage getStorageManager() {
        return statsStorage;
    }

}
