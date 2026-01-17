package de.xxschrandxx.wsc.wscsync.core.api.permission;

import java.util.ArrayList;
import java.util.UUID;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;

public class DefaultHandler implements IPermissionHandler {

    protected final ISyncCoreAPI api;
    public DefaultHandler(ISyncCoreAPI api) {
        this.api = api;
    }

    public ArrayList<String> groupList() {
        api.log("DefaultHandler set! Update config.yml");
        return null;
    }

    public ArrayList<String> getUsersGroups(ISender<?> sender) {
        api.log("DefaultHandler set! Update config.yml");
        return null;
    }

    public ArrayList<String> getUsersGroups(UUID uuid) {
        api.log("DefaultHandler set! Update config.yml");
        return null;
    }

    public boolean addGroup(ISender<?> sender, String name) {
        api.log("DefaultHandler set! Update config.yml");
        return false;
    }

    public boolean addGroup(UUID uuid, String groupName) {
        api.log("DefaultHandler set! Update config.yml");
        return false;
    }

    public boolean removeGroup(ISender<?> sender, String name) {
        api.log("DefaultHandler set! Update config.yml");
        return false;
    }

    public boolean removeGroup(UUID uuid, String groupName) {
        api.log("DefaultHandler set! Update config.yml");
        return false;
    }    
}
