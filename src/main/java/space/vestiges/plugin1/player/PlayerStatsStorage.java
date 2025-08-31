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
                total_xp REAL
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
        String sql = "INSERT OR REPLACE INTO player_stats (uuid, playername, last_saved, total_xp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, player.getUniqueId().toString());
            pstmt.setString(2, player.getName());
            pstmt.setInt(3, stats.getLast_saved());
            pstmt.setDouble(4, stats.getCombat_xp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // REMOVE PLAYER IN THE DATABASE for testing? TODO
    public void removeStoredPlayer(@NotNull Player player) {
        String sql = "DELETE FROM player_stats WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, player.getUniqueId().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public PlayerStats getPlayerStoredStats(@NotNull Player player) {
        String sql = "SELECT uuid, playername, last_saved, total_xp FROM player_stats WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new PlayerStats(
                        rs.getString("uuid"),
                        rs.getString("playername"),
                        rs.getDouble("total_xp"),
                        rs.getInt("last_saved")
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
