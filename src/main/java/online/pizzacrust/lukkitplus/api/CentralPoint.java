package online.pizzacrust.lukkitplus.api;


import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.logging.Logger;

import online.pizzacrust.lukkitplus.environment.Environment;
import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class CentralPoint extends LuaLibrary.StaticLibrary {
    public CentralPoint() {
        newFunction(new LoggerFunctionController());
        newFunction(new VerifyFunctionController());
        newFunction(new NewPluginController());
        newFunction(new ForceLoadController());
    }

    public static Logger LOGGER; // This field will be filled in runtime.

    @Override
    public String getName() {
        return "lukkit";
    }

    public static class ForceLoadController implements FunctionController {

        @Override
        public String getName() {
            return "forceLoad";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            System.out.println("NOTE: A plugin is attempting to forceload another/itself! Please " +
                    "wait until lifecycle reaches plugin, and not forceloading!");
            Environment.loadByName(parameters.arg(1).tojstring());
            return LuaValue.NIL;
        }
    }

    public static class LoggerFunctionController implements FunctionController {
        @Override
        public String getName() {
            return "logger";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            return new LuaLogger(LOGGER);
        }
    }

    public static class VerifyFunctionController implements FunctionController {

        @Override
        public String getName() {
            return "verify";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            System.out.println("Verify success!");
            return LuaValue.NIL;
        }
    }

    public static class NewPluginController implements FunctionController {

        @Override
        public String getName() {
            return "newPlugin";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            String version = parameters.arg(2).tojstring();
            String desc = parameters.arg(3).tojstring();
            LuaValue function = parameters.arg(4);
            LuaPlugin plugin = new LuaPlugin(name, version, desc, function);
            Environment.PLUGINS.add(plugin);
            return LuaValue.NIL;
        }
    }

}
