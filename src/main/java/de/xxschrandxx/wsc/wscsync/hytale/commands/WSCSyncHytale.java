package de.xxschrandxx.wsc.wscsync.hytale.commands;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;

import de.xxschrandxx.wsc.wscbridge.hytale.api.command.SenderHytale;
import de.xxschrandxx.wsc.wscsync.core.commands.WSCSync;
import de.xxschrandxx.wsc.wscsync.hytale.HytaleSync;

public class WSCSyncHytale extends AbstractCommand {

    public WSCSyncHytale() {
        super("wscsync", "WSCSync command");
        setAllowsExtraArguments(true);
    }

    @Override
    @Nullable
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        HytaleSync instance = HytaleSync.getInstance();
        SenderHytale s = new SenderHytale(ctx.sender(), instance);
        String[] args = ctx.getInputString().split(" ");
        String[] argsWithoutFirst = Arrays.copyOfRange(args, 1, args.length);
        new WSCSync(instance).execute(s, argsWithoutFirst);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
}
