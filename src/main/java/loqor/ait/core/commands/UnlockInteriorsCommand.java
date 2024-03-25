package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import loqor.ait.AITMod;
import loqor.ait.registry.DesktopRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class UnlockInteriorsCommand {

	public static final SuggestionProvider<ServerCommandSource> TARDIS_SUGGESTION = (context, builder) -> CommandSource.suggestMatching(ServerTardisManager.getInstance().getLookup().keySet().stream().map(UUID::toString), builder);

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("unlock_interiors").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
								.executes(UnlockInteriorsCommand::runCommand)))
		);
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
		if (tardis == null || source == null) return 0;
		for (int i = 0; i < DesktopRegistry.getInstance().size(); i++) {
			tardis.unlockDesktop(DesktopRegistry.getInstance().get(i));
		}
		source.sendMessage(Text.literal("Granted [" + tardis.getUuid() + "] all interiors"), true);
		return Command.SINGLE_SUCCESS;
	}

}
