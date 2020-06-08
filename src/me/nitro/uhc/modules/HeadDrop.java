package me.nitro.uhc.modules;

import me.nitro.uhc.Module;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class HeadDrop implements CommandExecutor, Listener, Module {

    private static boolean state;

    public HeadDrop() {
        enable();
    }

    public boolean getState() {
        return state;
    }

    public void enable() {
        state = true;
    }

    public void disable() {
        state = false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("headdrop")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("headDropDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("headDropEnable"));
            return false;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!getState()) {
            return;
        }

        Player p = e.getEntity();
        p.getLocation().getBlock().setType(Material.NETHER_FENCE);
        int skullX = p.getLocation().getBlockX();
        int skullY = p.getLocation().getBlockY() + 1;
        int skullZ = p.getLocation().getBlockZ();

        Block skullBlock = p.getWorld().getBlockAt(skullX, skullY, skullZ);
        skullBlock.setType(Material.SKULL);
        skullBlock.setData((byte) 3);
        Skull skull = (Skull) skullBlock.getState();

        skull.setSkullType(SkullType.PLAYER);
        skull.setOwner(p.getName());;

        skull.update();
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled";
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colour("&c&lHead Drop"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.colour("&7&m----------"));
        lore.add(Utils.colour("&c" + status));
        lore.add(Utils.colour("&7&m----------"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
