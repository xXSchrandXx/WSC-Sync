package de.xxschrandxx.wsc.wscsync.core.api.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class LuckPermsHandler implements IPermissionHandler {

    protected final ISyncCoreAPI api;
    protected final LuckPerms plugin;

    public LuckPermsHandler(ISyncCoreAPI api) {
        this.api = api;
        this.plugin = LuckPermsProvider.get();
    }

    public ArrayList<String> groupList() {
        this.plugin.getGroupManager().loadAllGroups().join();
        Set<Group> groupSet = this.plugin.getGroupManager().getLoadedGroups();
        ArrayList<String> groups = new ArrayList<String>();
        for (Group group : groupSet) {
            groups.add(group.getName());
        }
        return groups;
    }

    public ArrayList<String> getUsersGroups(ISender<?> sender) throws IllegalArgumentException {
        return getUsersGroups(sender.getUniqueId());
    }

    public ArrayList<String> getUsersGroups(UUID uuid) throws IllegalArgumentException {
        User user = this.plugin.getUserManager().getUser(uuid);
        if (user == null) {
            CompletableFuture<User> userFuture = this.plugin.getUserManager().loadUser(uuid);
            user = userFuture.join();
        }
        if (user == null) {
            throw new IllegalArgumentException("Unknown user.");
        }
        Collection<Group> groupCollection = user.getInheritedGroups(user.getQueryOptions());
        ArrayList<String> groups = new ArrayList<String>();
        for (Group group : groupCollection) {
            groups.add(group.getFriendlyName());
        }
        return groups;
    }

    public boolean addGroup(ISender<?> sender, String groupName) throws IllegalArgumentException {
        return addGroup(sender.getUniqueId(), groupName);
    }

    public boolean addGroup(UUID uuid, String groupName) throws IllegalArgumentException {
        Group group = this.plugin.getGroupManager().getGroup(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Unknown group.");
        }
        User user = this.plugin.getUserManager().getUser(uuid);
        if (user == null) {
            CompletableFuture<User> userFuture = this.plugin.getUserManager().loadUser(uuid);
            user = userFuture.join();
        }
        if (user == null) {
            throw new IllegalArgumentException("Unknown User.");
        }
        Collection<Group> groupCollection = user.getInheritedGroups(user.getQueryOptions());
        if (groupCollection.contains(group)) {
            return false;
        }
        user.data().add(Node.builder("group." + group.getName()).build());
        this.plugin.getUserManager().saveUser(user).join();
        return true;
    }

    public boolean removeGroup(ISender<?> sender, String groupName) throws IllegalArgumentException {
        return removeGroup(sender.getUniqueId(), groupName);
    }

    public boolean removeGroup(UUID uuid, String groupName) throws IllegalArgumentException {
        Group group = this.plugin.getGroupManager().getGroup(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Unknown group.");
        }
        User user = this.plugin.getUserManager().getUser(uuid);
        if (user == null) {
            CompletableFuture<User> userFuture = this.plugin.getUserManager().loadUser(uuid);
            user = userFuture.join();
        }
        if (user == null) {
            throw new IllegalArgumentException("Unknown user.");
        }
        Collection<Group> groupCollection = user.getInheritedGroups(user.getQueryOptions());
        if (!groupCollection.contains(group)) {
            return false;
        }
        user.data().remove(Node.builder("group." + group.getName()).build());
        this.plugin.getUserManager().saveUser(user).join();
        return true;
    }    
}
