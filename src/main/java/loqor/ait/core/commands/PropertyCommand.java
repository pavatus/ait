package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Commands regarding getting/setting the {@link PropertiesHandler}
 */
public class PropertyCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("property").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", TardisArgumentType.tardis())
								.then(argument("target", StringArgumentType.string()) // todo autofill targets based off keys in the properties handler
								.then(literal("set").then(argument("value", StringArgumentType.string()) // todo - allow for other data types.
										.executes(PropertyCommand::runSet)))
								.then(literal("get").executes(PropertyCommand::runGet))))));
	}

	private static int runGet(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");
		String target = StringArgumentType.getString(context, "target");

		Object value = PropertiesHandler.get(tardis, target);
		source.sendMessage(Text.translatableWithFallback("tardis.property.equals",
				"Property %s = %s",target, value.toString())
		);

		return Command.SINGLE_SUCCESS;
	}

	private static int runSet(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		String target = StringArgumentType.getString(context, "target");
		String value = StringArgumentType.getString(context, "value");

		PropertiesHandler.set(tardis, target, value);

		source.sendMessage(Text.translatableWithFallback("tardis.property.set",
				"Set property %s to %s", target, value.toString())
		);

		return Command.SINGLE_SUCCESS;
	}
}
