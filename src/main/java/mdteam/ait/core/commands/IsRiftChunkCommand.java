package mdteam.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static mdteam.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static mdteam.ait.tardis.TardisTravel.State.FLIGHT;
import static mdteam.ait.tardis.TardisTravel.State.LANDED;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class IsRiftChunkCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("is-rift-chunk").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("position", BlockPosArgumentType.blockPos())
                                .executes(IsRiftChunkCommand::runCommand)))
        );
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        BlockPos target = BlockPosArgumentType.getBlockPos(context, "position");
        ServerPlayerEntity source = context.getSource().getPlayer();

        source.sendMessage(Text.literal("Is rift: " + TardisUtil.isRiftChunk(source.getServerWorld(), target)));

        return Command.SINGLE_SUCCESS;
    }
}
