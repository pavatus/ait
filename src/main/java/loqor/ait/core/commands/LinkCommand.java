package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.tardis.link.LinkableItem;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LinkCommand {

	// TODO: add slot argument, like in "/item replace" command
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("link").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", TardisArgumentType.tardis())
								.executes(LinkCommand::runCommand))));
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		ItemStack stack = source.getMainHandStack();

		if (!(stack.getItem() instanceof LinkableItem linker))
			return 0;

		linker.link(stack, tardis);
		return Command.SINGLE_SUCCESS;
	}
}
