package mdteam.ait.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mdteam.ait.AITMod;
import mdteam.ait.core.entities.TardisRealEntity;
import mdteam.ait.core.util.ForcedChunkUtil;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

import static mdteam.ait.core.commands.SetFuelCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RealWorldCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("real-world").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
								.then(argument("tardis-id", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
										.then(argument("spawn-position", BlockPosArgumentType.blockPos())
												.executes(RealWorldCommand::runSpawnRealTardisTestCommand)))));
	}

	private static int runSpawnRealTardisTestCommand(CommandContext<ServerCommandSource> context) {
		BlockPos spawnBlockPos = BlockPosArgumentType.getBlockPos(context, "spawn-position");
		ServerPlayerEntity source = context.getSource().getPlayer();
		Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis-id"));
		if (tardis == null || tardis.getTravel().getState() != TardisTravel.State.LANDED || source == null) return 0;
		try {
			TardisUtil.teleportOutside(tardis, source);
			source.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 1, false, false, false));
			TardisRealEntity.spawnFromTardisId(tardis.getExterior().getExteriorPos().getWorld(), tardis.getUuid(), spawnBlockPos, source);
			Text textResponse = Text.translatable("command.ait.realworld.response").append(Text.literal(" " + spawnBlockPos.getX() + ", " + spawnBlockPos.getY() + ", " + spawnBlockPos.getZ()));
			source.sendMessage(textResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return 1;

	}
}
