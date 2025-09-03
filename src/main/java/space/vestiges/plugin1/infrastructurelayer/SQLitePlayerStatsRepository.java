package space.vestiges.plugin1.infrastructurelayer;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.applicationlayer.persistentdata.PlayerStatsData;
import space.vestiges.plugin1.applicationlayer.persistentdata.PlayerStatsRepository;
import space.vestiges.plugin1.domainlayer.model.player.PlayerStats;

import java.io.*;
import java.sql.*;
import java.util.UUID;

public class SQLitePlayerStatsRepository implements PlayerStatsRepository {

    private Connection connection;
    private final String fileName = "player_stats.db";

    public SQLitePlayerStatsRepository() {
        initStorage();
    }

    private void connect() {
        try {
            File dbFile = new File(Plugin1.getInstance().getDataFolder(), fileName);
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

    @Override
    public void initStorage() {
        connect();
        createTable();
    }


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

    @Override
    public boolean playerExists(@NotNull UUID uuid) {
        String sql = "SELECT 1 FROM player_stats WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * This method updates a player's stats data into the database,
     * and creates a new entry if it doesn't exist yet.
     * @param stats the player's stats to save (as a DTO)
     */
    @Override
    public void save(PlayerStatsData stats) {
        String sql = """
        INSERT INTO player_stats (uuid, playername, last_saved, total_xp) 
        VALUES (?, ?, ?, ?)
        ON CONFLICT(uuid) DO UPDATE SET
                             playername = excluded.playername,
                             last_saved = excluded.last_saved,
                             total_xp = excluded.total_xp;
        """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, stats.getUuid().toString());
            pstmt.setString(2, stats.getPlayername());
            pstmt.setInt(3, stats.getLast_saved());
            pstmt.setDouble(4, stats.getTotal_xp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads a player's persistent stats from the database,
     * and returns a PlayerStatsData object.
     * @param uuid the uuid of the player
     * @return the PlayerStatsData (DTO) of the player
     */
    @Override
    public PlayerStatsData load(UUID uuid) {
        String sql = """
                SELECT uuid, playername, last_saved, total_xp
                FROM player_stats
                WHERE uuid = ?;
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new PlayerStatsData(
                        rs.getString("uuid"),
                        rs.getString("playername"),
                        rs.getInt("last_saved"),
                        rs.getDouble("total_xp")
                );
            } else {
                throw new IllegalArgumentException("No row exists for UUID: " + uuid);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
