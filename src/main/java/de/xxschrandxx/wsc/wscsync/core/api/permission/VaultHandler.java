package de.xxschrandxx.wsc.wscsync.core.api.permission;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import net.milkbowl.vault.permission.Permission;

public class VaultHandler implements IPermissionHandler {

    protected final ISyncCoreAPI api;
    protected final Permission plugin;
    protected final String world;

    public VaultHandler(ISyncCoreAPI api) {
        this.api = api;
        this.plugin = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();;
        this.world = Bukkit.getWorlds().get(0).getName();
    }

    public ArrayList<String> groupList() {
        String[] groupList = this.plugin.getGroups();
        ArrayList<String> groups = new ArrayList<String>();
        for (String group : groupList) {
            groups.add(group);
        }
        return groups;
    }

    public ArrayList<String> getUsersGroups(ISender<?> sender) throws IllegalArgumentException {
        return getUsersGroups(sender.getUniqueId());
    }

    public ArrayList<String> getUsersGroups(UUID uuid) throws IllegalArgumentException {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player == null) {
            throw new IllegalArgumentException("Unknown player.");
        }
        String[] groupList = this.plugin.getPlayerGroups(this.world, player);
        ArrayList<String> groups = new ArrayList<String>();
        for (String group : groupList) {
            groups.add(group);
        }
        return groups;
    }

    public boolean addGroup(ISender<?> sender, String name) throws IllegalArgumentException {
        return addGroup(sender.getUniqueId(), name);
    }

    public boolean addGroup(UUID uuid, String groupName) throws IllegalArgumentException {
        if (!groupList().contains(groupName)) {
            throw new IllegalArgumentException("Unknown group.");
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player == null) {
            throw new IllegalArgumentException("Unknown player.");
        }
        if (this.plugin.playerInGroup(this.world, player, groupName)) {
            return false;
        }
        this.plugin.playerAddGroup(this.world, player, groupName);
        return true;
    }

    public boolean removeGroup(ISender<?> sender, String name) throws IllegalArgumentException {
        return removeGroup(sender.getUniqueId(), name);
    }

    public boolean removeGroup(UUID uuid, String groupName) throws IllegalArgumentException {
        if (!groupList().contains(groupName)) {
            throw new IllegalArgumentException("Unknown group.");
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player == null) {
            throw new IllegalArgumentException("Unknown player.");
        }
        if (!this.plugin.playerInGroup(this.world, player, groupName)) {
            return false;
        }
        this.plugin.playerRemoveGroup(this.world, player, groupName);
        return true;
    }    
}
