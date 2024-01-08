package mdteam.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.FuelData;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetFuelCommand {

    public static final SuggestionProvider<ServerCommandSource> TARDIS_SUGGESTION = (context, builder) -> CommandSource.suggestMatching(ServerTardisManager.getInstance().getLookup().keySet().stream().map(UUID::toString), builder);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("set_fuel").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
                                .then(argument("amount", DoubleArgumentType.doubleArg(0, FuelData.TARDIS_MAX_FUEL))
                                        .executes(SetFuelCommand::runCommand)))));
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity source = context.getSource().getPlayer();
        Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
        if (tardis == null || source == null) return 0;
        double fuelAmount = DoubleArgumentType.getDouble(context, "amount");
        tardis.setFuelCount(fuelAmount);
        source.sendMessage(Text.literal("Set fuel for [" + tardis.getUuid() + "] to: [" + fuelAmount + "au]"), true);
        return Command.SINGLE_SUCCESS;
    }

}
