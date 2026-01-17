package de.xxschrandxx.wsc.wscsync.hytale.listener;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.listener.SyncCoreJoinListener;

public class MinecraftSyncHytaleJoinListener extends SyncCoreJoinListener {
    public MinecraftSyncHytaleJoinListener(IBridgePlugin<? extends ISyncCoreAPI> instance) {
        super(instance);
    }

    public void execute(PlayerConnectEvent event) {
        syncPlayer(event.getPlayerRef().getUuid());
    }
}
