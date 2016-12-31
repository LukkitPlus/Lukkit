package online.pizzacrust.lukkitplus;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.LuaValue;
import org.reflections.Reflections;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Set;

import online.pizzacrust.lukkitplus.api.CentralPoint;
import online.pizzacrust.lukkitplus.environment.Environment;

public class LukkitPlus extends JavaPlugin {

    public static File PLUGINS_FOLDER;
    public static Set<Class<? extends Event>> BUKKIT_EVENTS;

    @Override
    public void onEnable() {
        getLogger().info("Searching for default Bukkit event classes...");
        Reflections reflections = new Reflections("org.bukkit.event");
        BUKKIT_EVENTS = reflections.getSubTypesOf(Event.class);
        getLogger().info("Constructing event handlers...");
        if (!EventCallbackGenerator.isClassGenerated()) {
            try {
                EventCallbackGenerator.generateClass();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
            getLogger().info("Class has been constructed!");
        }
        getLogger().info("Registering event handlers...");
        try {
            Class<?> eventHandlerClass = Class.forName("online.pizzacrust.lukkitplus" +
                    ".EventCallback");
            Object instance = eventHandlerClass.newInstance();
            Listener listener = (Listener) instance;
            getServer().getPluginManager().registerEvents(listener, this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        getLogger().info("Initialising environment...");
        Environment.loadCoreLibs();
        Environment.loadLukkitLibs();
        Environment.installRuntime();
        Environment.loadGlobalFunctions();
        Environment.loadGlobalVariables();
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
        getLogger().info("Lifecycle has reached #onEnable, running functions...");
        Environment.enablePlugins();
    }

    @Override
    public void onDisable() {
        getLogger().info("Lifecycle has reached #onDisable, running functions...");
        Environment.disablePlugins();
    }

}
