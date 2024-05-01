package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GetCreatorCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("creator-name").requires(source -> source.hasPermissionLevel(2))
						.then(literal("get").then(argument("tardis", TardisArgumentType.tardis())
								.executes(GetCreatorCommand::runCommand))))
		);
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		source.sendMessage(Text.literal(tardis.getHandlers().getStats().getPlayerCreatorName()));
		return Command.SINGLE_SUCCESS;
	}
}
