package space.vestiges.plugin1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsStorage {

    private File pluginFolder; // TODO This can be moved to folder generation class
    private File pluginFile;
    private final HashMap<UUID, PlayerStats> storedPlayers;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    // ----------------------------------- SEPARATION DB -----------------------------------------

    private Connection connection;
    private void connect() {
        try {
            File dbFile = new File(pluginFolder, "player_stats.db");
            if (!dbFile.getParentFile().exists()) {
                dbFile.getParentFile().mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_stats (
                uuid TEXT PRIMARY KEY,
                playername TEXT,
                hp INTEGER,
                mana INTEGER,
                defense INTEGER
            );
        """;
        try  (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStoredPlayer1(@NotNull Player player, PlayerStats stats) {
        String sql = "INSERT OR REPLACE INTO player_stats (uuid, playername, hp, mana, defense) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, player.getUniqueId().toString());
/*            pstmt.setInt(2, stats.getHp());
            pstmt.setInt(3, stats.getMana());
            pstmt.setInt(4, stats.getDefense());*/
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerStats getPlayerStoredStats1(@NotNull Player player) {
        String sql = "SELECT playername, hp, mana, defense FROM player_stats WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new PlayerStats(
                        rs.getString("name")        /* ,
                        rs.getInt("mana"),
                        rs.getInt("defense")                   */
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean playerExists2(@NotNull Player player) {
        String sql = "SELECT 1 FROM player_stats WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void initStorage2() {
        connect();
        createTable();
    }

    // ----------------------------------- SEPARATION DB -----------------------------------------

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
    public void addStoredPlayer(@NotNull Player player, PlayerStats playerStats) {
        UUID uuid = player.getUniqueId();
        storedPlayers.put(uuid, playerStats);
        saveData();
    }
    public void editStoredPlayer(@NotNull Player player, PlayerStats playerStats) {
        storedPlayers.put(player.getUniqueId(), playerStats);
    }
    public void saveData() {
        try (Writer writer = new FileWriter(pluginFile)){
            gson.toJson(storedPlayers, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
