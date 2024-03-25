package loqor.ait.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.managers.RiftChunkManager;
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
				.then(literal("rift_chunk").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
						.then(literal("is_rift_chunk").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
								.then(argument("position", BlockPosArgumentType.blockPos())
										.executes(RiftChunkCommand::runIsRiftChunkCommand)))
						.then(literal("get_artron_levels").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
								.then(argument("position", BlockPosArgumentType.blockPos())
										.executes(RiftChunkCommand::runGetArtronLevels)))
						.then(literal("set_artron_levels").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
								.then(argument("position", BlockPosArgumentType.blockPos())
										.then(argument("artron_levels", IntegerArgumentType.integer())
												.executes(RiftChunkCommand::runSetArtronLevels)
										)))));
	}

	private static int runIsRiftChunkCommand(CommandContext<ServerCommandSource> context) {
		BlockPos targetBlockPos = BlockPosArgumentType.getBlockPos(context, "position");
		ServerPlayerEntity source = context.getSource().getPlayer();
		if (source == null) return 0;
		boolean isARiftChunk = RiftChunkManager.isRiftChunk(targetBlockPos);
		Text isriftchunk = Text.translatable("message.ait.sonic.riftfound");
		Text notriftchunk = Text.translatable("message.ait.sonic.riftnotfound");
		source.sendMessage((isARiftChunk ? isriftchunk : notriftchunk));

		return 1;
	}

	private static int runGetArtronLevels(CommandContext<ServerCommandSource> context) {
		BlockPos targetBlockPos = BlockPosArgumentType.getBlockPos(context, "position");
		ServerPlayerEntity source = context.getSource().getPlayer();
		if (source == null) return 0;
		boolean isARiftChunk = RiftChunkManager.isRiftChunk(targetBlockPos);
		Text message;
		message = !isARiftChunk ? Text.translatable("command.ait.riftchunk.cannotgetlevel") :
				Text.translatable("command.ait.riftchunk.getlevel")
						.append(Text.literal(": " + RiftChunkManager.getArtronLevels(source.getWorld(), targetBlockPos)));
		source.sendMessage(message);

		return 1;
	}

	private static int runSetArtronLevels(CommandContext<ServerCommandSource> context) {
		BlockPos targetBlockPos = BlockPosArgumentType.getBlockPos(context, "position");
		ServerPlayerEntity source = context.getSource().getPlayer();
		if (source == null) return 0;
		boolean isARiftChunk = RiftChunkManager.isRiftChunk(targetBlockPos);
		Text message;
		if (!isARiftChunk) {
			message = Text.translatable("command.ait.riftchunk.cannotsetlevel"); // This chunk is not a rift chunk, so you can't get the artron levels of it
		} else {
			Integer artron_levels = IntegerArgumentType.getInteger(context, "artron-levels");
			RiftChunkManager.setArtronLevels(source.getServerWorld(), targetBlockPos, artron_levels);
			message = Text.translatable("command.ait.riftchunk.setlevel").append(Text.literal(": " + artron_levels));
		}
		source.sendMessage(message);

		return 1;
	}
}
