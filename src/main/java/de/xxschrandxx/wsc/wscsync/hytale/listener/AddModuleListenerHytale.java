package de.xxschrandxx.wsc.wscsync.hytale.listener;

import de.xxschrandxx.wsc.wscbridge.hytale.api.event.WSCBridgeModuleEventHytale;
import de.xxschrandxx.wsc.wscsync.core.listener.AddModuleListenerCore;

public class AddModuleListenerHytale extends AddModuleListenerCore {
    public void execute(WSCBridgeModuleEventHytale event) {
        event.addModule(name);
    }
}
