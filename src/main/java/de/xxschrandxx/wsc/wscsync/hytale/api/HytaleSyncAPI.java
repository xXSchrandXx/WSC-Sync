package de.xxschrandxx.wsc.wscsync.hytale.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.UUID;

import de.xxschrandxx.wsc.wscbridge.core.api.Response;
import de.xxschrandxx.wsc.wscbridge.hytale.api.HytaleBridgeAPI;
import de.xxschrandxx.wsc.wscbridge.hytale.api.HytaleBridgeLogger;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.api.SyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.api.exception.SyncGroupException;
import de.xxschrandxx.wsc.wscsync.core.api.permission.*;

public class HytaleSyncAPI extends HytaleBridgeAPI implements ISyncCoreAPI {

    protected final URL url;

    protected final IPermissionHandler permHandler;

    public HytaleSyncAPI(URL url, PermissionPlugin permPlugin, HytaleBridgeLogger logger, HytaleBridgeAPI api) {
        super(api, logger);
        this.url = url;
        switch(permPlugin) {
            case LuckPerms:
                this.permHandler = new LuckPermsHandler(this);
                break;
            case Hytale:
                this.permHandler = new HytaleHandler(this);
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
