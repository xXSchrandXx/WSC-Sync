package de.xxschrandxx.wsc.wscsync.velocity.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.UUID;

import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.Response;
import de.xxschrandxx.wsc.wscbridge.velocity.api.MinecraftBridgeVelocityAPI;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.api.SyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.api.exception.SyncGroupException;
import de.xxschrandxx.wsc.wscsync.core.api.permission.*;

public class MinecraftSyncVelocityAPI extends MinecraftBridgeVelocityAPI implements ISyncCoreAPI {

    protected final URL url;

    protected final IPermissionHandler permHandler;

    public MinecraftSyncVelocityAPI(URL url, PermissionPlugin permPlugin, MinecraftBridgeLogger logger, MinecraftBridgeVelocityAPI api) {
        super(api, logger);
        this.url = url;
        switch(permPlugin) {
            case LuckPerms:
                this.permHandler = new LuckPermsHandler(this);
                break;
            default:
                this.permHandler = new DefaultHandler(this);
                break;
        }
    }

    public Response<String, Object> getGroups(UUID uuid) throws SocketTimeoutException, MalformedURLException, IOException {
        return SyncCoreAPI.getGroups(this, url, uuid);
    }

    public void syncGroups(UUID uuid) throws SyncGroupException {
        SyncCoreAPI.syncGroups(this, url, uuid);
    }

    public IPermissionHandler getHandler() {
        return this.permHandler;
    }
}
