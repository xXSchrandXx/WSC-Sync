package de.xxschrandxx.wsc.wscsync.hytale.listener;

import com.hypixel.hytale.server.core.HytaleServer;

import de.xxschrandxx.wsc.wscbridge.hytale.api.event.WSCBridgePluginReloadEventHytale;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import de.xxschrandxx.wsc.wscsync.hytale.HytaleSync;
import de.xxschrandxx.wsc.wscsync.hytale.api.event.WSCSyncPluginReloadEventHytale;

public class WSCBridgePluginReloadListenerHytale {
    public void execute(WSCBridgePluginReloadEventHytale event) {
        HytaleSync instance = HytaleSync.getInstance();
        String apiStart = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadAPIStart);
        event.getSender().sendMessage(apiStart);
        instance.loadAPI(event.getSender());
        String apiSuccess = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadAPISuccess);
        event.getSender().sendMessage(apiSuccess);
        HytaleServer.get().getEventBus().dispatchFor(WSCSyncPluginReloadEventHytale.class).dispatch(new WSCSyncPluginReloadEventHytale(event.getSender()));
    }
}
