package mdteam.ait.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mdteam.ait.AITMod;
import mdteam.ait.core.entities.TardisRealEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RealWorldCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("real-world").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                        .then(literal("spawn_real_tardis_test").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                                .then(argument("position", BlockPosArgumentType.blockPos())
                                        .then(argument("spawn-position", BlockPosArgumentType.blockPos())
                                                .executes(RealWorldCommand::runSpawnRealTardisTestCommand))))));
    }

    private static int runSpawnRealTardisTestCommand(CommandContext<ServerCommandSource> context) {
        BlockPos targetBlockPos = BlockPosArgumentType.getBlockPos(context, "position");
        BlockPos spawnBlockPos = BlockPosArgumentType.getBlockPos(context, "spawn-position");
        ServerPlayerEntity source = context.getSource().getPlayer();
        if (source == null) return 0;
        try {
            TardisRealEntity.testSpawnFromExteriorBlockEntity(source.getServerWorld(), targetBlockPos, spawnBlockPos);
            Text textResponse = Text.translatable("command.ait.realworld.response").append(Text.literal(" " + spawnBlockPos.getX() + ", " + spawnBlockPos.getY() + ", " + spawnBlockPos.getZ()));
            source.sendMessage(textResponse);
        } catch (Exception e) {
            System.out.println(e);
        }


        return 1;

    }
}
