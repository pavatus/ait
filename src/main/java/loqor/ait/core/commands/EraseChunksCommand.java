package loqor.ait.core.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.drtheo.blockqueue.util.ChunkEraser;

import net.minecraft.block.Block;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.ColumnPos;

import loqor.ait.AITMod;

public class EraseChunksCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID).then(literal("erase-chunks")
                .then(argument("from", ColumnPosArgumentType.columnPos())
                        .then(argument("to", ColumnPosArgumentType.columnPos())
                                .executes(EraseChunksCommand::execute)))));

    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ColumnPos from = ColumnPosArgumentType.getColumnPos(context, "from");
        ColumnPos to = ColumnPosArgumentType.getColumnPos(context, "to");

        ChunkEraser.erase(() -> {}, context.getSource().getWorld(),
                from.toChunkPos(), to.toChunkPos(), Block.FORCE_STATE | Block.REDRAW_ON_MAIN_THREAD, true);

        return Command.SINGLE_SUCCESS;
    }
}
