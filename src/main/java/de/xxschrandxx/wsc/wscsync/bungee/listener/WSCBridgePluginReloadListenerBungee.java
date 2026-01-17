package de.xxschrandxx.wsc.wscsync.bungee.listener;

import de.xxschrandxx.wsc.wscbridge.bungee.api.event.WSCBridgePluginReloadEventBungee;
import de.xxschrandxx.wsc.wscsync.bungee.MinecraftSyncBungee;
import de.xxschrandxx.wsc.wscsync.bungee.api.event.WSCSyncPluginReloadEventBungee;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WSCBridgePluginReloadListenerBungee implements Listener {
    @EventHandler
    public void onPluginReload(WSCBridgePluginReloadEventBungee event) {
        MinecraftSyncBungee instance = MinecraftSyncBungee.getInstance();
        String apiStart = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadAPIStart);
        event.getSender().sendMessage(apiStart);
        instance.loadAPI(event.getSender());
        String apiSuccess = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadAPISuccess);
        event.getSender().sendMessage(apiSuccess);
        instance.getProxy().getPluginManager().callEvent(new WSCSyncPluginReloadEventBungee(event.getSender()));
    }
}
