package de.xxschrandxx.wsc.wscsync.bukkit.listener;

import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.xxschrandxx.wsc.wscbridge.bukkit.api.event.WSCBridgeConfigReloadEventBukkit;
import de.xxschrandxx.wsc.wscsync.bukkit.MinecraftSyncBukkit;
import de.xxschrandxx.wsc.wscsync.bukkit.api.event.WSCSyncConfigReloadEventBukkit;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;

public class WSCBridgeConfigReloadListenerBukkit implements Listener {
    @EventHandler
    public void onConfigReload(WSCBridgeConfigReloadEventBukkit event) {
        MinecraftSyncBukkit instance = MinecraftSyncBukkit.getInstance();
        String configStart = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigStart);
        event.getSender().sendMessage(configStart);
        if (!instance.reloadConfiguration(event.getSender())) {
                String configError = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigError);
                event.getSender().sendMessage(configError);
                instance.getLogger().log(Level.WARNING, "Could not load config.yml!");
            return;
        }
        String configSuccess = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdReloadConfigSuccess);
        event.getSender().sendMessage(configSuccess);
        instance.getServer().getPluginManager().callEvent(new WSCSyncConfigReloadEventBukkit(event.getSender()));
    }
}
