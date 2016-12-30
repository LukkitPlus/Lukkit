package online.pizzacrust.lukkitplus.api;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.logging.Logger;

import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class LuaLogger extends LuaLibrary {
    private final Logger logger;

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
            logger.info(parameters.arg(1).tojstring());
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
            logger.severe(parameters.arg(1).tojstring());
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
            logger.warning(parameters.arg(1).tojstring());
            return LuaValue.NIL;
        }
    }
}
