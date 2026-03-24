package de.xxschrandxx.wsc.wscsync.velocity.listener;

import com.velocitypowered.api.event.Subscribe;

import de.xxschrandxx.wsc.wscbridge.velocity.api.event.WSCBridgePluginReloadEventVelocity;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import de.xxschrandxx.wsc.wscsync.velocity.MinecraftSyncVelocity;
import de.xxschrandxx.wsc.wscsync.velocity.api.event.WSCSyncPluginReloadEventVelocity;

public class WSCBridgePluginReloadListenerVelocity {
    @Subscribe
    public void onPluginReload(WSCBridgePluginReloadEventVelocity event) {
        MinecraftSyncVelocity instance = MinecraftSyncVelocity.getInstance();
        String apiStart = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadAPIStart);
        event.getSender().sendMessage(apiStart);
        instance.loadAPI(event.getSender());
        String apiSuccess = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadAPISuccess);
        event.getSender().sendMessage(apiSuccess);
        instance.getProxy().getEventManager().fireAndForget(new WSCSyncPluginReloadEventVelocity(event.getSender()));
    }
}
