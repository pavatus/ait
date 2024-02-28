package mdteam.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static mdteam.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RemoveTardisCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("remove").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
								.then(argument("pos", BlockPosArgumentType.blockPos()).executes(RemoveTardisCommand::runCommand))
						))
		);
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		return removeTardis(context, BlockPosArgumentType.getBlockPos(context, "pos"));
	}

	private static int removeTardis(CommandContext<ServerCommandSource> context, @Nullable BlockPos pos) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));

		if (tardis == null || source == null) return 0;

		tardis.getTravel().setPosition(null);

		source.getWorld().removeBlock(pos, false);

		if (ServerTardisManager.getSavePath(tardis).exists()
				&& tardis.getExterior() == null &&
				ServerTardisManager.getInstance().getLookup().containsKey(tardis.getUuid())) {

			source.sendMessage(Text.literal("TARDIS [" + tardis.getUuid().toString().substring(0, 7) + "] removed"), true);

			ServerTardisManager.getSavePath(tardis).delete();

			ServerTardisManager.getInstance().getLookup().remove(tardis.getUuid());

		} else {

			source.sendMessage(Text.literal("TARDIS [" + tardis.getUuid().toString().substring(0, 7) + "] failed to be removed"), true);

		}

		return Command.SINGLE_SUCCESS;
	}
}
