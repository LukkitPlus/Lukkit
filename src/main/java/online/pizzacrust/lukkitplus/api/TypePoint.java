package online.pizzacrust.lukkitplus.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class TypePoint extends LuaLibrary.StaticLibrary {
    @Override
    public String getName() {
        return "types";
    }

    public TypePoint() {
        newFunction(new NewLocationTypeFunction());
        newFunction(new LocationWrapperFunction());
        newFunction(new PlayerWrapperFunction());
    }

    public static class NewLocationTypeFunction implements FunctionController {

        @Override
        public String getName() {
            return "newLocation";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            double x = parameters.arg(1).todouble();
            double y = parameters.arg(2).todouble();
            double z = parameters.arg(3).todouble();
            String worldName = parameters.arg(4).tojstring();
            return new LocationType(new Location(Bukkit.getWorld(worldName), x, y, z));
        }
    }

    public static class LocationWrapperFunction implements FunctionController {

        @Override
        public String getName() {
            return "wrapLocAccessor";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            LuaAccessor accessor = (LuaAccessor) parameters.arg(1);
            return new LocationType(accessor);
        }
    }

    public static class PlayerWrapperFunction implements FunctionController {

        @Override
        public String getName() {
            return "wrapPlayerAccessor";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            LuaAccessor accessor = (LuaAccessor) parameters.arg(1);
            return new PlayerType(accessor);
        }
    }

}
