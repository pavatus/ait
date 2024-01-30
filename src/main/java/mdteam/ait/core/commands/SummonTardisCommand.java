package mdteam.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.sun.jdi.connect.Connector;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.Block;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static mdteam.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static mdteam.ait.tardis.TardisTravel.State.FLIGHT;
import static mdteam.ait.tardis.TardisTravel.State.LANDED;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SummonTardisCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("summon").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION).executes(SummonTardisCommand::runCommand)
                                .then(argument("pos", BlockPosArgumentType.blockPos()).executes(SummonTardisCommand::runCommandWithPos))
                        ).then(argument("forced", BoolArgumentType.bool()).executes(SummonTardisCommand::runCommandWithForced)))
        );
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        return summonTardis(context, null, false);
    }

    private static int runCommandWithPos(CommandContext<ServerCommandSource> context) {
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");

        return summonTardis(context, pos, true);
    }

    private static int runCommandWithForced(CommandContext<ServerCommandSource> context) {
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
        boolean forced = BoolArgumentType.getBool(context, "forced");

        return summonTardis(context, pos, forced);
    }

    private static int summonTardis(CommandContext<ServerCommandSource> context, @Nullable BlockPos pos, boolean forced) {
        ServerPlayerEntity source = context.getSource().getPlayer();
        Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));

        if (tardis == null || source == null /*|| target == null*/) return 0;

        if (pos == null)
            pos = source.getBlockPos();

        tardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(pos, source.getServerWorld(), source.getMovementDirection()), true);
        // travel.toggleHandbrake();

        //FIXME: move to a kind of "goto" method, i would make it but theo said hands off the tardis package
        if (tardis.getTravel().getState() == LANDED) {
            PropertiesHandler.set(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE, false);
            tardis.getTravel().dematerialise(true, forced);
        }
        if (tardis.getTravel().getState() == FLIGHT) {
            tardis.getTravel().materialise();
        }

        source.sendMessage(Text.literal("TARDIS [" + tardis.getUuid().toString().substring(0, 7) + "] is on the way!"), true); // testing purposes can be removed if ugly

        return Command.SINGLE_SUCCESS;
    }
}
