package de.xxschrandxx.wsc.wscsync.velocity.commands;

import com.velocitypowered.api.command.RawCommand;

import de.xxschrandxx.wsc.wscbridge.velocity.api.command.SenderVelocity;
import de.xxschrandxx.wsc.wscsync.core.commands.WSCSync;
import de.xxschrandxx.wsc.wscsync.velocity.MinecraftSyncVelocity;

public class WSCSyncVelocity implements RawCommand {
    @Override
    public void execute(final Invocation invocation) {
        MinecraftSyncVelocity instance = MinecraftSyncVelocity.getInstance();
        SenderVelocity sv = new SenderVelocity(invocation.source(), instance);
        String[] args = {};
        if (!invocation.arguments().isBlank()) {
            args = invocation.arguments().split(" ");
        }
        new WSCSync(instance).execute(sv, args);
    }
}
