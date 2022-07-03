package tk.axyzl.kaptan;

import com.hakan.core.HCore;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import tk.axyzl.kaptan.commands.KaptanCommand;
import tk.axyzl.kaptan.utils.Data;

public final class Kaptan extends JavaPlugin implements Listener {

    public static Kaptan instance;
    public static Data data;

    @Override
    public void onEnable() {
        System.out.println("§ezKaptan eklentisi başlatılıyor...");
        instance = this;
        data = new Data(this);
        saveDefaultConfig();
        HCore.registerCommands(new KaptanCommand());
        getServer().getPluginManager().registerEvents(this, this);
        System.out.println("§azKaptan eklentisi başlatıldı.");
    }

    @Override
    public void onDisable() {

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
    public void onRespawn(PlayerRespawnEvent e) {
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
            task.runTaskLater(this, 10);
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
