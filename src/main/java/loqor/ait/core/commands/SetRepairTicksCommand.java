package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.data.TardisCrashData;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetRepairTicksCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("repair").then(literal("set").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", TardisArgumentType.tardis())
								.then(argument("ticks", IntegerArgumentType.integer(0, TardisCrashData.MAX_REPAIR_TICKS))
										.executes(SetRepairTicksCommand::runCommand))))));
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

		if (tardis.getHandlers().getCrashData().getRepairTicks() >= TardisCrashData.MAX_REPAIR_TICKS) {
			source.sendMessage(Text.translatableWithFallback("tardis.repair.max", "TARDIS repair ticks are at max!"));
			return 0;
		}

		int repairTicksAmount = IntegerArgumentType.getInteger(context, "ticks");
		tardis.getHandlers().getCrashData().setRepairTicks(repairTicksAmount);

		source.sendMessage(Text.translatableWithFallback("tardis.repair.set",
				"Set repair ticks for [%s] to: [%s]", tardis.getUuid(),
				tardis.getHandlers().getCrashData().getRepairTicks())
		);

		return Command.SINGLE_SUCCESS;
	}
}
