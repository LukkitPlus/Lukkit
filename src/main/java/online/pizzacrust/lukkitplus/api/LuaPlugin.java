package online.pizzacrust.lukkitplus.api;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.io.File;
import java.util.logging.Logger;

import online.pizzacrust.lukkitplus.environment.Environment;
import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class LuaPlugin extends LuaLibrary {

    private final String name;
    private final String description;
    private final String version;
    private final LuaValue handler;

    private LuaValue enableHandler;
    private LuaValue disableHandler;

    private boolean isEnabled = false;
    private boolean isLoaded = false;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public LuaPlugin(String name, String description, String version, LuaValue handler) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.handler = handler;
        constructHandlerFunctions();
    }

    public void loadPlugin() {
        if (!isLoaded) {
            handler.call(this);
            isLoaded = true;
        }
    }

    public void enablePlugin() {
        if (isLoaded) {
            if (!isEnabled) {
                enableHandler.call();
                isEnabled = true;
            }
        }
    }

    public void disablePlugin() {
        if (isEnabled) {
            disableHandler.call();
        }
    }

    public void setEnableHandler(LuaValue fun) {
        this.enableHandler = fun;
    }

    public void setDisableHandler(LuaValue fun) {
        this.disableHandler = fun;
    }

    private void constructHandlerFunctions() {
        newFunction(new EnableFunctionController(this));
        newFunction(new DisableFunctionController(this));
    }

    public static class EnableFunctionController implements FunctionController {
        private final LuaPlugin plugin;

        public EnableFunctionController(LuaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getName() {
            return "onEnable";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            plugin.setEnableHandler(parameters.arg(1));
            return LuaValue.NIL;
        }
    }

    public static class DisableFunctionController implements FunctionController {
        private final LuaPlugin plugin;

        public DisableFunctionController(LuaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getName() {
            return "onDisable";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            plugin.setDisableHandler(parameters.arg(1));
            return LuaValue.NIL;
        }
    }

    public static void main(String[] args) {
        Environment.loadCoreLibs();
        Environment.loadLukkitLibs();
        Environment.installRuntime();
        CentralPoint.LOGGER = Logger.getLogger("Logger");
        LuaValue chunk = Environment.GLOBAL_PATH.loadfile(new File(args[0]).getAbsolutePath());
        chunk.call();
    }



}
