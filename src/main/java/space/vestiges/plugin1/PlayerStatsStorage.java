package space.vestiges.plugin1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsStorage {

    private File pluginFolder; // TODO This can be moved to folder generation class
    private File pluginFile;
    private final HashMap<UUID, PlayerStats> storedPlayers;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Constructor sets pluginFolder, and pluginFile
    public PlayerStatsStorage(){
        this.pluginFolder = Plugin1.getInstance().getDataFolder();
        this.pluginFile = new File(pluginFolder, "player_stats.json");
        this.storedPlayers = new HashMap<>();
    }

    // Method that checks folder/file, and load storedPlayers
    public void initStorage() {
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
            // If no storage, create storage json file
        }
        if(!pluginFile.exists()) {
            try {
                pluginFile.createNewFile();
                try (FileWriter writer = new FileWriter(pluginFile)) {
                    writer.write("{}");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        // LOAD storedPlayers into HashMap<>()
        try (Reader reader = new FileReader(pluginFile)) {
            Type type = new TypeToken<HashMap<UUID, PlayerStats>>() {}.getType();
            HashMap<UUID, PlayerStats> data = gson.fromJson(reader, type);

            if (data != null) {
                storedPlayers.clear();
                storedPlayers.putAll(data);
            } else {
                Plugin1.getInstance().getLogger().warning("Failed to load player stats from file!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkFolder() {
        return pluginFolder.exists();
    }
    public Boolean checkFile() {
        return pluginFile.exists();
    }

    // Getters
    public File getPluginFolder() {
        return pluginFolder;
    }
    public File getPluginFile() {
        return pluginFile;
    }

    public Boolean playerExists(@NotNull Player player) {
        if (checkFile()) {
            UUID uuid = player.getUniqueId();
            return storedPlayers.containsKey(uuid);
        } else {
            Plugin1.getInstance().getLogger().warning("Man did you delete the entire database?");
            return false;
        }
    }
    public PlayerStats getPlayerStoredStats(@NotNull Player player) {
        if (checkFile()) {
            UUID uuid = player.getUniqueId();
            return storedPlayers.get(uuid);
        } else {
            Plugin1.getInstance().getLogger().warning("Man did you delete the entire database?");
            return null;
        }
    }
    // Add player to hashmap and reflect change in database
    public void addStoredPlayer(@NotNull Player player, PlayerStats playerStats) {
        UUID uuid = player.getUniqueId();
        storedPlayers.put(uuid, playerStats);
        saveData();
    }
    public void saveData() {
        try (Writer writer = new FileWriter(pluginFile)){
            gson.toJson(storedPlayers, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
