package online.pizzacrust.lukkitplus.api;


import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.logging.Logger;

import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class CentralPoint extends LuaLibrary.StaticLibrary {
    public CentralPoint() {
        newFunction(new LoggerFunctionController());
    }

    public static Logger LOGGER; // This field will be filled in runtime.

    @Override
    public String getName() {
        return "lukkit";
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

}
