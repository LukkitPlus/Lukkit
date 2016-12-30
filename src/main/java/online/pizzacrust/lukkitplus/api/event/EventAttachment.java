package online.pizzacrust.lukkitplus.api.event;

import org.bukkit.event.Event;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.HashMap;
import java.util.Map;

import online.pizzacrust.lukkitplus.api.LuaAccessor;
import online.pizzacrust.lukkitplus.api.LuaPlugin;
import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class EventAttachment extends LuaLibrary {

    private Map<Class<?>, LuaValue> eventListeners = new HashMap<>();
    private final LuaPlugin plugin;

    public EventAttachment(LuaPlugin plugin) {
        this.plugin = plugin;
        newFunction(new Listener(this));
    }

    public void call(Event event) {
        for (Map.Entry<Class<?>, LuaValue> entry : eventListeners.entrySet()) {
            if (entry.getKey() == event.getClass()) {
                entry.getValue().call(new LuaAccessor(event));
            }
        }
    }

    public static class Listener implements FunctionController {

        private EventAttachment attachment;

        public Listener(EventAttachment attachment) {
            this.attachment = attachment;
        }

        @Override
        public String getName() {
            return "listen";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String eventClassName = parameters.arg(1).tojstring();
            LuaValue function = parameters.arg(2);
            try {
                Class<?> theClass = Class.forName(eventClassName);
                attachment.eventListeners.put(theClass, function);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return LuaValue.NIL;
        }
    }


}
