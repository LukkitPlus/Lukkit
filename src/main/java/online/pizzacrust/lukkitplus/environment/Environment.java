package online.pizzacrust.lukkitplus.environment;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.lukkitplus.api.CentralPoint;
import online.pizzacrust.lukkitplus.api.LuaPlugin;

public class Environment {

    public static final Globals GLOBAL_PATH = new Globals();
    public static final List<LuaPlugin> PLUGINS = new ArrayList<>();

    public static void installRuntime() {
        LoadState.install(GLOBAL_PATH);
        LuaC.install(GLOBAL_PATH);
    }

    public static void loadCoreLibs() {
        GLOBAL_PATH.load(new JseBaseLib());
        GLOBAL_PATH.load(new PackageLib());
        GLOBAL_PATH.load(new Bit32Lib());
        GLOBAL_PATH.load(new TableLib());
        GLOBAL_PATH.load(new StringLib());
        GLOBAL_PATH.load(new CoroutineLib());
        GLOBAL_PATH.load(new JseMathLib());
        GLOBAL_PATH.load(new JseIoLib());
        GLOBAL_PATH.load(new JseOsLib());
    }

    public static void loadLukkitLibs() {
        GLOBAL_PATH.load(new JavaLibInteractionTest());
        GLOBAL_PATH.load(new CentralPoint());
    }

    public static void loadByName(String name) {
        for (LuaPlugin luaPlugin : PLUGINS) {
            if (luaPlugin.getName().equals(name)) {
                luaPlugin.loadPlugin();
            }
        }
    }

    public static void loadPlugins() {
        for (LuaPlugin plugin : PLUGINS) {
            plugin.loadPlugin();
        }
    }

    public static void enablePlugins() {
        for (LuaPlugin plugin : PLUGINS) {
            plugin.enablePlugin();
        }
    }

    public static void disablePlugins() {
        for (LuaPlugin plugin : PLUGINS) {
            plugin.disablePlugin();
        }
    }

}
