package de.xxschrandxx.wsc.wscsync.bukkit;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import de.xxschrandxx.wsc.wscbridge.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.wscbridge.bukkit.api.ConfigurationBukkit;
import de.xxschrandxx.wsc.wscbridge.bukkit.api.command.SenderBukkit;
import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.bukkit.api.MinecraftSyncBukkitAPI;
import de.xxschrandxx.wsc.wscsync.bukkit.commands.WSCSyncBukkit;
import de.xxschrandxx.wsc.wscsync.bukkit.listener.AddModuleListenerBukkit;
import de.xxschrandxx.wsc.wscsync.bukkit.listener.MinecraftSyncBukkitJoinListener;
import de.xxschrandxx.wsc.wscsync.bukkit.listener.WSCBridgeConfigReloadListenerBukkit;
import de.xxschrandxx.wsc.wscsync.bukkit.listener.WSCBridgePluginReloadListenerBukkit;
import de.xxschrandxx.wsc.wscsync.core.MinecraftSyncVars;
import de.xxschrandxx.wsc.wscsync.core.api.permission.PermissionPlugin;

public class MinecraftSyncBukkit extends JavaPlugin implements IMinecraftBridgePlugin<MinecraftSyncBukkitAPI> {

    // start of api part
    public String getInfo() {
        return null;
    }

    private static MinecraftSyncBukkit instance;

    public static MinecraftSyncBukkit getInstance() {
        return instance;
    }

    private MinecraftSyncBukkitAPI api;

    public void loadAPI(ISender<?> sender) {
        String urlString = getConfiguration().getString(MinecraftSyncVars.Configuration.url);
        URL url;
        try {
            url = URI.create(urlString).toURL();
        } catch (MalformedURLException e) {
            getLogger().log(Level.INFO, "Could not load api, disabeling plugin!.", e);
            return;
        }
        PermissionPlugin perm = PermissionPlugin.valueOf(getConfiguration().getString(MinecraftSyncVars.Configuration.plugin));
        MinecraftBridgeBukkit wsc = MinecraftBridgeBukkit.getInstance();
        this.api = new MinecraftSyncBukkitAPI(
            url,
            perm,
            getLogger(),
            wsc.getAPI()
        );
    }
    public MinecraftSyncBukkitAPI getAPI() {
        return this.api;
    }
    // end of api part

    // start of plugin part
    @Override
    public void onEnable() {
        instance = this;

        // Load configuration
        getLogger().log(Level.INFO, "Loading Configuration.");
        SenderBukkit sender = new SenderBukkit(getServer().getConsoleSender(), getInstance());
        if (!reloadConfiguration(sender)) {
            getLogger().log(Level.WARNING, "Could not load config.yml, disabeling plugin!");
            onDisable();
            return;
        }

        // Load api
        getLogger().log(Level.INFO, "Loading API.");
        loadAPI(sender);

        // Load listener
        getLogger().log(Level.INFO, "Loading Listener.");
        getServer().getPluginManager().registerEvents(new WSCBridgeConfigReloadListenerBukkit(), getInstance());
        getServer().getPluginManager().registerEvents(new WSCBridgePluginReloadListenerBukkit(), getInstance());
        if (getConfiguration().getBoolean(MinecraftSyncVars.Configuration.syncOnJoinEnabled)) {
            getServer().getPluginManager().registerEvents(new MinecraftSyncBukkitJoinListener(getInstance()), getInstance());
        }
        getServer().getPluginManager().registerEvents(new AddModuleListenerBukkit(), getInstance());

        // Load commands
        getLogger().log(Level.INFO, "Loading Commands.");
        getCommand("wscsync").setExecutor(new WSCSyncBukkit());
    }

    @Override
    public void onDisable() {
    }
    // end of plugin part

    // start config part
    public ConfigurationBukkit getConfiguration() {
        return new ConfigurationBukkit(getInstance().getConfig());
    }

    public boolean reloadConfiguration(ISender<?> sender) {
        reloadConfig();

        if (MinecraftSyncVars.startConfig(getConfiguration(), getLogger())) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration(sender);
        }
        return true;
    }

    public boolean saveConfiguration() {
        saveConfig();
        return true;
    }
    // end config part
}
