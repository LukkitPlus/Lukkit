package online.pizzacrust.lukkitplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import online.pizzacrust.lukkitplus.api.LuaPlugin;
import online.pizzacrust.lukkitplus.environment.Environment;

public class CommandPluginList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equals("luaplugins")) {
            if (!commandSender.hasPermission("lukkitplus.listpl")) {
                commandSender.sendMessage(ChatColor.RED + "Sorry, you don't have permission.");
                return true;
            }
            StringBuilder pluginSentence = new StringBuilder();
            int index = 0;
            for (LuaPlugin plugin : Environment.PLUGINS) {
                if (index != (Environment.PLUGINS.size() - 1)) {
                    pluginSentence.append(plugin.getName() + ", ");
                } else {
                    pluginSentence.append(plugin.getName() + "");
                }
                index++;
            }
            commandSender.sendMessage("Plugins: " + ChatColor.GREEN + pluginSentence.toString());
            return true;
        }
        return false;
    }
}
