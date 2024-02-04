package mdteam.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.SiegeData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static mdteam.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetSiegeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("set-siege")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
                                .then(argument("siege", BoolArgumentType.bool())
                                .executes(SetSiegeCommand::runCommand))))
        );
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity source = context.getSource().getPlayer();
        Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
        boolean sieged = BoolArgumentType.getBool(context, "siege");

        if (tardis == null || source == null) return 0;

        PropertiesHandler.set(tardis, PropertiesHandler.SIEGE_MODE, sieged);

        return Command.SINGLE_SUCCESS;
    }
}
