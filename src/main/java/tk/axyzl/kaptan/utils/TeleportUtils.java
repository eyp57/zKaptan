package tk.axyzl.kaptan.utils;

import java.util.HashSet;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import tk.axyzl.kaptan.Kaptan;

public class TeleportUtils {

    public static HashSet<Material> forbiddenBlock = new HashSet<>();

    static {
        forbiddenBlock.add(Material.FIRE);
        forbiddenBlock.add(Material.LAVA);
        forbiddenBlock.add(Material.CACTUS);
        forbiddenBlock.add(Material.MAGMA_BLOCK);
        forbiddenBlock.add(Material.WATER);
        forbiddenBlock.add(Material.ICE);
        forbiddenBlock.add(Material.PACKED_ICE);
        forbiddenBlock.add(Material.BLUE_ICE);
        forbiddenBlock.add(Material.ACACIA_LEAVES);
        forbiddenBlock.add(Material.BIRCH_LEAVES);
        forbiddenBlock.add(Material.DARK_OAK_LEAVES);
        forbiddenBlock.add(Material.JUNGLE_LEAVES);
        forbiddenBlock.add(Material.OAK_LEAVES);
        forbiddenBlock.add(Material.SPRUCE_LEAVES);
        forbiddenBlock.add(Material.GRAVEL);
        forbiddenBlock.add(Material.SAND);
        forbiddenBlock.add(Material.SEAGRASS);
        forbiddenBlock.add(Material.TALL_SEAGRASS);
        forbiddenBlock.add(Material.AIR);
    }

    public static Location newLocation(String world, Player player) {
        Random random = new Random();
        if (Bukkit.getWorld(world) == null) {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&czKaptan &8» &e" + world + " &fdünyası bulunmuyor!"));
            return null;
        }

        Location loc =  Kaptan.data.getLocation(player.getUniqueId() + "." + world.toLowerCase() + ".lastLocation");
        if(loc != null) {
            player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                    Kaptan.instance.getConfig().getString("messages.titles.lastLocation.title")
            ), ChatColor.translateAlternateColorCodes('&',
                    Kaptan.instance.getConfig().getString("messages.titles.lastLocation.subtitle")
                            .replaceAll("%x%", String.valueOf(loc.getX())))
                            .replaceAll("%y%", String.valueOf(loc.getY()))
                            .replaceAll("%z%", String.valueOf(loc.getZ()))
            );

        } else {

            int x = random.nextInt(Kaptan.instance.getConfig().getInt("settings.randomTP.max.x"));
            int y = 151;
            int z = random.nextInt(Kaptan.instance.getConfig().getInt("settings.randomTP.max.z"));
            loc = new Location(Bukkit.getWorld(world), x, y, z);

            y = loc.getWorld().getHighestBlockYAt(loc) + 1;
            loc.setY(y);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                    Kaptan.instance.getConfig().getString("messages.titles.searchingLocation.title")
            ), ChatColor.translateAlternateColorCodes('&',
                    Kaptan.instance.getConfig().getString("messages.titles.searchingLocation.subtitle")
            ));

            if (!isLocationSafe(loc)) {
                return newLocation(world, player);
            }

            player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                    Kaptan.instance.getConfig().getString("messages.titles.findedLocation.title")
            ), ChatColor.translateAlternateColorCodes('&',
                    Kaptan.instance.getConfig().getString("messages.titles.findedLocation.subtitle")
                            .replaceAll("%x%", String.valueOf(loc.getX()))
                            .replaceAll("%y%", String.valueOf(loc.getY()))
                            .replaceAll("%z%", String.valueOf(loc.getZ()))
            ));


            Kaptan.data.set(player.getUniqueId() + "." + world.toLowerCase() + ".firstLocation", loc);

            Kaptan.data.set(player.getUniqueId() + "." + world.toLowerCase() + ".lastLocation", loc);
            Kaptan.data.save();
        }
        return loc;
    }

    public static boolean isLocationSafe(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);
        return (!forbiddenBlock.contains(below.getType()) || !forbiddenBlock.contains(above.getType()) || !forbiddenBlock.contains(block.getType()) || (!block.getType().isSolid() && above.getType().isSolid() && below.getType().isSolid()) || (!block.getType().isTransparent() && above.getType().isTransparent() && below.getType().isTransparent()));
    }
}
