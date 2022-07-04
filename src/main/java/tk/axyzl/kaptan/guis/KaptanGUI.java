package tk.axyzl.kaptan.guis;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.item.HItemBuilder;
import com.hakan.core.ui.inventory.HInventory;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import tk.axyzl.kaptan.Kaptan;
import tk.axyzl.kaptan.utils.TeleportUtils;

import java.util.List;

public class KaptanGUI extends HInventory {

    public KaptanGUI(@NotNull String id, @NotNull String title, int size, @NotNull InventoryType type) {
        super(id, title, size, type);
    }

    @Override
    protected void onOpen(@NotNull Player player) {
        for(String x : Kaptan.instance.getConfig().getConfigurationSection("KaptanGUI.items").getKeys(false)) {
            Material material = XMaterial.valueOf(Kaptan.instance.getConfig().getString("KaptanGUI.items." + x + ".material")).parseMaterial();
            List<String> lore = Kaptan.instance.getConfig().getStringList("KaptanGUI.items." + x + ".lore");
            if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                lore = PlaceholderAPI.setPlaceholders(player, lore);
            }
            HItemBuilder itemBuilder = new HItemBuilder(material)
                    .name(ChatColor.translateAlternateColorCodes('&', Kaptan.instance.getConfig().getString("KaptanGUI.items." + x + ".displayName")))
                    .lores(true, lore);
            super.setItem(Kaptan.instance.getConfig().getInt("KaptanGUI.items." + x + ".slot"), itemBuilder.build(), event -> {
                World world = Bukkit.getServer().getWorld(Kaptan.instance.getConfig().getString("KaptanGUI.items." + x + ".settings.world"));
                event.getWhoClicked().teleport(TeleportUtils.newLocation(world.getName(), (Player) event.getWhoClicked()));
            });
        }
    }
}
