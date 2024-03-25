package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetLockedCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("set-locked")
						.requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", UuidArgumentType.uuid()).suggests(TeleportInteriorCommand.TARDIS_SUGGESTION)
								.then(argument("locked", BoolArgumentType.bool())
										.executes(SetLockedCommand::runCommand))))
		);
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
		boolean locked = BoolArgumentType.getBool(context, "locked");

		if (tardis == null || source == null) return 0;

		DoorData.lockTardis(locked, tardis, source, true);

		return Command.SINGLE_SUCCESS;
	}
}
