package online.pizzacrust.lukkitplus.api;

import org.bukkit.entity.Player;
import org.luaj.vm2.LuaValue;

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
    }

}
