package space.vestiges.plugin1;

import space.vestiges.plugin1.listeners.PlayerListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin1 extends JavaPlugin {
    private static Plugin1 instance;
    private PlayerStatsManager statsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialization
        instance = this;
        FileManager fileManager = new FileManager();

        // Create Folder and File if they don't exist TODO: Separate the folder and file later
        fileManager.initStorage();

        // Create in memory player stats
        statsManager = new PlayerStatsManager();

        PlayerListeners listener = new PlayerListeners();
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // TODO: Save from memory to json
    }

    public static Plugin1 getInstance() {
        return instance;
    }

    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }
}
