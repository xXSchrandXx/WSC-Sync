package de.xxschrandxx.wsc.wscsync.hytale.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.hytale.api.event.AbstractWSCPluginReloadEventHytale;

public final class WSCSyncPluginReloadEventHytale extends AbstractWSCPluginReloadEventHytale {
    public WSCSyncPluginReloadEventHytale(ISender<?> sender) {
        super(sender);
    }    
}
