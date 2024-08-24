package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.data.TardisCrashHandler;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetRepairTicksCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("repair").requires(source -> source.hasPermissionLevel(2))
                        .then(literal("set").then(argument("tardis", TardisArgumentType.tardis()).then(
                                argument("ticks", IntegerArgumentType.integer(0, TardisCrashHandler.MAX_REPAIR_TICKS))
                                        .executes(SetRepairTicksCommand::runCommand))))));
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

        if (tardis.crash().getRepairTicks() >= TardisCrashHandler.MAX_REPAIR_TICKS) {
            source.sendMessage(Text.translatableWithFallback("tardis.repair.max", "TARDIS repair ticks are at max!"));
            return 0;
        }

        int repairTicksAmount = IntegerArgumentType.getInteger(context, "ticks");
        tardis.crash().setRepairTicks(repairTicksAmount);

        source.sendMessage(Text.translatableWithFallback("tardis.repair.set", "Set repair ticks for [%s] to: [%s]",
                tardis.getUuid(), tardis.crash().getRepairTicks()));

        return Command.SINGLE_SUCCESS;
    }
}
