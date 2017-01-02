package online.pizzacrust.lukkitplus.api;

import org.bukkit.event.player.PlayerEvent;

public class PlayerEventType extends EventType {

    public PlayerEventType(PlayerEvent e) {
        super(e);
        set("player", new PlayerType(new LuaAccessor(e.getPlayer())));
    }

}
