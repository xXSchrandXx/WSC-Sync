package de.xxschrandxx.wsc.wscsync.hytale.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.hytale.api.event.AbstractWSCConfigReloadEventHytale;

public final class WSCSyncPluginReloadEventHytale extends AbstractWSCConfigReloadEventHytale {
    public WSCSyncPluginReloadEventHytale(ISender<?> sender) {
        super(sender);
    }    
}
