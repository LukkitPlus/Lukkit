package online.pizzacrust.lukkitplus;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import online.pizzacrust.lukkitplus.api.EventPoint;
import online.pizzacrust.lukkitplus.api.event.EventAttachment;

public class ListenerCallback implements Listener {

    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        for (EventAttachment attachment : EventPoint.ATTACHMENTS) {
            attachment.call(event);
        }
    }

}
