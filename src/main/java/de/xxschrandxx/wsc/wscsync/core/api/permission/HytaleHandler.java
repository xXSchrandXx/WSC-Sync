package de.xxschrandxx.wsc.wscsync.core.api.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;

public class HytaleHandler implements IPermissionHandler {

    protected final ISyncCoreAPI api;

    public HytaleHandler(ISyncCoreAPI api) {
        this.api = api;
    }

    public ArrayList<String> groupList() {
        throw new UnsupportedOperationException("Cannot list groups.");
    }

    public ArrayList<String> getUsersGroups(ISender<?> sender) {
        return getUsersGroups(sender.getUniqueId());
    }

    public ArrayList<String> getUsersGroups(UUID uuid) {
        return new ArrayList<String>(Arrays.asList(PermissionsModule.get().getGroupsForUser(uuid).toArray(new String[0])));
    }

    public boolean addGroup(ISender<?> sender, String groupName) {
        return addGroup(sender.getUniqueId(), groupName);
    }

    public boolean addGroup(UUID uuid, String groupName) {
        PermissionsModule.get().addUserToGroup(uuid, groupName);
        return true;
    }

    public boolean removeGroup(ISender<?> sender, String groupName) {
        return removeGroup(sender.getUniqueId(), groupName);
    }

    public boolean removeGroup(UUID uuid, String groupName) {
        PermissionsModule.get().removeUserFromGroup(uuid, groupName);
        return true;
    }    
}
