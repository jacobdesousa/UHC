package me.nitro.uhc.scoreboard;

import me.nitro.uhc.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UHCScoreboard implements Listener {

    private final List<ChatColor> colors = Arrays.asList(ChatColor.values());

    private Scoreboard scoreboard;
    private Objective objective;

    private List<BoardLine> boardLines = new ArrayList<>();

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public UHCScoreboard(String displayName) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Utils.colour(displayName));
        for(int i=0; i < colors.size(); i++) {
            final ChatColor color = colors.get(i);
            final Team team = scoreboard.registerNewTeam("line" + i);
            team.addEntry(color.toString());
            boardLines.add(new BoardLine(color, i, team));
        }
    }

    private BoardLine getBoardLine(int line) {
        return boardLines.stream().filter(boardLine -> boardLine.getLine() == line).findFirst().orElse(null);
    }

    public void setValue(int line, String prefix, String suffix) {
        final BoardLine boardLine = getBoardLine(line);
        Validate.notNull(boardLine, "Unable to find BoardLine with index of " + line + ".");
        objective.getScore(boardLine.getColour().toString()).setScore(line);
        boardLine.getTeam().setPrefix(Utils.colour(prefix));
        boardLine.getTeam().setSuffix(Utils.colour(suffix));
    }

    public void removeLine(int line) {
        final BoardLine boardLine = getBoardLine(line);
        Validate.notNull(boardLine, "Unable to find BoardLine with index of " + line + ".");
        scoreboard.resetScores(boardLine.getColour().toString());
    }
}
