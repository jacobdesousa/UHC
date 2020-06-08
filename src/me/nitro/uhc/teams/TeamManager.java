package me.nitro.uhc.teams;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamManager implements CommandExecutor {

    boolean tmanage = false;
    private static int tlimit = 0;

    public static int getTeamLimit() {
        return tlimit;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("team")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(UHC.getMessage("playerOnly"));
                return false;
            }
            Player p = (Player) sender;
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if (p.hasPermission("uhc.admin")) {
                    p.sendMessage(UHC.getRawMessage("adminHelp"));
                } else {
                    p.sendMessage(UHC.getRawMessage("playerHelp"));
                }
                return false;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {
                if (tmanage == false) {
                    p.sendMessage(UHC.getMessage("managementDisabled"));
                    return false;
                }
                if (tlimit == 0) {
                    p.sendMessage(UHC.getMessage("ffaGame"));
                    return false;
                }
                if (Bukkit.getPlayer(args[1]) == null) {
                    p.sendMessage(UHC.getMessage("invalidPlayer"));
                    return false;
                }
                String inviter = p.getName();
                String invitee = Bukkit.getPlayer(args[1]).getName();
                if (inviter.equalsIgnoreCase(invitee)) {
                    p.sendMessage(UHC.getMessage("noSelfInvite"));
                    return false;
                }
                if (Team.getTeam(invitee) != null) {
                    p.sendMessage(UHC.getPrefix() + Utils.colour("&c" + invitee + "&7 is already on a team."));
                    return false;
                }
                if (Team.getTeam(inviter) == null) {
                    Team.newTeam(inviter);
                    Team.newInvite(inviter, invitee);
                } else if (Team.isTeamLeader(inviter)) {
                    if (Team.getMembers(inviter).size() == tlimit) {
                        p.sendMessage(UHC.getMessage("teamLimitReached"));
                        return false;
                    }
                    Team.newInvite(inviter, invitee);
                } else if (!Team.isTeamLeader(inviter)) {
                    p.sendMessage(UHC.getMessage("failedInviteNotLeader"));
                }
                return false;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
                if (!tmanage) {
                    p.sendMessage(UHC.getMessage("managementDisabled"));
                    return false;
                }
                if (Bukkit.getPlayer(args[1]) == null) {
                    p.sendMessage(UHC.getMessage("invalidPlayer"));
                    return false;
                }
                String inviter = Bukkit.getPlayer(args[1]).getName();
                String invitee = p.getName();
                if (Team.hasInvite(inviter, invitee)) {
                    if (Team.getMembers(inviter).size() == tlimit) {
                        p.sendMessage(UHC.getMessage("teamLimitReached"));
                        Bukkit.getPlayer(invitee).sendMessage(UHC.getMessage("teamFull"));
                        return false;
                    }
                    Team.joinTeam(inviter, invitee);
                } else {
                    p.sendMessage(UHC.getPrefix() + Utils.colour("&7You do not have an invite from &c" + inviter + "&7."));
                    return false;
                }
                return false;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                if (!tmanage) {
                    p.sendMessage(UHC.getMessage("managementDisabled"));
                    return false;
                }
                if (Team.getTeam(p.getName()) == null) {
                    p.sendMessage(UHC.getMessage("noTeam"));
                } else {
                    Team.removeFromTeam(p.getName());
                }
                return false;
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
                if (args.length == 1) {
                    if (Team.getTeam(p.getName()) == null) {
                        p.sendMessage(UHC.getMessage("noTeam"));
                    } else {
                        String leaderName = Team.getTeam(p.getName());
                        ArrayList<String> members = new ArrayList<>();
                        for (String pName : Team.getMembers(Team.getTeam(p.getName()))) {
                            if (!(pName == Team.getTeam(p.getName()))) {
                                members.add(Bukkit.getPlayer(pName).getName());
                            }
                        }
                        String[] memberArray = members.toArray(new String[0]);
                        printTeamList(p.getName(), leaderName, memberArray);
                    }
                } else if (args.length == 2) {
                    if (Team.getTeam(args[1]) == null) {
                        p.sendMessage(UHC.getMessage("playerNoTeam"));
                    } else {
                        String leaderName = Team.getTeam(args[1]);
                        ArrayList<String> members = new ArrayList<>();
                        for (String pName : Team.getMembers(Team.getTeam(args[1]))) {
                            if (!(pName == Team.getTeam(args[1]))) {
                                members.add(Bukkit.getPlayer(pName).getName());
                            }
                        }
                        String[] memberArray = members.toArray(new String[0]);
                        printTeamList(p.getName(), leaderName, memberArray);
                    }
                }
                return false;
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("management")) {
                if (p.hasPermission("uhc.admin")) {
                    if (args.length == 2 && args[1].equalsIgnoreCase("on")) {
                        p.sendMessage(UHC.getMessage("tManageOn"));
                        tmanage = true;
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("off")) {
                        p.sendMessage(UHC.getMessage("tManageOff"));
                        tmanage = false;
                    } else if (args.length == 1) {
                        if (tmanage) {
                            p.sendMessage(UHC.getMessage("tManageOff"));
                            tmanage = false;
                        } else if (tmanage == false) {
                            p.sendMessage(UHC.getMessage("tManageOn"));
                            tmanage = true;
                        }
                    } else {
                        p.sendMessage(UHC.getMessage("tManageUsage"));
                    }
                } else {
                    p.sendMessage(UHC.getMessage("noPermission"));
                }
                return false;
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("limit")) {
                if (p.hasPermission("uhc.admin")) {
                    if (args.length == 2) {
                        if (Utils.isInt(args[1])) {
                            int limit = Integer.parseInt(args[1]);
                            if (limit > 10) {
                                p.sendMessage(UHC.getMessage("maxLimit"));
                            } else {
                                tlimit = limit;
                                p.sendMessage(UHC.getMessage("limitChanged"));
                            }
                        } else {
                            p.sendMessage(UHC.getMessage("limitUsage"));
                        }
                    } else {
                        p.sendMessage(UHC.getMessage("limitUsage"));
                    }
                } else {
                    p.sendMessage(UHC.getMessage("noPermission"));
                }
                return false;
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("forceleave")) {
                if (p.hasPermission("uhc.admin")) {
                    if (args.length == 2) {
                        if (Team.getTeam(args[1]) == null) {
                            p.sendMessage(UHC.getMessage("playerNoTeam"));
                        } else {
                            Team.removeFromTeam(args[1]);
                            p.sendMessage(UHC.getPrefix() + Utils.colour("&c" + args[1] + "&7 has been removed from their team."));
                        }
                    } else {
                        p.sendMessage(UHC.getMessage("forceLeaveUsage"));
                    }
                } else {
                    p.sendMessage(UHC.getMessage("noPermission"));
                }
                return false;
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("disband")) {
                if (p.hasPermission("uhc.admin")) {
                    if (args.length < 2) {
                        p.sendMessage(UHC.getMessage("disbandAdminUsage"));
                    } else {
                        if (Team.getTeam(args[1]) == null) {
                            p.sendMessage(UHC.getMessage("playerNoTeam"));
                        } else {
                            Team.deleteTeam(Team.getTeam(args[1]));
                        }
                    }
                } else {
                    if (tmanage == false) {
                        p.sendMessage(UHC.getMessage("managementDisabled"));
                        return false;
                    }
                    if (args.length == 1) {
                        if (Team.getTeam(p.getName()) == null) {
                            p.sendMessage(UHC.getMessage("noTeam"));
                        } else {
                            if (Team.isTeamLeader(p.getName())) {
                                Team.deleteTeam(p.getName());
                            } else {
                                p.sendMessage(UHC.getMessage("failedDisbandNotLeader"));
                            }
                        }
                    } else {
                        p.sendMessage(UHC.getMessage("disbandUsage"));
                    }
                }
                return false;
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
                if (p.hasPermission("uhc.admin")) {
                    tmanage = false;
                    tlimit = 0;
                    Team.reset();
                    Bukkit.broadcastMessage(UHC.getMessage("teamsReset"));
                } else {
                    p.sendMessage(UHC.getMessage("noPermission"));
                }
            } else {
                if (p.hasPermission("uhc.admin")) {
                    p.sendMessage(UHC.getRawMessage("adminHelp"));
                } else {
                    p.sendMessage(UHC.getRawMessage("playerHelp"));
                }
                return false;
            }
            return false;
        } else if (cmd.getName().equalsIgnoreCase("sc")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(UHC.getMessage("playerOnly"));
            }
            Player p = (Player) sender;
            if (Team.getTeam(p.getName()) == null) {
                p.sendMessage(UHC.getMessage("noTeam"));
            } else {
                ArrayList<String> members = Team.getMembers(Team.getTeam(p.getName()));
                String message = UHC.getPrefix() + Utils.colour("&7" + p.getName() + "'s Coords - &c" + p.getLocation().getBlockX() + ", " +
                        p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + "&7.");
                for (String memberName : members) {
                    Bukkit.getPlayer(memberName).sendMessage(message);
                }
            }
            return false;
        }
        return false;
    }

    private void printTeamList(String target, String leaderName, String[] members) {
        Bukkit.getPlayer(target).sendMessage(Utils.colour("&7&m------------------------------------------------------------"));
        Bukkit.getPlayer(target).sendMessage(Utils.colour("&7" + leaderName + "'s Team:"));
        Bukkit.getPlayer(target).sendMessage(Utils.colour("&cLeader: &7" + leaderName));
        Bukkit.getPlayer(target).sendMessage(Utils.colour("&cMembers: &7" + Arrays.toString(members)));
        Bukkit.getPlayer(target).sendMessage(Utils.colour("&7&m------------------------------------------------------------"));
    }
}
