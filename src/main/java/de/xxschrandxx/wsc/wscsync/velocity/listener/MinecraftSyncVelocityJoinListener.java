package de.xxschrandxx.wsc.wscsync.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.listener.SyncCoreJoinListener;

public class MinecraftSyncVelocityJoinListener extends SyncCoreJoinListener {
    public MinecraftSyncVelocityJoinListener(IBridgePlugin<? extends ISyncCoreAPI> instance) {
        super(instance);
    }

    @Subscribe
    public void onJoin(ServerConnectedEvent event) {
        syncPlayer(event.getPlayer().getUniqueId());
    }
}
