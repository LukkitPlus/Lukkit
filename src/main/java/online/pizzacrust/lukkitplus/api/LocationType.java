package online.pizzacrust.lukkitplus.api;

import org.bukkit.Location;

import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class LocationType extends LuaLibrary {

    public LocationType(LuaAccessor accessor) {
        this((Location) accessor.getObject());
    }

    public LocationType(Location location) {
        set("x", location.getX());
        set("y", location.getY());
        set("z", location.getZ());
        set("worldName", location.getWorld().getName());
    }

}
