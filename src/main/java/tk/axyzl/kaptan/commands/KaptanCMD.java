package tk.axyzl.kaptan.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.axyzl.kaptan.Kaptan;
import tk.axyzl.kaptan.utils.Gui;

public class KaptanCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Player p = (Player) commandSender;

        if(args[0].equalsIgnoreCase("gui")) {
            if (p.getWorld().getName().equalsIgnoreCase(Kaptan.instance.getConfig().getString("settings.usableWorld"))) {
                Gui gui = new Gui(Kaptan.inventoryAPI, "kaptanGui", ChatColor
                        .translateAlternateColorCodes('&', Kaptan.instance.getConfig().getString("KaptanGUI.title")), Kaptan.instance.getConfig().getInt("KaptanGUI.size"));
                gui.open(p);
            } else {
                p.sendMessage(ChatColor
                        .translateAlternateColorCodes('&', Kaptan.instance.getConfig().getString("messages.worldError")));
            }
        } else if(args[0].equalsIgnoreCase("reload")) {
            if(p.hasPermission("zkaptan.admin")) {

                p.sendMessage("Reloading...");
                Kaptan.instance.saveDefaultConfig();
                Kaptan.instance.reloadConfig();
                Kaptan.instance.saveConfig();
                p.sendMessage("Reloaded.");

            } else {
                p.sendMessage(ChatColor
                        .translateAlternateColorCodes('&', Kaptan.instance.getConfig().getString("messages.permError")));
            }
        } else {
            p.sendMessage(ChatColor
                    .translateAlternateColorCodes('&', Kaptan.instance.getConfig().getString("messages.error")));

        }

        return false;
    }
}
