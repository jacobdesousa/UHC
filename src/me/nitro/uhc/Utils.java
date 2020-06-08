package me.nitro.uhc;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Utils {

    public static ItemStack createItemStack(Material type, String name, String loreString) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colour(name));
        ArrayList<String> loreList = new ArrayList<>();
        if (loreString != null) {
            String[] loreLines = StringUtils.split(loreString, '~');
            for (String lore : loreLines) {
                loreList.add(Utils.colour(lore));
            }
            meta.setLore(loreList);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static String colour(String input) {
        String output = ChatColor.translateAlternateColorCodes('&', input);
        return output;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isOnline(String s) {
        try {
            Bukkit.getPlayer(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static double distanceApart(Location loc1, Location loc2) {
        return Math.abs((loc1.getX() - loc2.getX()) + (loc1.getZ() - loc2.getZ()));
    }
}
