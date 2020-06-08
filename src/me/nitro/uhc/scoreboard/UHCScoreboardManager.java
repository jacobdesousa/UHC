package me.nitro.uhc.scoreboard;

import me.nitro.uhc.GameManager;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import me.nitro.uhc.features.Border;
import me.nitro.uhc.features.KillCounter;
import me.nitro.uhc.teams.Team;
import me.nitro.uhc.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sun.text.SupplementaryCharacterData;

import java.util.HashMap;
import java.util.UUID;

public class UHCScoreboardManager implements Listener {

    private static UHC uhc;

    public UHCScoreboardManager(UHC uhc) {
        this.uhc = uhc;
    }

    static HashMap<UUID, UHCScoreboard> scoreboards = new HashMap<>();

    public static void initScoreboard() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            updateScoreboard();
        }, 0, 20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        scoreboards.put(e.getPlayer().getUniqueId(), new UHCScoreboard(Utils.colour("&c&lNitro UHC")));
        e.getPlayer().setScoreboard(scoreboards.get(e.getPlayer().getUniqueId()).getScoreboard());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        scoreboards.remove(e.getPlayer().getUniqueId());
    }

    public static void updateScoreboard() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (GameManager.getAlivePlayers() == null || !GameManager.getAlivePlayers().contains(p.getUniqueId())) {
                UHCScoreboard scoreboard = scoreboards.get(p.getUniqueId());
                scoreboard.setValue(15, "&7&m------------", "&7&m--------");
                scoreboard.setValue(14, " ", "");
                scoreboard.setValue(13, "&cOnline: &7" + Bukkit.getOnlinePlayers().size(), "");
                scoreboard.setValue(12, "  ", "");
                scoreboard.setValue(11, "&cBorder: &7" + Border.getCurrentSize(), "");
                String typeString;
                if (TeamManager.getTeamLimit() <= 1) {
                    typeString = "FFA";
                } else {
                    typeString = "To" + TeamManager.getTeamLimit();
                }
                scoreboard.setValue(10, "&cType: &7" + typeString, "");
                scoreboard.setValue(9, "   ", "");
                scoreboard.setValue(8, "&7&m------------", "&7&m--------");
            } else if (TeamManager.getTeamLimit() <= 1) {
                UHCScoreboard scoreboard = scoreboards.get(p.getUniqueId());
                scoreboard.setValue(15, "&7&m------------", "&7&m--------");
                scoreboard.setValue(14, " ", "");
                scoreboard.setValue(13, "&cGame Time: ", "&7" + GameManager.getElapsedTime());
                scoreboard.setValue(12, "&cPlayers Left: ", "&7" + GameManager.getAlivePlayers().size());
                scoreboard.setValue(11, "&cKills: ", "&7" + KillCounter.getKills(p.getUniqueId()));
                scoreboard.setValue(10, "&cBorder: &7" + Border.getCurrentSize(), "");
                scoreboard.setValue(9, "   ", "");
                scoreboard.setValue(8, "&7&m------------", "&7&m--------");
            } else if (TeamManager.getTeamLimit() > 1) {
                UHCScoreboard scoreboard = scoreboards.get(p.getUniqueId());
                scoreboard.setValue(15, "&7&m------------", "&7&m--------");
                scoreboard.setValue(14, " ", "");
                scoreboard.setValue(13, "&cGame Time: ", "&7" + GameManager.getElapsedTime());
                scoreboard.setValue(12, "&cPlayers Left: ", "&7" + GameManager.getAlivePlayers().size());
                scoreboard.setValue(11, "&cTeams Left: ", "&7" + Team.getAllTeams().size());
                scoreboard.setValue(10, "&cBorder: &7" + Border.getCurrentSize(), "");
                scoreboard.setValue(9, "   ", "");
                scoreboard.setValue(8, "&cKills: ", "&7" + KillCounter.getKills(p.getUniqueId()));
                scoreboard.setValue(7, "&cTeam Kills: ", "&7" + Team.getTeamKills(p));
                scoreboard.setValue(6, "   ", "");
                scoreboard.setValue(5, "&7&m------------", "&7&m--------");
            } else {
                UHCScoreboard scoreboard = scoreboards.get(p.getUniqueId());
                scoreboard.setValue(15, "&7&m-------------", "&7&m-------");
                scoreboard.setValue(14, " ", "");
                scoreboard.setValue(13, "&cOnline: &7" + Bukkit.getOnlinePlayers().size(), "");
                scoreboard.setValue(12, " ", "");
                scoreboard.setValue(11, "&7&m-------------", "&7&m-------");
            }
        }
    }

}
