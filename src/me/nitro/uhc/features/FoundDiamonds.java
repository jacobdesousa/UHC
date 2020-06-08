package me.nitro.uhc.features;

import me.nitro.uhc.Cuboid;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class FoundDiamonds implements Listener {

    HashSet<Location> foundDiamonds = new HashSet<>();
    HashMap<UUID, Integer> numberFound = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.DIAMOND_ORE) {
            if (foundDiamonds.contains(e.getBlock().getLocation())) {
                return;
            }

            World world = e.getBlock().getWorld();
            Location loc1 = new Location(world, e.getBlock().getX() + 2, e.getBlock().getY() + 2, e.getBlock().getZ() + 2);
            Location loc2 = new Location(world, e.getBlock().getX() - 2, e.getBlock().getY() - 2, e.getBlock().getZ() - 2);
            Cuboid cube = new Cuboid(loc1, loc2);
            int diamonds = 0;
            for (Block eachBlock : cube.getBlocks()) {
                if (eachBlock.getType() == Material.DIAMOND_ORE) {
                    foundDiamonds.add(eachBlock.getLocation());
                    diamonds++;
                }
            }
            if (numberFound.get(e.getPlayer().getUniqueId()) == null) {
                numberFound.put(e.getPlayer().getUniqueId(), diamonds);
            } else {
                numberFound.put(e.getPlayer().getUniqueId(), numberFound.get(e.getPlayer().getUniqueId()) + diamonds);
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("uhc.admin")) {
                    p.sendMessage(UHC.getPrefix() + Utils.colour("&b&lFD: &r&c" + e.getPlayer().getName() + "&7[&c" + numberFound.get(e.getPlayer().getUniqueId()) + "&7] has found " + diamonds + " diamonds."));
                }
            }
        }
    }
}
