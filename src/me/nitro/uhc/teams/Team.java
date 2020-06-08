package me.nitro.uhc.teams;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import me.nitro.uhc.features.KillCounter;
import me.nitro.uhc.scenarios.Backpacks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {

    //Hash stores the team captain name as an ID, and the players in the team in the arraylist.
    private static HashMap<String, ArrayList<String>> teams = new HashMap<>();

    //Hash stores active invites.
    private static HashMap<String, ArrayList<String>> invites = new HashMap<>();

    //Method used for creating a new team.
    public static void newTeam(String leaderName) {
        teams.put(leaderName, new ArrayList<String>()); //When a team is initialized, a new hash is inserted with the captainName as the key, and a new list of strings for the team's contents as the value.
        teams.get(leaderName).add(leaderName); //The captain is added to the list of players that corresponds with their team.
    }

    //Method used for getting the team of any player. Returns null if no team is found.
    public static String getTeam(String name) {
        for (String teamKey : teams.keySet()) { //Iterates through all teams using the keySet
            if (teams.get(teamKey).contains(name)) { //Checks if the current value contains the specified player
                return teamKey; //If the player is found, the player's team is returned;
            }
        }
        return null; //If there is no result after all keys were checked, null is returned indicating that the player doesn't not have a team.
    }

    //Method used for getting the members in a team.
    public static ArrayList<String> getMembers(String teamKey) {
        return teams.get(teamKey); //Returns the list of players for the specified team; including the leader.
    }

    //Method used for removing a team from existence.
    public static void deleteTeam(String name) {
        for (String teamKey : teams.keySet()) { //Iterates through all teams using the keySet
            if (teams.get(teamKey).contains(name)) { //Checks if current value contains the player
                ArrayList<String> members = teams.get(teamKey);
                for (String memberName : members) {
                    Bukkit.getPlayer(memberName).sendMessage(UHC.getMessage("teamDisbanded"));
                }
                Backpacks.removeBackpack(teamKey);
                teams.remove(teamKey); //Deletes the team from storage
                return;
            }
        }
    }

    //Method used for removing a player from a team.
    public static void removeFromTeam(String name) {
        if (isTeamLeader(name)) { //Checks if the player is a team leader.
            deleteTeam(name); //Deletes the team completely if the player was a team leader.
        } else {
            for (String member : teams.get(getTeam(name))) {
                Bukkit.getPlayer(member).sendMessage(UHC.getPrefix() + Utils.colour("&c" + name + " &7has left your team."));
            }
            teams.get(getTeam(name)).remove(name); //Removes the player from their team if they were not a team leader.
            Bukkit.getPlayer(name).sendMessage(UHC.getMessage("kickedFromTeam"));
            /*for (String teamKey : teams.keySet()) { //Iterates through all teams using the keySet
                if (teams.get(teamKey).contains(name)) { //Checks if current value contains the player
                    ArrayList<String> members = teams.get(teamKey);
                    for (String memberName : members) {
                        Bukkit.getPlayer(memberName).sendMessage(UHC.getPrefix() + Utils.colour("&c" + name + " &7has left your team."));
                    }
                }
            }*/
        }
    }

    //Method used for placing players on teams.
    public static void joinTeam(String inviter, String invitee) {
        Bukkit.getPlayer(invitee).sendMessage(UHC.getPrefix() + Utils.colour("&7You have joined &c" + inviter + "'s &7team."));
        for (String memberName : teams.get(inviter)) {
            Bukkit.getPlayer(memberName).sendMessage(UHC.getPrefix() + Utils.colour("&c" + invitee + " &7has joined your team."));
        }
        teams.get(getTeam(inviter)).add(invitee); //Puts the invitee in the team of the inviter.
        for (String inviters : invites.keySet()) {
            ArrayList<String> removeList = new ArrayList<>();
            for (String invitees : invites.get(inviters)) {
                if (invitees.equalsIgnoreCase(invitee)) {
                    removeList.add(invitees);
                }
            }
            invites.get(inviters).removeAll(removeList);
        }
        //invites.get(inviter).remove(invitee); //Removes the active invite for the invitee.
    }

    //Method used for creating and storing an invite.
    public static void newInvite(String inviter, String invitee) {
        if (invites.get(inviter) != null) { //Checks if the inviter already has active invites.
            Bukkit.getPlayer(inviter).sendMessage(UHC.getPrefix() + Utils.colour("&7You have invited &c" + invitee + " &7to join your team."));
            Bukkit.getPlayer(invitee).sendMessage(UHC.getPrefix() + Utils.colour("&c" + inviter + " &7has invited you to join their team."));
            Bukkit.getPlayer(invitee).sendMessage(Utils.colour("&7Type &c/team accept " + inviter + " &7to join their team."));
            invites.get(inviter).add(invitee); //Adds invitee to inviter's active invites.
        } else { //If the inviter doesn't already have active invites.
            Bukkit.getPlayer(inviter).sendMessage(UHC.getPrefix() + Utils.colour("&7You have invited &c" + invitee + " &7to join your team."));
            Bukkit.getPlayer(invitee).sendMessage(UHC.getPrefix() + Utils.colour("&c" + inviter + " &7has invited you to join their team."));
            Bukkit.getPlayer(invitee).sendMessage(Utils.colour("&7Type &c/team accept " + inviter + " &7to join their team."));
            invites.put(inviter, new ArrayList<String>()); //A new list is added to the hash for that inviter.
            invites.get(inviter).add(invitee); //Adds invitee to inviter's active invites.
        }
    }

    //Method used for checking if a player has a pending invite.
    public static boolean hasInvite(String inviter, String invitee) {
        for (String inviteKey : invites.keySet()) { //Iterates through all teams using the keySet
            if (inviteKey.equalsIgnoreCase(inviter)) { //Checks if the current key contains the inviter
                if (invites.get(inviteKey).contains(invitee)) { //Checks if the inviter has invited the invitee to the team
                    return true;
                }
            }
        }
        return false;
    }

    //Method used for checking if a player is a team leader.
    public static boolean isTeamLeader(String name) {
        for (String teamKey : teams.keySet()) { //Iterates through all teams using the keySet
            if (teamKey.equalsIgnoreCase(name)) { //Checks if the team captain is equal to the query.
                return true;
            }
        }
        return false;
    }

    //Method used for resetting teams and invites.
    public static void reset() {
        teams.clear();
        invites.clear();
    }

    //Returns the hash of all the teams.
    public static HashMap<String, ArrayList<String>> getAllTeams() {
        return teams;
    }

    public static int getTeamKills(Player p) {
        int teamKills = 0;
        for (String member : teams.get(getTeam(p.getName()))) {
            teamKills = teamKills + KillCounter.getKills(Bukkit.getOfflinePlayer(member).getUniqueId());
        }
        return teamKills;
    }
}
