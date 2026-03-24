package de.xxschrandxx.wsc.wscsync.velocity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta.Builder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.velocity.MinecraftBridgeVelocity;
import de.xxschrandxx.wsc.wscbridge.velocity.api.ConfigurationVelocity;
import de.xxschrandxx.wsc.wscbridge.velocity.api.command.SenderVelocity;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import de.xxschrandxx.wsc.wscsync.core.api.permission.PermissionPlugin;
import de.xxschrandxx.wsc.wscsync.velocity.api.MinecraftSyncVelocityAPI;
import de.xxschrandxx.wsc.wscsync.velocity.api.event.WSCSyncConfigReloadEventVelocity;
import de.xxschrandxx.wsc.wscsync.velocity.commands.WSCSyncVelocity;
import de.xxschrandxx.wsc.wscsync.velocity.listener.AddModuleListenerVelocity;
import de.xxschrandxx.wsc.wscsync.velocity.listener.MinecraftSyncVelocityJoinListener;
import de.xxschrandxx.wsc.wscsync.velocity.listener.WSCBridgeConfigReloadListenerVelocity;
import de.xxschrandxx.wsc.wscsync.velocity.listener.WSCBridgePluginReloadListenerVelocity;

@Plugin(id = "wscsync-velocity", name = "wscsync",
        version = "1.0.8", authors = {"xXSchrandXx"},
        dependencies = {
            @Dependency(id = "wscbridge-velocity"),
            @Dependency(id = "luckperms", optional = true)
        })
public class MinecraftSyncVelocity implements IBridgePlugin<MinecraftSyncVelocityAPI> {
    // start of api part
    public String getInfo() {
        return null;
    }

    private static MinecraftSyncVelocity instance;

    public static MinecraftSyncVelocity getInstance() {
        return instance;
    }

    private MinecraftSyncVelocityAPI api;

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
            getBridgeLogger().warn("Could not load api, disabeling plugin!.", e);
            return;
        }
        PermissionPlugin perm = PermissionPlugin.valueOf(getConfiguration().getString(SyncVars.Configuration.plugin));
        MinecraftBridgeVelocity wsc = MinecraftBridgeVelocity.getInstance();
        this.api = new MinecraftSyncVelocityAPI(
            url,
            perm,
            getBridgeLogger(),
            wsc.getAPI()
        );
    }

    public MinecraftSyncVelocityAPI getAPI() {
        return this.api;
    }
    // end of api part

    // start of plugin part
    private final ProxyServer server;
    public ProxyServer getProxy() {
        return this.server;
    }

    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public MinecraftSyncVelocity(
        ProxyServer server,
        Logger logger,
        @DataDirectory Path dataDirectory
    ) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        this.configFile = new File(this.dataDirectory.toFile(), "config.json");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;
        bridgeLogger = new MinecraftBridgeLogger(this.logger);
            // Load configuration
        getBridgeLogger().info("Loading Configuration.");
        SenderVelocity sender = new SenderVelocity(getProxy().getConsoleCommandSource(), getInstance());
        if (!reloadConfiguration(sender)) {
            getBridgeLogger().warn("Could not load config.yml, disabeling plugin!");
            return;
        }

        // Load api
        getBridgeLogger().info("Loading API.");
        loadAPI(sender);

        // Load listener
        getBridgeLogger().info("Loading Listener.");
        getProxy().getEventManager().register(getInstance(), new WSCBridgeConfigReloadListenerVelocity());
        getProxy().getEventManager().register(getInstance(), new WSCBridgePluginReloadListenerVelocity());
        if (getConfiguration().getBoolean(SyncVars.Configuration.syncOnJoinEnabled)) {
            getProxy().getEventManager().register(getInstance(), new MinecraftSyncVelocityJoinListener(getInstance()));
        }
        getProxy().getEventManager().register(getInstance(), new AddModuleListenerVelocity());

        // load commands
        getBridgeLogger().info("Loading Commands.");
        Builder commandMeta = this.getProxy().getCommandManager().metaBuilder("wscsync")
            .plugin(getInstance());
        this.getProxy().getCommandManager().register(
            commandMeta.build(),
            new WSCSyncVelocity()
        );
    }
    // end of plugin part

    // start config part
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile;
    private ConfigurationVelocity config;

    public ConfigurationVelocity getConfiguration() {
        return config;
    }

    public boolean reloadConfiguration(ISender sender) {
        if (!dataDirectory.toFile().exists()) {
            dataDirectory.toFile().mkdir();
        }
        if (configFile.exists()) {
            try {
                String json = Files.readString(configFile.toPath());
                this.config = new ConfigurationVelocity(gson.fromJson(json, LinkedTreeMap.class));
            }
            catch (IOException e) {
                getBridgeLogger().warn("Could not load config.json.", e);
                return false;
            }
        }
        else {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {
                getBridgeLogger().warn("Could not create config.json.", e);
                return false;
            }
            config = new ConfigurationVelocity();
        }

        if (SyncVars.startConfig(getConfiguration(), getBridgeLogger())) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration(sender);
        }
        this.getProxy().getEventManager().fireAndForget(new WSCSyncConfigReloadEventVelocity(sender));
        return true;
    }

    public boolean saveConfiguration() {
        if (!this.dataDirectory.toFile().exists()) {
            this.dataDirectory.toFile().mkdir();
        }
        try {
            Path tmp = this.dataDirectory.resolve("config.json.tmp");
            String json = gson.toJson(this.config.getConfiguration());
            Files.writeString(tmp, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            try {
                Files.move(tmp, configFile.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                // Falls ATOMIC_MOVE nicht unterstützt wird
                Files.move(tmp, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            getBridgeLogger().warn("Could not save config.json.", e);
            return false;
        }
        return true;
    }
    // end config part
}
