package de.xxschrandxx.wsc.wscsync.core;

import de.xxschrandxx.wsc.wscbridge.core.api.IBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.configuration.AbstractConfiguration;
import de.xxschrandxx.wsc.wscbridge.core.api.configuration.IConfiguration;
import de.xxschrandxx.wsc.wscsync.core.api.permission.PermissionPlugin;

public class SyncVars extends AbstractConfiguration {
    public static boolean startConfig(IConfiguration<?> configuration, IBridgeLogger logger) {
        return startConfig(configuration, Configuration.class, defaults.class, logger);
    }

    public static class Configuration {
        // universal
        // url
        public static final String url = "url";
        // plugin
        public static final String plugin = "plugin";
        // syncOnJoin.enabled
        public static final String syncOnJoinEnabled = "syncOnJoin.enabled";
        // syncOnJoin.interval
        public static final String syncOnJoinInterval = "syncOnJoin.interval";

        // permission
        // permission.command.wscsync
        public static final String PermCmdWSCSync = "permission.command.wscsync";
        // permission.command.admin
        public static final String PermCmdWSCSyncAdmin = "permission.command.admin";

        // language
        // language.command.noperm
        public static final String LangCmdNoPerm = "language.command.noperm";
        // language.command.reload.config.start
        public static final String LangCmdReloadConfigStart = "language.command.reload.config.start";
        // language.command.reload.config.error
        public static final String LangCmdReloadConfigError = "language.command.reload.config.error";
        // language.command.reload.config.success
        public static final String LangCmdReloadConfigSuccess = "language.command.reload.config.success";
        // language.command.reload.api.start
        public static final String LangCmdReloadAPIStart = "language.command.reload.api.start";
        // language.command.reload.api.success
        public static final String LangCmdReloadAPISuccess = "language.command.reload.api.success";
        // language.command.playeronly
        public static final String LangCmdPlayerOnly = "language.command.playeronly";
        // language.command.sync.success
        public static final String LangCmdWSCSyncSuccess = "language.command.sync.success";
        // language.command.sync.error
        public static final String LangCmdWSCSyncError = "language.command.sync.error";

        // language.command.admin.noplayer
        public static final String LangCmdAdminNoPlayer = "language.command.admin.noplayer";
        // language.command.admin.success
        public static final String LangCmdAdminSyncSuccess = "language.command.admin.success";
        // language.command.admin.error
        public static final String LangCmdAdminSyncError = "language.command.admin.error";
    }
    // Default values
    public static final class defaults {
        // universal
        // url
        public static final String url = "https://example.domain/index.php?minecraft-sync-get-groups/";
        // plugin
        public static final String plugin = PermissionPlugin.LuckPerms.name();
        // syncOnJoin.enabled
        public static final Boolean syncOnJoinEnabled = true;
        // syncOnJoin.interval
        public static final Integer syncOnJoinInterval = 5 * 60 * 1000;

        // permission
        // permission.command.wscsync
        public static final String PermCmdWSCSync = "wscsync.command.wscsync";
        // permission.command.admin
        public static final String PermCmdWSCSyncAdmin = "wscsync.command.admin";

        // language
        // language.command.noperm
        public static final String LangCmdNoPerm = "&8[&6WSC-Sync&8]&c You don't have permission to do this.";
        // language.command.reload.config.start
        public static final String LangCmdReloadConfigStart = "&8[&6WSC-Sync&8]&7 Reloading configuration.";
        // language.command.reload.config.error
        public static final String LangCmdReloadConfigError = "&8[&6WSC-Sync&8]&e Reloading configuration failed.";
        // language.command.reload.config.success
        public static final String LangCmdReloadConfigSuccess = "&8[&6WSC-Sync&8]&7 Configuration reloaded successfully.";
        // language.command.reload.api.start
        public static final String LangCmdReloadAPIStart = "&8[&6WSC-Sync&8]&7 Reloading API.";
        // language.command.reload.api.success
        public static final String LangCmdReloadAPISuccess = "&8[&6WSC-Sync&8]&7 API reloaded successfully.";
        // language.command.playeronly
        public static final String LangCmdPlayerOnly = "&8[&6WSC-Sync&8]&c You need to be a player.";
        // language.command.sync.success
        public static final String LangCmdWSCSyncSuccess = "&8[&6WSC-Sync&8]&7 Successfully synced groups.";
        // language.command.sync.error
        public static final String LangCmdWSCSyncError = "&8[&6WSC-Sync&8]&c Could not sync groups: %error%";

        // language.command.admin.noplayer
        public static final String LangCmdAdminNoPlayer = "&8[&6WSC-Linker&8]&c Cannot find player.";
        // language.command.admin.success
        public static final String LangCmdAdminSyncSuccess = "&8[&6WSC-Sync&8]&7 Successfully synced groups.";
        // language.command.admin.error
        public static final String LangCmdAdminSyncError = "&8[&6WSC-Sync&8]&c Could not sync groups: %error%";
    }
}
