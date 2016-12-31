package online.pizzacrust.lukkitplus.api;

import org.bukkit.Location;

import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class LocationType extends LuaLibrary {

    private final Location location;

    public LocationType(LuaAccessor accessor) {
        this.location = (Location) accessor.getObject();
        set("x", location.getX());
        set("y", location.getY());
        set("z", location.getZ());
    }

}
