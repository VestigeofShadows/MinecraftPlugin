package space.vestiges.plugin1;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import space.vestiges.plugin1.commands.TestStatsCommand;
import space.vestiges.plugin1.listeners.MobListener;
import space.vestiges.plugin1.listeners.PlayerListeners;
import org.bukkit.plugin.java.JavaPlugin;
import space.vestiges.plugin1.player.PlayerStatsManager;
import space.vestiges.plugin1.player.PlayerStatsStorage;

public final class Plugin1 extends JavaPlugin {
    private static Plugin1 instance;
    private ProtocolManager protocolManager;
    private PlayerStatsManager statsManager;
    private PlayerStatsStorage statsStorage;
    public boolean toggleflag = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialization
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        statsStorage = new PlayerStatsStorage();
        statsManager = new PlayerStatsManager();
        statsStorage.initStorage(); // Creates Folder + player_stats.db if not exist

        //Listeners
        PlayerListeners listener = new PlayerListeners();
        MobListener mobListener = new MobListener();
        getServer().getPluginManager().registerEvents(listener, this);
        getServer().getPluginManager().registerEvents(mobListener, this);

        //Commands
        this.getCommand("p").setExecutor(new TestStatsCommand(statsManager, statsStorage));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // TODO: Save from memory stats to db?? Not sure if I actually need to do this
    }

    public static Plugin1 getInstance() {
        return instance;
    }
    public ProtocolManager getProtocolManager() { return protocolManager; }
    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }
    public PlayerStatsStorage getStorageManager() {
        return statsStorage;
    }
}
