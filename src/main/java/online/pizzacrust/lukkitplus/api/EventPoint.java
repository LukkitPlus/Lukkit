package online.pizzacrust.lukkitplus.api;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.lukkitplus.api.event.EventAttachment;
import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class EventPoint extends LuaLibrary.StaticLibrary {

    public static final List<EventAttachment> ATTACHMENTS = new ArrayList<>();

    public EventPoint() {
        newFunction(new AttachFunction());
        newFunction(new CancelFunction());
    }

    @Override
    public String getName() {
        return "events";
    }

    public static class AttachFunction implements FunctionController {

        @Override
        public String getName() {
            return "attach";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            LuaPlugin luaPlugin = (LuaPlugin) parameters.arg(1);
            EventAttachment attachment = new EventAttachment(luaPlugin);
            ATTACHMENTS.add(attachment);
            return attachment;
        }
    }

    public static class CancelFunction implements FunctionController {

        @Override
        public String getName() {
            return "cancelEvent";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            LuaAccessor accessor = (LuaAccessor) parameters.arg(1);
            if (accessor.getObject() instanceof Event) {
                Event event = (Event) accessor.getObject();
                if (event instanceof Cancellable) {
                    Cancellable cancellable = (Cancellable) event;
                    cancellable.setCancelled(true);
                }
            }
            return LuaValue.NIL;
        }
    }

}
