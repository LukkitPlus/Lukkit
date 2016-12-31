package online.pizzacrust.lukkitplus.api;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import online.pizzacrust.lukkitplus.api.cmd.DynamicCommand;
import online.pizzacrust.lukkitplus.environment.Environment;
import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class CentralPoint extends LuaLibrary.StaticLibrary {
    public CentralPoint() {
        set("logger", new LuaLogger(LOGGER));
        newFunction(new VerifyFunctionController());
        newFunction(new NewPluginController());
        newFunction(new ForceLoadController());
        newFunction(new NewCommandController());
    }

    public static Logger LOGGER; // This field will be filled in runtime.

    @Override
    public String getName() {
        return "lukkit";
    }

    public static class NewCommandController implements FunctionController {

        @Override
        public String getName() {
            return "newCommand";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            String desc = parameters.arg(2).tojstring();
            String usage = parameters.arg(3).tojstring();
            LuaValue func = parameters.arg(4);
            DynamicCommand command = new DynamicCommand(name, desc, usage, func);
            try {
                Field cmdMapField = SimplePluginManager.class.getDeclaredField("commandMap");
                cmdMapField.setAccessible(true);
                CommandMap commandMap = (CommandMap) cmdMapField.get(Bukkit.getPluginManager());
                commandMap.register(command.getName(), command);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return LuaValue.NIL;
        }
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
