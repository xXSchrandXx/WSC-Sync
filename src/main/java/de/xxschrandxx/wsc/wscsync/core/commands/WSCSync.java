package de.xxschrandxx.wsc.wscsync.core.commands;

import java.util.UUID;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscsync.core.SyncVars;
import de.xxschrandxx.wsc.wscsync.core.api.ISyncCoreAPI;
import de.xxschrandxx.wsc.wscsync.core.api.exception.SyncGroupException;

public class WSCSync {
    private IBridgePlugin<? extends ISyncCoreAPI> instance;
    public WSCSync(IBridgePlugin<? extends ISyncCoreAPI> instance) {
        this.instance = instance;
    }
    public void execute(ISender<?> sender, String[] args) {
        if (args.length == 0) {
            player(sender);
        }
        else {
            admin(sender, args);
        }
    }

    public void player(ISender<?> sender) {
        if (!sender.isPlayer()) {
            sender.send(SyncVars.Configuration.LangCmdPlayerOnly);
            return;
        }
        if (!sender.checkPermission(SyncVars.Configuration.PermCmdWSCSync)) {
            sender.send(SyncVars.Configuration.LangCmdNoPerm);
            return;
        }
        UUID uuid = sender.getUniqueId();
        if (uuid == null) {
            sender.send(SyncVars.Configuration.LangCmdPlayerOnly);
            return;
        }
        try {
            instance.getAPI().syncGroups(uuid);
            sender.send(SyncVars.Configuration.LangCmdWSCSyncSuccess);
        }
        catch (SyncGroupException e) {
            String message = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdWSCSyncError);
            sender.sendMessage(message.replaceAll("%error%", e.getMessage()));
        }
    }

    public void admin(ISender<?> sender, String[] args) {
        if (!sender.checkPermission(SyncVars.Configuration.PermCmdWSCSyncAdmin)) {
            sender.send(SyncVars.Configuration.LangCmdNoPerm);
            return;
        }
        ISender<?> target = null;
        try {
            UUID uuid = UUID.fromString(args[0]);
            target = instance.getAPI().getSender(uuid, instance);
            }
        catch (IllegalArgumentException e) {
            target = instance.getAPI().getSender(args[0], instance);
        }
        if (target == null) {
            sender.send(SyncVars.Configuration.LangCmdAdminNoPlayer);
            return;
        }
        try {
            instance.getAPI().syncGroups(target.getUniqueId());
            sender.send(SyncVars.Configuration.LangCmdAdminSyncSuccess);
        }
        catch (SyncGroupException e) {
            String message = instance.getConfiguration().getString(SyncVars.Configuration.LangCmdAdminSyncError);
            sender.sendMessage(message.replaceAll("%error%", e.getMessage()));
        }
    }
}
