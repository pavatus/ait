package mdteam.ait.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RiftChunkCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("rift-chunk").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                        .then(literal("is-rift-chunk").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                                .then(argument("position", BlockPosArgumentType.blockPos())
                                        .executes(RiftChunkCommand::runIsRiftChunkCommand)))
                        .then(literal("get-artron-levels").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                                .then(argument("position", BlockPosArgumentType.blockPos())
                                        .executes(RiftChunkCommand::runGetArtronLevels)))
                        .then(literal("set-artron-levels").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                                .then(argument("position", BlockPosArgumentType.blockPos())
                                        .then(argument("artron-levels", IntegerArgumentType.integer())
                                                .executes(RiftChunkCommand::runSetArtronLevels)
                                        )))));
    }

    private static int runIsRiftChunkCommand(CommandContext<ServerCommandSource> context) {
        BlockPos targetBlockPos = BlockPosArgumentType.getBlockPos(context, "position");
        ServerPlayerEntity source = context.getSource().getPlayer();
        if (source == null) return 0;
        boolean isARiftChunk = TardisUtil.isRiftChunk(source.getServerWorld(), targetBlockPos);
        Text is_a_rift_chunk = Text.translatable("command.ait.riftchunk.isariftchunk");
        Text not_a_rift_chunk = Text.translatable("command.ait.riftchunk.notariftchunk");
        source.sendMessage((isARiftChunk ? is_a_rift_chunk : not_a_rift_chunk));

        return 1;
    }

    private static int runGetArtronLevels(CommandContext<ServerCommandSource> context) {
        BlockPos targetBlockPos = BlockPosArgumentType.getBlockPos(context, "position");
        ServerPlayerEntity source = context.getSource().getPlayer();
        if (source == null) return 0;
        boolean isARiftChunk = TardisUtil.isRiftChunk(source.getServerWorld(), targetBlockPos);
        Text message;
        if (!isARiftChunk) {
            message = Text.literal("This chunk is not a rift chunk, so you can't set the artron levels of it");
        } else {
            message = Text.literal("The artron levels in this chunk are: " + TardisUtil.getArtronLevelsOfChunk(source.getServerWorld(), targetBlockPos));
        }
        source.sendMessage(message);

        return 1;
    }

    private static int runSetArtronLevels(CommandContext<ServerCommandSource> context) {
        BlockPos targetBlockPos = BlockPosArgumentType.getBlockPos(context, "position");
        ServerPlayerEntity source = context.getSource().getPlayer();
        if (source == null) return 0;
        boolean isARiftChunk = TardisUtil.isRiftChunk(source.getServerWorld(), targetBlockPos);
        Text message;
        if (!isARiftChunk) {
            message = Text.literal("This chunk is not a rift chunk, so you can't get the artron levels of it");
        } else {
            Integer artron_levels = IntegerArgumentType.getInteger(context, "artron-levels");
            TardisUtil.setArtronLevelsOfChunk(source.getServerWorld(), targetBlockPos, artron_levels);
            message = Text.literal("You set the artron levels in this chunk to: " + artron_levels);
        }
        source.sendMessage(message);

        return 1;
    }
}
