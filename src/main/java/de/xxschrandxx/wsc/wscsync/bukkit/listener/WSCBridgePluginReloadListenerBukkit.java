package de.xxschrandxx.wsc.wscsync.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.xxschrandxx.wsc.wscbridge.bukkit.api.event.WSCBridgePluginReloadEventBukkit;
import de.xxschrandxx.wsc.wscsync.bukkit.MinecraftSyncBukkit;
import de.xxschrandxx.wsc.wscsync.bukkit.api.event.WSCSyncPluginReloadEventBukkit;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;

public class WSCBridgePluginReloadListenerBukkit implements Listener {
    @EventHandler
    public void onPluginReload(WSCBridgePluginReloadEventBukkit event) {
        MinecraftSyncBukkit instance = MinecraftSyncBukkit.getInstance();
        String apiStart = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadAPIStart);
        event.getSender().sendMessage(apiStart);
        instance.loadAPI(event.getSender());
        String apiSuccess = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadAPISuccess);
        event.getSender().sendMessage(apiSuccess);
        instance.getServer().getPluginManager().callEvent(new WSCSyncPluginReloadEventBukkit(event.getSender()));
    }
}
