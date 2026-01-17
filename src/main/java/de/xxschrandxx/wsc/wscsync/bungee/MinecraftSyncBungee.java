package de.xxschrandxx.wsc.wscsync.bungee;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;

import de.xxschrandxx.wsc.wscbridge.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.ConfigurationBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.command.SenderBungee;
import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.bungee.api.MinecraftSyncBungeeAPI;
import de.xxschrandxx.wsc.wscsync.bungee.commands.WSCSyncBungee;
import de.xxschrandxx.wsc.wscsync.bungee.listener.AddModuleListenerBungee;
import de.xxschrandxx.wsc.wscsync.bungee.listener.MinecraftSyncBungeeJoinListener;
import de.xxschrandxx.wsc.wscsync.bungee.listener.WSCBridgeConfigReloadListenerBungee;
import de.xxschrandxx.wsc.wscsync.bungee.listener.WSCBridgePluginReloadListenerBungee;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import de.xxschrandxx.wsc.wscsync.core.api.permission.PermissionPlugin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MinecraftSyncBungee extends Plugin implements IBridgePlugin<MinecraftSyncBungeeAPI> {

    // start of api part
    public String getInfo() {
        return null;
    }

    private static MinecraftSyncBungee instance;

    public static MinecraftSyncBungee getInstance() {
        return instance;
    }

    private MinecraftSyncBungeeAPI api;

    private MinecraftBridgeLogger bridgeLogger;

    @Override
    public MinecraftBridgeLogger getBridgeLogger() {
        return bridgeLogger;
    }

    public void loadAPI(ISender<?> sender) {
        String urlString = getConfiguration().getString(SyncVars.Configuration.url);
        URL url;
        try {
            url = URI.create(urlString).toURL();
        } catch (MalformedURLException e) {
            getLogger().log(Level.INFO, "Could not load api, disabeling plugin!.", e);
            return;
        }
        PermissionPlugin perm = PermissionPlugin.valueOf(getConfiguration().getString(SyncVars.Configuration.plugin));
        MinecraftBridgeBungee wsc = MinecraftBridgeBungee.getInstance();
        this.api = new MinecraftSyncBungeeAPI(
            url,
            perm,
            getBridgeLogger(),
            wsc.getAPI()
        );
    }

    public MinecraftSyncBungeeAPI getAPI() {
        return this.api;
    }
    // end of api part

    // start of plugin part
    @Override
    public void onEnable() {
        instance = this;
        bridgeLogger = new MinecraftBridgeLogger(getLogger());

        // Load configuration
        getLogger().log(Level.INFO, "Loading Configuration.");
        SenderBungee sender = new SenderBungee(getProxy().getConsole(), getInstance());
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
        getProxy().getPluginManager().registerListener(getInstance(), new WSCBridgeConfigReloadListenerBungee());
        getProxy().getPluginManager().registerListener(getInstance(), new WSCBridgePluginReloadListenerBungee());
        if (getConfiguration().getBoolean(SyncVars.Configuration.syncOnJoinEnabled)) {
            getProxy().getPluginManager().registerListener(getInstance(), new MinecraftSyncBungeeJoinListener(getInstance()));
        }
        getProxy().getPluginManager().registerListener(getInstance(), new AddModuleListenerBungee());

        // load commands
        getLogger().log(Level.INFO, "Loading Commands.");
        getProxy().getPluginManager().registerCommand(getInstance(), new WSCSyncBungee("wscsync"));
    }

    @Override
    public void onDisable() {
    }
    // end of plugin part

    // start config part
    private File configFile = new File(getDataFolder(), "config.yml");
    private ConfigurationBungee config;

    public ConfigurationBungee getConfiguration() {
        return getInstance().config;
    }

    public boolean reloadConfiguration(ISender<?> sender) {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (configFile.exists()) {
            try {
                config = new ConfigurationBungee(ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile));
            }
            catch (IOException e) {
                getLogger().log(Level.WARNING, "Could not load config.yml.", e);
                return false;
            }
        }
        else {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {
                getLogger().log(Level.WARNING, "Could not create config.yml.", e);
                return false;
            }
            config = new ConfigurationBungee();
        }

        if (SyncVars.startConfig(getConfiguration(), getBridgeLogger())) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration(sender);
        }
        return true;
    }

    public boolean saveConfiguration() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config.getConfiguration(), configFile);
        }
        catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not save config.yml.", e);
            return false;
        }
        return true;
    }
    // end config part
}
