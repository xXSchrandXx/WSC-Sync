package de.xxschrandxx.wsc.wscsync.hytale.listener;

import com.hypixel.hytale.server.core.HytaleServer;

import de.xxschrandxx.wsc.wscbridge.hytale.api.event.WSCBridgeConfigReloadEventHytale;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import de.xxschrandxx.wsc.wscsync.hytale.HytaleSync;
import de.xxschrandxx.wsc.wscsync.hytale.api.event.WSCSyncConfigReloadEventHytale;

public class WSCBridgeConfigReloadListenerHytale {
    public void execute(WSCBridgeConfigReloadEventHytale event) {
        HytaleSync instance = HytaleSync.getInstance();
        String configStart = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigStart);
        event.getSender().sendMessage(configStart);
        if (!instance.reloadConfiguration(event.getSender())) {
            String configError = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigError);
            event.getSender().sendMessage(configError);
            instance.getLogger().atWarning().log("Could not load config.yml!");
            return;
        }
        String configSuccess = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigSuccess);
        event.getSender().sendMessage(configSuccess);
        HytaleServer.get().getEventBus().dispatchFor(WSCSyncConfigReloadEventHytale.class).dispatch(new WSCSyncConfigReloadEventHytale(event.getSender()));
    }
}
