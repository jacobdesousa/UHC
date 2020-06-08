package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Border {

    private static UHC uhc;
    private static int generateTask = 0;
    private static int currentSize = 3000;
    private static int nextSize;
    static int fiveMin, fourMin, threeMin, twoMin, oneMin, halfMin, tenSec, fiveSec, fourSec, threeSec, twoSec, oneSec, shrink;

    public Border(UHC uhc) {
        this.uhc = uhc;
    }

    public static void setCurrentSize(int newSize) {
        currentSize = newSize;
    }

    public static int getCurrentSize() {
        return currentSize;
    }

    public static void setBorder(int size, World world) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + world.getName() + " set " + size + " " + size + " 0 0");
        ArrayList<Block> blocks = new ArrayList<>();
        for (int x = -size; x < size; x++) {
            for (int y = 0; y < 6; y++) {
                blocks.add(world.getBlockAt(x, world.getHighestBlockAt(x, size).getY() + y, size));
                blocks.add(world.getBlockAt(x, world.getHighestBlockAt(x, -size).getY() + y, -size));
            }
        }
        for (int z = -size; z < (size + 1); z++) {
            for (int y = 0; y < 6; y++) {
                blocks.add(world.getBlockAt(size, world.getHighestBlockAt(size, z).getY() + y, z));
            }
        }
        for (int z = -size + 1; z < size; z++) {
            for (int y = 0; y < 6; y++) {
                blocks.add(world.getBlockAt(-size, world.getHighestBlockAt(-size, z).getY() + y, z));
            }
        }
        generateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            for (int v = 0; v < 1800; v++) {
                if (blocks.iterator().hasNext()) {
                    Block activeBlock = blocks.iterator().next();
                    activeBlock.setTypeId(7);
                    blocks.remove(activeBlock);
                } else {
                    Bukkit.getScheduler().cancelTask(generateTask);
                    generateTask = 0;
                }
            }
        }, 0L, 10);
    }

    public static void startBorder(int shrinkTime, World gameWorld) {
        Border.setBorder(currentSize, gameWorld);
        nextSize = getNextSize(currentSize);
        fiveMin = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 5 minutes."));
        }, (20 * 60 * shrinkTime) - (20 * 299), 20 * 60 * 5);
        fourMin = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 4 minutes."));
        }, (20 * 60 * shrinkTime) - (20 * 60 * 4), 20 * 60 * 5);
        threeMin = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 3 minutes."));
        }, (20 * 60 * shrinkTime) - (20 * 60 * 3), 20 * 60 * 5);
        twoMin = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 2 minutes."));
        }, (20 * 60 * shrinkTime) - (20 * 60 * 2), 20 * 60 * 5);
        oneMin = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 1 minute."));
        }, (20 * 60 * shrinkTime) - (20 * 60), 20 * 60 * 5);
        halfMin = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 30 seconds."));
        }, (20 * 60 * shrinkTime) - (20 * 30), 20 * 60 * 5);
        tenSec = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 10 seconds."));
        }, (20 * 60 * shrinkTime) - (20 * 10), 20 * 60 * 5);
        fiveSec = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 5 seconds."));
        }, (20 * 60 * shrinkTime) - (20 * 5), 20 * 60 * 5);
        fourSec = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 4 seconds."));
        }, (20 * 60 * shrinkTime) - (20 * 4), 20 * 60 * 5);
        threeSec = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 3 seconds."));
        }, (20 * 60 * shrinkTime) - (20 * 3), 20 * 60 * 5);
        twoSec = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 2 seconds."));
        }, (20 * 60 * shrinkTime) - (20 * 2), 20 * 60 * 5);
        oneSec = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border will shrink to &c" + nextSize + " &7in 1 second."));
        }, (20 * 60 * shrinkTime) - (20 * 1), 20 * 60 * 5);
        shrink = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Border shrinking to &c" + nextSize + "&7."));
            setBorder(nextSize, gameWorld);
            currentSize = nextSize;
            nextSize = getNextSize(currentSize);
        }, (20 * 60 * shrinkTime), 20 * 60 * 5);
    }

    private static int getNextSize(int currentSize) {
        if (currentSize > 500) {
            return currentSize - 500;
        }
        if (currentSize <= 500 && currentSize > 100) {
            return 100;
        }
        if (currentSize <= 100 && currentSize > 50) {
            return 50;
        }
        if (currentSize <= 50 && currentSize > 25) {
            return 25;
        }
        if (currentSize <= 25) {
            cancelShrink();
            return 0;
        }
        return 0;
    }

    public static void cancelShrink() {
        Bukkit.getScheduler().cancelTask(fiveMin);
        Bukkit.getScheduler().cancelTask(fourMin);
        Bukkit.getScheduler().cancelTask(threeMin);
        Bukkit.getScheduler().cancelTask(twoMin);
        Bukkit.getScheduler().cancelTask(oneMin);
        Bukkit.getScheduler().cancelTask(halfMin);
        Bukkit.getScheduler().cancelTask(tenSec);
        Bukkit.getScheduler().cancelTask(fiveSec);
        Bukkit.getScheduler().cancelTask(fourSec);
        Bukkit.getScheduler().cancelTask(threeSec);
        Bukkit.getScheduler().cancelTask(twoSec);
        Bukkit.getScheduler().cancelTask(oneSec);
        Bukkit.getScheduler().cancelTask(shrink);
    }

    public static ItemStack getIcon() {
        return Utils.createItemStack(Material.BEDROCK, "&c&lBorder", "&7&m----------~&c" + currentSize + "~&7&m----------");
    }
}
