package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * temporary command to set max speed until we find a proper way
 */
public class SetMaxSpeedCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("set-max-speed").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", TardisArgumentType.tardis())
								.then(argument("speed", IntegerArgumentType.integer(0))
										.executes(SetMaxSpeedCommand::runCommand))))
		);
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");
		int speed = IntegerArgumentType.getInteger(context, "speed");

		tardis.getTravel().setMaxSpeed(speed);
		return Command.SINGLE_SUCCESS;
	}
}
