package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import loqor.ait.AITMod;
import loqor.ait.registry.ExteriorVariantRegistry;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class UnlockExteriorsCommand {

	public static final SuggestionProvider<ServerCommandSource> TARDIS_SUGGESTION = (context, builder) -> CommandSource.suggestMatching(ServerTardisManager.getInstance().getLookup().keySet().stream().map(UUID::toString), builder);
	public static final SuggestionProvider<ServerCommandSource> DESKTOP_SUGGESTION = (context, builder) -> CommandSource.suggestMatching(ExteriorVariantRegistry.getInstance().toList().stream().map(schema -> schema.id().toString()), builder);

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("unlock_exteriors").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
								.then(argument("exterior", IdentifierArgumentType.identifier()).suggests(DESKTOP_SUGGESTION)
										.executes(UnlockExteriorsCommand::runCommand))))
		);
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		ServerTardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
		ExteriorVariantSchema schema = ExteriorVariantRegistry.getInstance().get(IdentifierArgumentType.getIdentifier(context, "exterior"));

		if (tardis == null || source == null || schema == null)
			return 0;

		tardis.unlockExterior(schema);

		source.sendMessage(Text.literal("Granted [" + tardis.getUuid() + "] " + schema.name() + " exterior"), true);
		return Command.SINGLE_SUCCESS;
	}
}
