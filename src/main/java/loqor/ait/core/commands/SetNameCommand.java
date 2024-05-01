package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetNameCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("name").requires(source -> source.hasPermissionLevel(2))
						.then(literal("set").then(argument("tardis", TardisArgumentType.tardis())
								.then(argument("value", StringArgumentType.string())
										.executes(SetNameCommand::runCommand)))))
		);
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");
		String name = StringArgumentType.getString(context, "value");

		tardis.getHandlers().getStats().setName(name);
		return Command.SINGLE_SUCCESS;
	}
}
