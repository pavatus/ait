package mdteam.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mdteam.ait.AITMod;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

import static mdteam.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetHumCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("set-hum").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
								.then(argument("hum", StringArgumentType.string())
										.executes(SetHumCommand::runCommand)))));
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
		SoundEvent hum = stringToHum(StringArgumentType.getString(context, "hum"));

		if (tardis == null || source == null) return 0;

		// tardis.getHandlers().getHum().setHum(hum);

		return Command.SINGLE_SUCCESS;
	}

	// fixme this is only temporary and is NOT how it should be done!!
	private static SoundEvent stringToHum(String var) {
		return switch (var.toLowerCase()) {
			default -> AITSounds.TOYOTA_HUM;
			case "coral" -> AITSounds.CORAL_HUM;
		};
	}
}
