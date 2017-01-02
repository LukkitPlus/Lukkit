package online.pizzacrust.lukkitplus.api;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

/**
 * TODO
 */
public class PlayerType extends LuaLibrary {

    private final Player player;

    public PlayerType(LuaAccessor accessor) {
        this.player = (Player) accessor.getObject();
        set("displayName", LuaValue.valueOf(player.getDisplayName()));
        set("location", new LocationType(player.getLocation()));
        set("id", player.getUniqueId().toString());
        newFunction(new GetAttribute(player));
    }

    public static class GetAttribute implements FunctionController {

        private final Player player;

        public GetAttribute(Player player) {
            this.player = player;
        }

        @Override
        public String getName() {
            return "getAttribute";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            Attribute attribute = Attribute.valueOf(name.toUpperCase());
            if (attribute == null) {
                return LuaValue.NIL;
            }
            return new AttributeType(player.getAttribute(attribute));
        }
    }

}
