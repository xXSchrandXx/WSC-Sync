package de.xxschrandxx.wsc.wscsync.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.listener.SyncCoreJoinListener;

public class MinecraftSyncBukkitJoinListener extends SyncCoreJoinListener implements Listener {
    public MinecraftSyncBukkitJoinListener(IBridgePlugin<? extends ISyncCoreAPI> instance) {
        super(instance);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        syncPlayer(event.getPlayer().getUniqueId());
    }
}
