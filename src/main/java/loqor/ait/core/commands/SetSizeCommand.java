package loqor.ait.core.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.ServerCommandSource;

import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.core.tardis.ServerTardis;

public class SetSizeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID).then(literal("scale").requires(source -> source.hasPermissionLevel(2))
                .then(argument("tardis", TardisArgumentType.tardis())
                        .then(argument("x_scale", IntegerArgumentType.integer())).executes(SetSizeCommand::runCommand))));
                                //.then(argument("y_scale", IntegerArgumentType.integer()))
                                        //.then(argument("z_scale", IntegerArgumentType.integer()).executes(SetSizeCommand::runCommand)))));
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");
        int x_scale = IntegerArgumentType.getInteger(context, "x_scale");
        int y_scale = IntegerArgumentType.getInteger(context, "y_scale");
        int z_scale = IntegerArgumentType.getInteger(context, "z_scale");

        tardis.stats().setXScale(x_scale);
        tardis.stats().setYScale(x_scale);
        tardis.stats().setZScale(x_scale);

        return Command.SINGLE_SUCCESS;
    }
}
