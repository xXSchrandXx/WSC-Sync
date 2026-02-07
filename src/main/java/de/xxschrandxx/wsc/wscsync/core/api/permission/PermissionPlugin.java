package de.xxschrandxx.wsc.wscsync.core.api.permission;

public enum PermissionPlugin {
    /**
     * LuckPerms
     * @see <a href="https://luckperms.net/">LuckPerms</a>
     */
    LuckPerms("LuckPerms"),
    /**
     * CloudNet
     * @see <a href="https://cloudnetservice.eu/">CloudNetService</a>
     */
    CloudNet("CloudNet-CloudPerms"),
    /**
     * Vault
     * Still needs a plugin wich supports groups.
     * @see <a href="https://github.com/MilkBowl/VaultAPI">VaultAPI</a>
     */
    Vault("Vault"),
    /**
     * Hytale
     * Default permission management (only for Hytale)
     */
    Hytale("Hytale")
    ;

    PermissionPlugin(String name) {
        this.pluginName = name;
    }
    private final String pluginName;
    public String getName() {
        return this.pluginName;
    }
}
