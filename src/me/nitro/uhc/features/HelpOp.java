package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class HelpOp implements CommandExecutor {

    private UHC uhc;

    public HelpOp(UHC uhc) {
        this.uhc = uhc;
    }

    private Set<Player> cooldownPlayers = new HashSet<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("help") && !cmd.getName().equalsIgnoreCase("helpop")) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(UHC.getMessage("helpOpUsage"));
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(UHC.getMessage("playerOnly"));
            return false;
        }
        Player p = (Player) sender;
        if (cooldownPlayers.contains(p)) {
            p.sendMessage(UHC.getMessage("helpOpCooldown"));
            return false;
        }
        String input = StringUtils.join(args, " ", 0, args.length);
        sender.sendMessage(UHC.getMessage("completedHelp"));
        cooldownPlayers.add(p);
        Bukkit.getScheduler().runTaskLater(uhc, () -> {
            cooldownPlayers.remove(p);
        }, 20 * 5);
        for (Player op : Bukkit.getServer().getOnlinePlayers()) {
            if (op.hasPermission("uhc.admin")) {
                op.sendMessage(UHC.getPrefix() + Utils.colour("&8HelpOp - &a" + sender.getName() + "&8: &7" + input));
            }
        }
        return false;

    }
}
