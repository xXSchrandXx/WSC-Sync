package de.xxschrandxx.wsc.wscsync.bungee.listener;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.listener.SyncCoreJoinListener;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MinecraftSyncBungeeJoinListener extends SyncCoreJoinListener implements Listener {
    public MinecraftSyncBungeeJoinListener(IBridgePlugin<? extends ISyncCoreAPI> instance) {
        super(instance);
    }

    @EventHandler
    public void onJoin(ServerConnectedEvent event) {
        syncPlayer(event.getPlayer().getUniqueId());
    }
}
