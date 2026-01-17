package de.xxschrandxx.wsc.wscsync.core.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import com.google.gson.internal.LinkedTreeMap;

import de.xxschrandxx.wsc.wscbridge.core.api.Response;
import de.xxschrandxx.wsc.wscsync.core.api.exception.SyncGroupException;
import de.xxschrandxx.wsc.wscsync.core.api.permission.IPermissionHandler;

public class SyncCoreAPI {
    public static Response<String, Object> getGroups(ISyncCoreAPI api, URL url, UUID uuid) throws SocketTimeoutException, MalformedURLException, IOException {
        HashMap<String, Object> postData = new HashMap<String, Object>();
        postData.put("uuid", uuid.toString());
        Response<String, Object> request = api.requestObject(url, postData);
        return request;
    }
    public static void syncGroups(ISyncCoreAPI api, URL url, UUID uuid) throws SyncGroupException {
        Response<String, Object> response;
        try {
            response = getGroups(api, url, uuid);
        } catch (IOException e) {
            throw new SyncGroupException("Could not get groups.", e);
        }
        if (response.getResponse() == null) {
            throw new SyncGroupException("Resonse is null.");
        }
        IPermissionHandler handler = api.getHandler();
        ArrayList<String> availableGroups = handler.groupList();
        if (availableGroups.isEmpty()) {
            throw new SyncGroupException("No available groups.");
        }
        ArrayList<String> userHasGroups = handler.getUsersGroups(uuid);

        Object shouldHaveObject = response.get("shouldHave");
        if (shouldHaveObject instanceof LinkedTreeMap) {
            @SuppressWarnings("unchecked")
            LinkedTreeMap<String, Integer> shouldHave = (LinkedTreeMap<String, Integer>) shouldHaveObject;
            for (Entry<String, Integer> shouldHaveGroup : shouldHave.entrySet()) {
                if (!availableGroups.contains(shouldHaveGroup.getKey())) {
                    if (api.isDebugModeEnabled()) {
                        api.log("Unknown shouldNotHave '" + shouldHaveGroup.getKey() + "', skipping it.");
                    }
                    continue;
                }
                if (userHasGroups.contains(shouldHaveGroup.getKey())) {
                    if (api.isDebugModeEnabled()) {
                        api.log("User already in '" + shouldHaveGroup.getKey() + "', skipping it.");
                    }
                    continue;
                }
                if (api.isDebugModeEnabled()) {
                    api.log("Adding group '" + shouldHaveGroup.getKey() + "' to '" + uuid.toString() + "'.");
                }
                handler.addGroup(uuid, shouldHaveGroup.getKey());
            }
        }
        else if (api.isDebugModeEnabled()) {
            api.log("shouldHaveGroup has no entrys or wrong formatted.");
        }
    
        Object shouldNotHaveObject = response.get("shouldNotHave");
        if (shouldNotHaveObject instanceof LinkedTreeMap) {
            @SuppressWarnings("unchecked")
            LinkedTreeMap<String, Integer> shouldNotHave = (LinkedTreeMap<String, Integer>) shouldNotHaveObject;
            for (Entry<String, Integer> shouldNotHaveGroup : shouldNotHave.entrySet()) {
                if (!availableGroups.contains(shouldNotHaveGroup.getKey())) {
                    if (api.isDebugModeEnabled()) {
                        api.log("Unknown shouldNotHaveGroup '" + shouldNotHaveGroup.getKey() + "', skipping it.");
                    }
                    continue;
                }
                if (!userHasGroups.contains(shouldNotHaveGroup.getKey())) {
                    if (api.isDebugModeEnabled()) {
                        api.log("User is not in '" + shouldNotHaveGroup.getKey() + "', skipping it.");
                    }
                    continue;
                }
                if (api.isDebugModeEnabled()) {
                    api.log("Removing group '" + shouldNotHaveGroup.getKey() + "' to '" + uuid.toString() + "'.");
                }
                handler.removeGroup(uuid, shouldNotHaveGroup.getKey());
            }
        }
        else if (api.isDebugModeEnabled()) {
            api.log("shouldNotHaveGroup has no entrys or wrong formatted.");
        }
    }
}
