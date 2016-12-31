package online.pizzacrust.lukkitplus.environment;

import org.bukkit.Bukkit;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.lukkitplus.api.CentralPoint;
import online.pizzacrust.lukkitplus.api.EventPoint;
import online.pizzacrust.lukkitplus.api.LuaPlugin;
import online.pizzacrust.lukkitplus.api.TypePoint;

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
        GLOBAL_PATH.load(new EventPoint());
        GLOBAL_PATH.load(new TypePoint());
    }

    public static void loadByName(String name) {
        for (LuaPlugin luaPlugin : PLUGINS) {
            if (luaPlugin.getName().equals(name)) {
                luaPlugin.loadPlugin();
            }
        }
    }

    public static LuaPlugin findByName(String name) {
        for (LuaPlugin luaPlugin : PLUGINS) {
            if (luaPlugin.getName().equals(name)) {
                return luaPlugin;
            }
        }
        return null;
    }

    public static void addGlobalFunction(final FunctionController controller) {
        GLOBAL_PATH.set(controller.getName(), new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                return controller.onCalled(varargs);
            }
        });
    }

    public static void addGlobalVariable(String name, LuaValue value) {
        GLOBAL_PATH.set(name, value);
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

    public static void loadGlobalVariables() {
        addGlobalVariable("bukkitevents", LuaValue.valueOf("org.bukkit.event."));
    }

    public static void loadGlobalFunctions() {
        addGlobalFunction(new BroadcastFunction());
    }

    public static class BroadcastFunction implements FunctionController {

        @Override
        public String getName() {
            return "broadcast";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            Bukkit.broadcastMessage(parameters.arg(1).tojstring());
            return LuaValue.NIL;
        }
    }

}
