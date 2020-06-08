package me.nitro.uhc.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class BoardLine {

    private ChatColor colour;
    private int line;
    private Team team;

    public BoardLine(ChatColor colour, int line, Team team) {
        this.colour = colour;
        this.line = line;
        this.team = team;
    }

    public ChatColor getColour() {
        return colour;
    }

    public int getLine() {
        return line;
    }

    public Team getTeam() {
        return team;
    }
}
