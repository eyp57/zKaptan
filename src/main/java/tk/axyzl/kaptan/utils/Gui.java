package tk.axyzl.kaptan.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tk.axyzl.kaptan.Kaptan;
import xyz.efekurbann.inventory.GUI;
import xyz.efekurbann.inventory.Hytem;
import xyz.efekurbann.inventory.InventoryAPI;

import java.util.List;

public class Gui extends GUI {

    public Gui(InventoryAPI api, String id, String title, int size) {
        super(api, id, title, size);
        for(String x : Kaptan.instance.getConfig().getConfigurationSection("KaptanGUI.items").getKeys(false)) {

            Material material = Material.valueOf(Kaptan.instance.getConfig().getString("KaptanGUI.items." + x + ".material"));
            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Kaptan.instance.getConfig().getString("KaptanGUI.items." + x + ".displayName")));
            List<String> lore = Kaptan.instance.getConfig().getStringList("KaptanGUI.items." + x + ".lore");
            lore.replaceAll(e -> ChatColor.translateAlternateColorCodes('&', e));
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer("KAPTAN");
            if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                meta.setLore(PlaceholderAPI.setPlaceholders(offlinePlayer, lore));
            } else {
                meta.setLore(lore);
            }

            itemStack.setItemMeta(meta);
            addItem(Kaptan.instance.getConfig().getInt("KaptanGUI.items." + x + ".slot"),
                    new Hytem(itemStack,
                            e -> {
                                World world = Bukkit.getServer().getWorld(Kaptan
                                        .instance
                                        .getConfig().getString("KaptanGUI.items." + x + ".settings.world"));
                                e.
                                        getWhoClicked()
                                            .teleport(TeleportUtils.newLocation(world.getName(), (Player) e.getWhoClicked()));

                            }));

        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }


}
