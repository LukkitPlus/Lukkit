package online.pizzacrust.lukkitplus.api;

import org.bukkit.event.Event;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class EventType extends LuaLibrary {

    public EventType(Event event) {
        set("luaAccessor", new LuaAccessor(event));
    }

}
