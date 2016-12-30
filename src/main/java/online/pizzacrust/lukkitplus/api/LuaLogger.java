package online.pizzacrust.lukkitplus.api;

import net.md_5.bungee.api.ChatColor;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.logging.Logger;

import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class LuaLogger extends LuaLibrary {
    private final Logger logger;

    public LuaLogger() { this.logger = null; }

    public LuaLogger(Logger logger) {
        this.logger = logger;
        newFunction(new InfoLog(logger));
        newFunction(new ErrorLog(logger));
        newFunction(new WarnLog(logger));
    }

    public static class LoggerReferenceController {
        protected final Logger logger;
        public LoggerReferenceController(Logger logger) {
            this.logger = logger;
        }
    }

    public static String colorString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static class InfoLog extends LoggerReferenceController implements FunctionController {

        public InfoLog(Logger logger) {
            super(logger);
        }

        @Override
        public String getName() {
            return "info";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            //logger.info(String.valueOf(parameters == null));
            //logger.info(String.valueOf(parameters.narg()));
            logger.info(colorString(parameters.arg1().tojstring()));
            return LuaValue.NIL;
        }
    }
    public static class ErrorLog extends LoggerReferenceController implements FunctionController {

        public ErrorLog(Logger logger) {
            super(logger);
        }

        @Override
        public String getName() {
            return "error";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            logger.severe(colorString(parameters.arg(1).tojstring()));
            return LuaValue.NIL;
        }
    }
    public static class WarnLog extends LoggerReferenceController implements FunctionController {

        public WarnLog(Logger logger) {
            super(logger);
        }

        @Override
        public String getName() {
            return "warn";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            logger.warning(colorString(parameters.arg(1).tojstring()));
            return LuaValue.NIL;
        }
    }
}
