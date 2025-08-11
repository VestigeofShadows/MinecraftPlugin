package space.vestiges.plugin1.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.Plugin1;

import java.io.*;
import java.sql.*;

public class PlayerStatsStorage {

    // ----------------------------------- SEPARATION DB -----------------------------------------

    private Connection connection;
    private void connect() {
        try {
            File dbFile = new File(Plugin1.getInstance().getDataFolder(), "player_stats.db");
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
                last_saved INTEGER,
                total_xp REAL,
                base_hp REAL,
                base_mana REAL,
                base_stamina REAL,
                base_armor REAL,
                base_power REAL,
                base_haste REAL
            );
        """;
        try  (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // ADD PLAYER TO DATABASE
    public void addStoredPlayer(@NotNull Player player, PlayerStats stats) {
        String sql = "INSERT OR REPLACE INTO player_stats (uuid, playername, last_saved, total_xp, base_hp, base_mana, base_stamina, base_armor, base_power, base_haste) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, player.getUniqueId().toString());
            pstmt.setString(2, player.getName());
            pstmt.setInt(3, stats.getLast_saved());
            pstmt.setDouble(4, stats.getTotal_xp());
            pstmt.setDouble(5, stats.getBase_hp());
            pstmt.setDouble(6, stats.getBase_mana());
            pstmt.setDouble(7, stats.getBase_stamina());
            pstmt.setDouble(8, stats.getBase_armor());
            pstmt.setDouble(9, stats.getBase_power());
            pstmt.setDouble(10, stats.getBase_haste());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // REMOVE PLAYER IN THE DATABASE for testing? TODO
    public void removeStoredPlayer(@NotNull Player player) {
        String sql = "";
    }
    public PlayerStats getPlayerStoredStats(@NotNull Player player) {
        String sql = "SELECT uuid, playername, last_saved, total_xp, base_hp, base_mana, base_stamina, base_armor, base_power, base_haste FROM player_stats WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new PlayerStats(
                        rs.getString("uuid"),
                        rs.getString("playername"),
                        rs.getInt("last_saved"),
                        rs.getDouble("total_xp"),
                        rs.getDouble("base_hp"),
                        rs.getDouble("base_mana"),
                        rs.getDouble("base_stamina"),
                        rs.getDouble("base_armor"),
                        rs.getDouble("base_power"),
                        rs.getDouble("base_haste")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Boolean playerExists(@NotNull Player player) {
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
    public void initStorage() {
        connect();
        createTable();
    }

    // ----------------------------------- SEPARATION DB -----------------------------------------
}
