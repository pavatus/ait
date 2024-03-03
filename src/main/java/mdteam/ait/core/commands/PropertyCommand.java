package mdteam.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static mdteam.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Commands regarding getting/setting the {@link PropertiesHandler}
 */
public class PropertyCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("property").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
								.then(argument("target", StringArgumentType.string()) // todo autofill targets based off keys in the properties handler
								.then(literal("set")
										.then(argument("value", StringArgumentType.string()) // todo - allow for other data types.
												.executes(PropertyCommand::runSet)))
								.then(literal("get")
										.executes(PropertyCommand::runGet))))));
	}

	private static int runGet(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
		String target = StringArgumentType.getString(context, "target");

		if (tardis == null || source == null) return 0;

		Object value = PropertiesHandler.get(tardis, target);

		source.sendMessage(Text.literal(target + " = " + value.toString()));

		return Command.SINGLE_SUCCESS;
	}

	private static int runSet(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));

		String target = StringArgumentType.getString(context, "target");
		String value = StringArgumentType.getString(context, "value");

		if (tardis == null || source == null) return 0;

		PropertiesHandler.set(tardis, target, value);

		source.sendMessage(Text.literal(target + " = " + value.toString()));

		return Command.SINGLE_SUCCESS;
	}}
