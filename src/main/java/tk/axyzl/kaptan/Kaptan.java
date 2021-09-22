package tk.axyzl.kaptan;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import tk.axyzl.kaptan.commands.KaptanCMD;
import tk.axyzl.kaptan.utils.Data;
import tk.axyzl.kaptan.utils.TeleportUtils;
import xyz.efekurbann.inventory.Hytem;
import xyz.efekurbann.inventory.InventoryAPI;

import java.util.List;

public final class Kaptan extends JavaPlugin implements Listener {

    public static Kaptan instance;
    public static InventoryAPI inventoryAPI;
    public static Data data;

    @Override
    public void onEnable() {
        instance = this;
        inventoryAPI = new InventoryAPI(this);
        data = new Data(this);
        saveDefaultConfig();
        System.out.println("§czKaptan başlatıldı.");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("kaptan").setExecutor(new KaptanCMD());
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if(event.getView().getTitle().equals(Kaptan.instance.getConfig().getString("KaptanGUI.title"))) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        for(String x : getConfig().getConfigurationSection("KaptanGUI.items").getKeys(false)) {
            String configWorldName = getConfig().getString("KaptanGUI.items." + x + ".settings.world");
            String worldName = e.getPlayer().getWorld().getName();
            if(configWorldName != null) {
                if (configWorldName.equalsIgnoreCase(worldName)) {
                    data.set(e.getPlayer().getUniqueId() + "." + e.getPlayer().getWorld().getName().toLowerCase() + ".lastLocation", e.getPlayer().getLocation());
                    data.save();
                }
            }
        }
    }
    @EventHandler
    public void onDied(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Location loc = data.getLocation(p.getUniqueId() + "." + p.getWorld().getName().toLowerCase() + ".firstLocation");
        if(loc != null) {
            data.set(p.getUniqueId() + "." + p.getWorld().getName().toLowerCase() + ".lastLocation", loc);
            data.save();
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    p.teleport(loc);
                }
            };
            task.runTaskLater(this, 10 * 1);
        }
    }
    @EventHandler
    public void teleportEvent(PlayerTeleportEvent e) {
        for(String x : getConfig().getConfigurationSection("KaptanGUI.items").getKeys(false)) {
            String configWorldName = getConfig().getString("KaptanGUI.items." + x + ".settings.world");
            String worldName = e.getFrom().getWorld().getName();
            if(configWorldName != null) {
                if (configWorldName.equalsIgnoreCase(worldName)) {
                    data.set(e.getPlayer().getUniqueId() + "." + worldName.toLowerCase() + ".lastLocation", e.getFrom());
                    data.save();
                }
            }
        }
    }
}
