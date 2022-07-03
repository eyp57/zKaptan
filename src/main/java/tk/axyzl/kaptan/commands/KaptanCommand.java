package tk.axyzl.kaptan.commands;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.core.ui.inventory.HInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import tk.axyzl.kaptan.Kaptan;
import tk.axyzl.kaptan.guis.KaptanGUI;

@BaseCommand(
        name = "kaptan",
        description = "Kaptan Komutu",
        usage = "/kaptan",
        aliases = {
                "captan",
                "capitan",
                "captain"
        }
)
public class KaptanCommand implements HCommandAdapter {
    @SubCommand(
            permission = "kaptan.gui"
    )
    public void mainCommand(Player sender, String[] args) {
        if (sender.getWorld().getName().equalsIgnoreCase(Kaptan.instance.getConfig().getString("settings.usableWorld"))) {
            HInventory gui = new KaptanGUI("0", "kaptanGui", Kaptan.instance.getConfig().getInt("KaptanGUI.size"), InventoryType.CHEST);
            gui.open(sender);
        } else {
            sender.sendMessage(ChatColor
                    .translateAlternateColorCodes('&', Kaptan.instance.getConfig().getString("messages.worldError")));
        }
    }
    @SubCommand(
            permission = "kaptan.admin",
            args = {
                    "reload"
            }
    )
    public void reloadSubCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Reloading...");
        Kaptan.instance.saveDefaultConfig();
        Kaptan.instance.reloadConfig();
        Kaptan.instance.saveConfig();
        sender.sendMessage("Reloaded.");
    }
}
