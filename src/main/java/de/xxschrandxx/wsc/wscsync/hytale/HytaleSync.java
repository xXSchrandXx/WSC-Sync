package de.xxschrandxx.wsc.wscsync.hytale;

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
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.hytale.HytaleBridge;
import de.xxschrandxx.wsc.wscbridge.hytale.api.ConfigurationHytale;
import de.xxschrandxx.wsc.wscbridge.hytale.api.HytaleBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.hytale.api.command.SenderHytale;
import de.xxschrandxx.wsc.wscbridge.hytale.api.event.WSCBridgeConfigReloadEventHytale;
import de.xxschrandxx.wsc.wscbridge.hytale.api.event.WSCBridgeModuleEventHytale;
import de.xxschrandxx.wsc.wscbridge.hytale.api.event.WSCBridgePluginReloadEventHytale;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import de.xxschrandxx.wsc.wscsync.core.api.permission.PermissionPlugin;
import de.xxschrandxx.wsc.wscsync.hytale.api.HytaleSyncAPI;
import de.xxschrandxx.wsc.wscsync.hytale.api.event.WSCSyncConfigReloadEventHytale;
import de.xxschrandxx.wsc.wscsync.hytale.api.event.WSCSyncPluginReloadEventHytale;
import de.xxschrandxx.wsc.wscsync.hytale.commands.WSCSyncHytale;
import de.xxschrandxx.wsc.wscsync.hytale.listener.AddModuleListenerHytale;
import de.xxschrandxx.wsc.wscsync.hytale.listener.MinecraftSyncHytaleJoinListener;
import de.xxschrandxx.wsc.wscsync.hytale.listener.WSCBridgeConfigReloadListenerHytale;
import de.xxschrandxx.wsc.wscsync.hytale.listener.WSCBridgePluginReloadListenerHytale;

public class HytaleSync extends JavaPlugin implements IBridgePlugin<HytaleSyncAPI> {

    // start of api part
    public String getInfo() {
        return null;
    }

    private static HytaleSync instance;

    public static HytaleSync getInstance() {
        return instance;
    }

    private HytaleSyncAPI api;

    private HytaleBridgeLogger bridgeLogger;

    @Override
    public HytaleBridgeLogger getBridgeLogger() {
        return bridgeLogger;
    }

    public void loadAPI(ISender<?> sender) {
        String urlString = getConfiguration().getString(SyncVars.Configuration.url);
        URL url;
        try {
            url = URI.create(urlString).toURL();
        } catch (MalformedURLException e) {
            getLogger().atWarning().log("Could not load api, disabeling plugin!.", e);
            return;
        }
        PermissionPlugin perm = PermissionPlugin.valueOf(getConfiguration().getString(SyncVars.Configuration.plugin));
        HytaleBridge wsc = HytaleBridge.getInstance();
        this.api = new HytaleSyncAPI(
            url,
            perm,
            getBridgeLogger(),
            wsc.getAPI()
        );
        HytaleServer.get().getEventBus().dispatchFor(WSCSyncPluginReloadEventHytale.class).dispatch(new WSCSyncPluginReloadEventHytale(sender));
    }

    public HytaleSyncAPI getAPI() {
        return this.api;
    }
    // end of api part

    // start of plugin part
    public HytaleSync(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        instance = this;
        bridgeLogger = new HytaleBridgeLogger(getLogger());

        // Load configuration
        getLogger().atInfo().log("Loading Configuration.");
        SenderHytale sender = new SenderHytale(ConsoleSender.INSTANCE, getInstance());
        if (!reloadConfiguration(sender)) {
            getLogger().atWarning().log("Could not load config.json, disabeling plugin!");
            shutdown();
            return;
        }
    }

    @Override
    protected void start() {
        // Load api
        getLogger().atInfo().log("Loading API.");
        SenderHytale sender = new SenderHytale(ConsoleSender.INSTANCE, getInstance());
        loadAPI(sender);

        // Load listener
        getLogger().atInfo().log("Loading Listener.");
        getEventRegistry().register(WSCBridgeModuleEventHytale.class, event -> {
            (new AddModuleListenerHytale()).execute(event);
        });
        getEventRegistry().register(WSCBridgeConfigReloadEventHytale.class, event -> {
            (new WSCBridgeConfigReloadListenerHytale()).execute(event);
        });
        getEventRegistry().register(WSCBridgePluginReloadEventHytale.class, event -> {
            (new WSCBridgePluginReloadListenerHytale()).execute(event);
        });
        if (getConfiguration().getBoolean(SyncVars.Configuration.syncOnJoinEnabled)) {
            getEventRegistry().register(PlayerConnectEvent.class, event -> {
                (new MinecraftSyncHytaleJoinListener(getInstance())).execute(event);
            });
        }

        // load commands
        getLogger().atInfo().log("Loading Commands.");
        getCommandRegistry().registerCommand(new WSCSyncHytale());
    }

    @Override
    public void shutdown() {
    }
    // end of plugin part

    // start config part
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File configFile = new File(getDataDirectory().toFile(), "config.json");
    private ConfigurationHytale config;

    public ConfigurationHytale getConfiguration() {
        return config;
    }

    public boolean reloadConfiguration(ISender sender) {
        if (!getDataDirectory().toFile().exists()) {
            getDataDirectory().toFile().mkdir();
        }
        if (configFile.exists()) {
            try {
                String json = Files.readString(configFile.toPath());
                config = new ConfigurationHytale(gson.fromJson(json, HashMap.class));
            }
            catch (IOException e) {
                getLogger().atWarning().log("Could not load config.json.", e);
                return false;
            }
        }
        else {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {
                getLogger().atWarning().log("Could not create config.json.", e);
                return false;
            }
            config = new ConfigurationHytale();
        }

        if (SyncVars.startConfig(getConfiguration(), getBridgeLogger())) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration(sender);
        }
        HytaleServer.get().getEventBus().dispatchFor(WSCSyncConfigReloadEventHytale.class).dispatch(new WSCSyncConfigReloadEventHytale(sender));
        return true;
    }

    public boolean saveConfiguration() {
        if (!getDataDirectory().toFile().exists()) {
            getDataDirectory().toFile().mkdir();
        }
        try {
            Path tmp = getDataDirectory().resolve("config.json.tmp");
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
            getLogger().atWarning().log("Could not save config.json.", e);
            return false;
        }
        return true;
    }
    // end config part
}
