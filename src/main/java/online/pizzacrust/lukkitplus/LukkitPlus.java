package online.pizzacrust.lukkitplus;

import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.FilenameFilter;

import online.pizzacrust.lukkitplus.api.CentralPoint;
import online.pizzacrust.lukkitplus.environment.Environment;

public class LukkitPlus extends JavaPlugin {

    public static File PLUGINS_FOLDER;

    @Override
    public void onEnable() {
        getLogger().info("Initialising environment...");
        Environment.loadCoreLibs();
        Environment.loadLukkitLibs();
        Environment.installRuntime();
        Environment.loadGlobalFunctions();
        getLogger().info("Selecting plugins folder...");
        PLUGINS_FOLDER = new File(this.getDataFolder().getParentFile(), "lua");
        getLogger().info("Selected plugin folder: " + PLUGINS_FOLDER.getAbsolutePath());
        if (!PLUGINS_FOLDER.exists()) {
            getLogger().warning("Plugin folder doesn't exist, creating one!");
            PLUGINS_FOLDER.mkdir();
        }
        getLogger().info("Indexing plugins folder...");
        for (File candidate : PLUGINS_FOLDER.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".lua");
            }
        })) {
            getLogger().info("Attempting to load " + candidate.getName() + "...");
            LuaValue chunk = Environment.GLOBAL_PATH.loadfile(candidate.getAbsolutePath());
            chunk.call();
        }
        getLogger().info("Sharing logger instances...");
        CentralPoint.LOGGER = getLogger();
        getLogger().info("Loading API on registered candidates...");
        Environment.loadPlugins();
        getLogger().info("Loading commands...");
        getCommand("luaplugins").setExecutor(new CommandPluginList());
        getCommand("luaplugin").setExecutor(new CommandPlugin());
        getLogger().info("Loading event listener...");
        getServer().getPluginManager().registerEvents(new ListenerCallback(), this);
        getLogger().info("Lifecycle has reached #onEnable, running functions...");
        Environment.enablePlugins();
    }

    @Override
    public void onDisable() {
        getLogger().info("Lifecycle has reached #onDisable, running functions...");
        Environment.disablePlugins();
    }

}
