package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.Tardis;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TravelDebugCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("travel").requires(source -> source.hasPermissionLevel(2))
                        .then(literal("demat")
                                .then(argument("tardis", TardisArgumentType.tardis())
                                        .executes(TravelDebugCommand::demat)
                                )))
        );
    }

    private static int demat(CommandContext<ServerCommandSource> context) {
        Tardis tardis = TardisArgumentType.getTardis(context, "tardis");
        tardis.travel2().dematerialize();

        return Command.SINGLE_SUCCESS;
    }
}
