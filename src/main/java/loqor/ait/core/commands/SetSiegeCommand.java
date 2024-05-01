package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetSiegeCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("siege").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", TardisArgumentType.tardis())
								.then(argument("siege", BoolArgumentType.bool())
										.executes(SetSiegeCommand::runCommand))))
		);
	}

	// TODO: improve feedback
	private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");
		boolean sieged = BoolArgumentType.getBool(context, "siege");

		PropertiesHandler.set(tardis, PropertiesHandler.SIEGE_MODE, sieged);
		return Command.SINGLE_SUCCESS;
	}
}
