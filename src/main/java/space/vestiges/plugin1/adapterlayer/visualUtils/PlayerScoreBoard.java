package space.vestiges.plugin1.adapterlayer.visualUtils;

import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import space.vestiges.plugin1.adapterlayer.Plugin1;
import space.vestiges.plugin1.domainlayer.player.PlayerStatsManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Deals with player scoreboards
 */
public class PlayerScoreBoard {

    private final Map<UUID, FastBoard> boards;
    private final PlayerStatsManager statsManager;
    private int counter;

    public PlayerScoreBoard(PlayerStatsManager statsManager) {
        boards = new HashMap<>();
        this.statsManager = statsManager;
    }

    public void addPlayerScoreBoard(Player player) {
        FastBoard board = new FastBoard(player);
        Component title = Component.text("My Stats", NamedTextColor.GREEN);
        board.updateTitle(title);

        boards.put(player.getUniqueId(), board);
    }

    public void removePlayerScoreBoard(Player player) {
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete(); // this is because fastboard does some extra stuff
        }
    }

    public void updateScoreBoard(Player player) {
        FastBoard board = boards.get(player.getUniqueId());

        if (board == null) {
            Plugin1.getInstance().getLogger().info("No ScoreBoard for play exists");

        } else {
            counter += 1;
            Component Lines = Component.text("Updating: " + counter);
            board.updateLines(
                    Lines,
                    Component.text("combatXP: " + statsManager.getPlayerStats(player).getCombat_xp()),
                    Component.text("Level: " + statsManager.getPlayerStats(player).getCombatLevel()),
                    Component.text("basehp: " + statsManager.getPlayerStats(player).getBase_hp()),
                    Component.text("basehpregen: " + statsManager.getPlayerStats(player).getBase_hp_regen()),
                    Component.text("basemana: " + statsManager.getPlayerStats(player).getBase_mana()),
                    Component.text("basemanaregen: " + statsManager.getPlayerStats(player).getBase_mana_regen()),
                    Component.text("basearmor: " + statsManager.getPlayerStats(player).getBase_armor()),
                    Component.text("basepower: " + statsManager.getPlayerStats(player).getBase_power())
            );
        }
    }

}
