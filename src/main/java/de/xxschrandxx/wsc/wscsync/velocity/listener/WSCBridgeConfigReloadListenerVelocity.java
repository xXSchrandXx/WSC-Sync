package de.xxschrandxx.wsc.wscsync.velocity.listener;

import com.velocitypowered.api.event.Subscribe;

import de.xxschrandxx.wsc.wscbridge.velocity.api.event.WSCBridgeConfigReloadEventVelocity;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import de.xxschrandxx.wsc.wscsync.velocity.MinecraftSyncVelocity;
import de.xxschrandxx.wsc.wscsync.velocity.api.event.WSCSyncConfigReloadEventVelocity;

public class WSCBridgeConfigReloadListenerVelocity {
    @Subscribe
    public void onConfigReload(WSCBridgeConfigReloadEventVelocity event) {
        MinecraftSyncVelocity instance = MinecraftSyncVelocity.getInstance();
        String configStart = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigStart);
        event.getSender().sendMessage(configStart);
        if (!instance.reloadConfiguration(event.getSender())) {
            String configError = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigError);
            event.getSender().sendMessage(configError);
            instance.getBridgeLogger().warn("Could not load config.yml!");
            return;
        }
        String configSuccess = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigSuccess);
        event.getSender().sendMessage(configSuccess);
        instance.getProxy().getEventManager().fireAndForget(new WSCSyncConfigReloadEventVelocity(event.getSender()));
    }
}
