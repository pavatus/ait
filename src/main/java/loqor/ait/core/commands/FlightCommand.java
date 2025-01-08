package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.drtheo.blockqueue.util.ChunkEraser;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.core.entities.FlightTardisEntity;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.DirectedGlobalPos;
import net.minecraft.block.Block;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ColumnPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FlightCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID).then(literal("flight")
                        .then(argument("tardis", TardisArgumentType.tardis())
                                .executes(FlightCommand::execute))));

    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

        try {
            FlightTardisEntity.create(player, tardis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Command.SINGLE_SUCCESS;
    }
}
