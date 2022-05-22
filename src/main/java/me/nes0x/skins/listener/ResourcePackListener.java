package me.nes0x.skins.listener;

import me.nes0x.skins.Skins;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import static me.nes0x.skins.util.ChatUtil.translateText;

public class ResourcePackListener implements Listener {
    private final Skins main;

    public ResourcePackListener(final Skins main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
       event.getPlayer().setResourcePack(main.getConfig().getString("resource-pack-url"));
    }

    @EventHandler
    public void onResourceStatus(PlayerResourcePackStatusEvent event) {
        if (main.getConfig().getBoolean("require-enabled-resource-pack")) {
            if (event.getStatus() != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED
                    && event.getStatus() != PlayerResourcePackStatusEvent.Status.ACCEPTED
            ) {
                event.getPlayer().kickPlayer(translateText(main.getConfig().getString("messages.kick-resource-pack")));
            }
        }

    }

}
