package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisCrashData;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetRepairTicksCommand {
	public static final SuggestionProvider<ServerCommandSource> TARDIS_SUGGESTION = (context, builder) -> CommandSource.suggestMatching(ServerTardisManager.getInstance().getLookup().keySet().stream().map(UUID::toString), builder);

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal(AITMod.MOD_ID)
				.then(literal("repair").then(literal("set").requires(source -> source.hasPermissionLevel(2))
						.then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
								.then(argument("timeUntilFullRepair", IntegerArgumentType.integer(0, TardisCrashData.MAX_REPAIR_TICKS))
										.executes(SetRepairTicksCommand::runCommand))))));
	}

	private static int runCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity source = context.getSource().getPlayer();
		Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
		if (tardis == null || source == null) return 0;
		if (tardis.getHandlers().getCrashData().getRepairTicks() >= TardisCrashData.MAX_REPAIR_TICKS) {
			source.sendMessage(Text.literal("TARDIS repair ticks are at max!"), true);
			return 0;
		}
		int repairTicksAmount = IntegerArgumentType.getInteger(context, "amount");
		tardis.getHandlers().getCrashData().setRepairTicks(repairTicksAmount);
		source.sendMessage(Text.literal("Set repair ticks for [" + tardis.getUuid() + "] to: [" + tardis.getHandlers().getCrashData().getRepairTicks() + "]"), true);
		return Command.SINGLE_SUCCESS;
	}

}
