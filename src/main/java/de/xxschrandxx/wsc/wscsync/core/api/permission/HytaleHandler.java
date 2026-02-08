package de.xxschrandxx.wsc.wscsync.core.api.permission;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.permissions.provider.HytalePermissionsProvider;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;

public class HytaleHandler implements IPermissionHandler {

    protected final ISyncCoreAPI api;

    public HytaleHandler(ISyncCoreAPI api) {
        this.api = api;
    }

    public ArrayList<String> groupList() {
        ArrayList<String> groups = new ArrayList<String>(HytalePermissionsProvider.DEFAULT_GROUP_LIST);
        try {
            FileReader fileReader = new FileReader(HytalePermissionsProvider.PERMISSIONS_FILE_PATH);
            JsonReader jsonReader = new JsonReader(fileReader);
            try {
                JsonObject root = JsonParser.parseReader(jsonReader).getAsJsonObject();
                if (root.has("groups")) {
                    JsonObject jsonGroups = root.getAsJsonObject("groups");
                    groups = new ArrayList<String>(jsonGroups.keySet());
                }
                jsonReader.close();
            } catch (Throwable throwable) {
                try {
                    jsonReader.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                } 
                this.api.log("Could not load groups: " + throwable.toString());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            this.api.log("Permissions file not found: " + HytalePermissionsProvider.PERMISSIONS_FILE_PATH);
        } catch (Throwable throwable) {
            this.api.log("Error reading permissions file: " + throwable.toString());
        }
        return groups;
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
