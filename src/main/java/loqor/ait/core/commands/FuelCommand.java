package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.data.FuelData;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FuelCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("fuel").requires(source -> source.hasPermissionLevel(2))
						.then(literal("add").then(argument("tardis", TardisArgumentType.tardis())
								.then(argument("amount", DoubleArgumentType.doubleArg(0, FuelData.TARDIS_MAX_FUEL))
										.executes(FuelCommand::add))))
						.then(literal("remove").requires(source -> source.hasPermissionLevel(2))
								.then(argument("tardis", TardisArgumentType.tardis())
										.then(argument("amount", DoubleArgumentType.doubleArg(0, FuelData.TARDIS_MAX_FUEL))
												.executes(FuelCommand::remove))))
						.then(literal("set").requires(source -> source.hasPermissionLevel(2))
								.then(argument("tardis", TardisArgumentType.tardis())
										.then(argument("amount", DoubleArgumentType.doubleArg(0, FuelData.TARDIS_MAX_FUEL))
												.executes(FuelCommand::set))))
						.then(literal("get").requires(source -> source.hasPermissionLevel(2))
								.then(argument("tardis", TardisArgumentType.tardis())
										.executes(FuelCommand::get)))));
	}

	private static int add(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		double fuel = DoubleArgumentType.getDouble(context, "amount");
		tardis.addFuel(fuel);

		source.sendMessage(Text.translatableWithFallback("tardis.fuel.add",
				"Added fuel for [%s] to: [%sau]", tardis.getUuid(), tardis.getFuel())
		);

		return Command.SINGLE_SUCCESS;
	}

	private static int remove(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		double fuel = DoubleArgumentType.getDouble(context, "amount");
		tardis.removeFuel(fuel);

		source.sendMessage(Text.translatableWithFallback("tardis.fuel.remove",
				"Removed fuel from [%s] to: [%sau]", tardis.getUuid(), tardis.getFuel())
		);

		return Command.SINGLE_SUCCESS;
	}

	private static int set(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		double fuel = DoubleArgumentType.getDouble(context, "amount");

		if (fuel > FuelData.TARDIS_MAX_FUEL) {
			source.sendMessage(Text.translatableWithFallback("tardis.fuel.max", "TARDIS fuel is at max!"));
			return 0;
		}

		tardis.setFuelCount(fuel);
		source.sendMessage(Text.translatableWithFallback("tardis.fuel.set",
				"Set fuel for [%s] to: [%sau]", tardis.getUuid(), fuel)
		);

		return Command.SINGLE_SUCCESS;
	}

	private static int get(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		double fuel = tardis.fuel().getCurrentFuel();
		source.sendMessage(Text.translatableWithFallback("tardis.fuel.get",
				"Fuel of [%s] is: [%sau]", tardis.getUuid(), fuel)
		);

		return (int) fuel;
	}
}
