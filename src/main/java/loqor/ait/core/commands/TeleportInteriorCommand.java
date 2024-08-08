package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Collections;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class TeleportInteriorCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("teleport").requires(source -> source.hasPermissionLevel(2))
						.then(literal("interior")
						.then(argument("tardis", TardisArgumentType.tardis())
							.executes(TeleportInteriorCommand::runCommand)
						.then(argument("entities", EntityArgumentType.players())
								.executes(TeleportInteriorCommand::runCommandWithPlayers)))))
		);
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		Entity source = context.getSource().getEntity();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		return teleportToInterior(tardis, Collections.singleton(source));
	}

	private static int runCommandWithPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Entity source = context.getSource().getEntity();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");
		Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "entities");

		return teleportToInterior(tardis, source, entities);
	}

	private static int teleportToInterior(ServerTardis tardis, Entity source, Collection<? extends Entity> players) {
		for (Entity player : players) {
			TardisUtil.teleportInside(tardis, player);
		}

		source.sendMessage(Text.translatableWithFallback("tardis.teleport.interior.success",
				"Successful teleport - interior of [" + tardis.getUuid().toString().substring(0, 7) + "]")
		);

		return Command.SINGLE_SUCCESS;
	}

	private static int teleportToInterior(ServerTardis tardis, Collection<? extends Entity> players) {
		if (players.isEmpty())
			return 0;

		return teleportToInterior(tardis, players.stream().findFirst().get(), players);
	}
}
