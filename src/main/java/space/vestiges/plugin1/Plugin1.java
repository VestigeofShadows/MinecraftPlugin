package space.vestiges.plugin1;

import space.vestiges.plugin1.listeners.PlayerListeners;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public final class Plugin1 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PlayerListeners listener = new PlayerListeners(this);
        getServer().getPluginManager().registerEvents(listener, this);

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
            // If no storage, create storage json file
        }
        File jsonFile = new File(dataFolder, "player_stats.json");
        if(!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
                try (FileWriter writer = new FileWriter(jsonFile)) {
                    writer.write("{}");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
