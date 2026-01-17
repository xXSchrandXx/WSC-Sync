package de.xxschrandxx.wsc.wscsync.core.api.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionManagement;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;

public class CloudNetHandler implements IPermissionHandler {

    protected final ISyncCoreAPI api;
    protected final IPermissionManagement plugin;

    public CloudNetHandler(ISyncCoreAPI api) {
        this.api = api;
        this.plugin = CloudNetDriver.getInstance().getPermissionManagement();
    }

    public ArrayList<String> groupList() {
        Collection<IPermissionGroup> groupSet = this.plugin.getGroups();
        ArrayList<String> groups = new ArrayList<String>();
        for (IPermissionGroup group: groupSet) {
            groups.add(group.getName());
        }
        return groups;
    }

    public ArrayList<String> getUsersGroups(ISender<?> sender) throws IllegalArgumentException {
        return getUsersGroups(sender.getUniqueId());
    }

    public ArrayList<String> getUsersGroups(UUID uuid) throws IllegalArgumentException {
        IPermissionUser user = this.plugin.getUser(uuid);
        if (user == null) {
            throw new IllegalArgumentException("Unknown user.");
        }
        return new ArrayList<String>(user.getGroupNames());
    }

    public boolean addGroup(ISender<?> sender, String groupName) throws IllegalArgumentException {
        return addGroup(sender.getUniqueId(), groupName);
    }

    public boolean addGroup(UUID uuid, String groupName) throws IllegalArgumentException {
        IPermissionGroup group = this.plugin.getGroup(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Unknown group.");
        }
        IPermissionUser user = this.plugin.getUser(uuid);
        if (user == null) {
            throw new IllegalArgumentException("Unknown user.");
        }
        Collection<String> groupCollection = user.getGroupNames();
        if (groupCollection.contains(group.getName())) {
            return false;
        }
        user.addGroup(group.getName());
        return true;
    }

    public boolean removeGroup(ISender<?> sender, String groupName) throws IllegalArgumentException {
        return removeGroup(sender.getUniqueId(), groupName);
    }

    public boolean removeGroup(UUID uuid, String groupName) throws IllegalArgumentException {
        IPermissionGroup group = this.plugin.getGroup(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Unknown group,");
        }
        IPermissionUser user = this.plugin.getUser(uuid);
        if (user == null) {
            throw new IllegalArgumentException("Unknown user.");
        }
        Collection<String> groupCollection = user.getGroupNames();
        if (!groupCollection.contains(group.getName())) {
            return false;
        }
        user.removeGroup(group.getName());
        return true;
    }    
}
