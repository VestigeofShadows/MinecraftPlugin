package space.vestiges.plugin1.adapterlayer;

import space.vestiges.plugin1.adapterlayer.commands.TestStatsCommand;
import space.vestiges.plugin1.adapterlayer.listeners.MobListener;
import space.vestiges.plugin1.adapterlayer.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import space.vestiges.plugin1.adapterlayer.schedulers.GlobalTasks;
import space.vestiges.plugin1.adapterlayer.visualUtils.PlayerScoreBoard;
import space.vestiges.plugin1.domainlayer.player.PlayerStatsManager;

import java.util.Objects;

public final class Plugin1 extends JavaPlugin {

    private static Plugin1 instance;
    private PlayerStatsManager statsManager;
    private PlayerScoreBoard boardsManager;
    private GlobalTasks globalTasks;
    public boolean toggleflag = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialization
        instance = this;
        statsManager = new PlayerStatsManager();
        boardsManager = new PlayerScoreBoard(statsManager);


        globalTasks = new GlobalTasks();
        globalTasks.startGlobalActionBarTask(this);

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
    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }
    public PlayerScoreBoard getBoardsManager() { return boardsManager; }
    public GlobalTasks getPlayerHud() { return globalTasks; }
}
