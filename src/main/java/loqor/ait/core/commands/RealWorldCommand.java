package loqor.ait.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static loqor.ait.core.commands.SetFuelCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RealWorldCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("real-world").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", TardisArgumentType.tardis()).suggests(TARDIS_SUGGESTION)
								.executes(RealWorldCommand::runSpawnRealTardisTestCommand))));
	}

	private static int runSpawnRealTardisTestCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		// TODO: better error handling
		if (tardis.getTravel().getState() != TardisTravel.State.LANDED)
			return 0;

		BlockPos spawnBlockPos = tardis.getExterior().getExteriorPos();

		try {
			TardisUtil.teleportOutside(tardis, source);
			source.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 1, false, false, false));
			TardisRealEntity.spawnFromTardisId(tardis.getExterior().getExteriorPos().getWorld(), tardis.getUuid(), spawnBlockPos, source, tardis.getDoor().getDoorPos());

			Text textResponse = Text.translatableWithFallback("command.ait.realworld.response",
					"Spawned a real world TARDIS at: ", spawnBlockPos
			);

			source.sendMessage(textResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 1;
	}
}
