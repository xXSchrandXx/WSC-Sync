package de.xxschrandxx.wsc.wscsync.velocity.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.velocity.api.event.AbstractWSCConfigReloadEventVelocity;

public final class WSCSyncPluginReloadEventVelocity extends AbstractWSCConfigReloadEventVelocity {
    public WSCSyncPluginReloadEventVelocity(ISender<?> sender) {
        super(sender);
    }    
}
