package loqor.ait.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RealWorldCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("real-world").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", TardisArgumentType.tardis())
								.executes(RealWorldCommand::runSpawnRealTardisTestCommand))));
	}

	private static int runSpawnRealTardisTestCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();

		if (source == null)
			return 0;

		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		// TODO: better error handling
		if (tardis.travel2().getState() != TravelHandlerBase.State.LANDED)
			return 0;

		DirectedGlobalPos.Cached globalPos = tardis.travel2().position();
		BlockPos spawnBlockPos = globalPos.getPos();

		TardisUtil.teleportOutside(tardis, source);
		source.setInvisible(true);

		TardisRealEntity.spawnFromTardisId(globalPos.getWorld(), tardis.getUuid(), spawnBlockPos, source, tardis.getDesktop().doorPos().getPos());

		Text textResponse = Text.translatableWithFallback("command.ait.realworld.response",
				"Spawned a real world TARDIS at: ", spawnBlockPos
		);

		source.sendMessage(textResponse);
		return 1;
	}
}
