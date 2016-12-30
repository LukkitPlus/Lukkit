package online.pizzacrust.lukkitplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import online.pizzacrust.lukkitplus.api.LuaPlugin;
import online.pizzacrust.lukkitplus.environment.Environment;

public class CommandPlugin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equals("luaplugin")) {
            if (!commandSender.hasPermission("lukkitplus.pl")) {
                commandSender.sendMessage(ChatColor.RED + "Sorry, you don't have permission.");
                return true;
            }
            if (strings.length != 1) {
                commandSender.sendMessage(ChatColor.RED + "Usage: /luaplugin <name>");
                return true;
            }
            LuaPlugin plugin = Environment.findByName(strings[0]);
            if (plugin == null) {
                commandSender.sendMessage(ChatColor.RED + "Plugin specified doesn't exist.");
                return true;
            }
            commandSender.sendMessage("Name: " + ChatColor.GREEN + plugin.getName());
            commandSender.sendMessage("Version: " + ChatColor.GREEN + plugin.getVersion());
            commandSender.sendMessage("Description: " + ChatColor.GREEN + plugin.getDescription());
            return true;
        }
        return false;
    }
}
