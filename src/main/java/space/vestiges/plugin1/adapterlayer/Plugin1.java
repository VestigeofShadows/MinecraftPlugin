package space.vestiges.plugin1.adapterlayer;

import space.vestiges.plugin1.adapterlayer.commands.TestStatsCommand;
import space.vestiges.plugin1.adapterlayer.listeners.MobListener;
import space.vestiges.plugin1.adapterlayer.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import space.vestiges.plugin1.adapterlayer.schedulers.PlayerHud;
import space.vestiges.plugin1.applicationlayer.PlayerStatsManager;

import java.util.Objects;

public final class Plugin1 extends JavaPlugin {

    private static Plugin1 instance;
    private PlayerStatsManager statsManager;
    private PlayerHud playerHud;
    public boolean toggleflag = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialization
        instance = this;
        statsManager = new PlayerStatsManager();


        //playerHud = new PlayerHud();
        //playerHud.startGlobalActionBarTask(this);

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new MobListener(), this);

        Objects.requireNonNull(this.getCommand("p")).setExecutor(new TestStatsCommand(statsManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // TODO: Save from memory stats to db?? Not sure if I actually need to do this
    }

    public static Plugin1 getInstance() {
        return instance;
    }
    // public ProtocolManager getProtocolManager() { return protocolManager; }
    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }
    public PlayerHud getPlayerHud() { return playerHud; }
}
