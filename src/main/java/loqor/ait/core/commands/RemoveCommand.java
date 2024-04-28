package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.util.desktop.structures.DesktopGenerator;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RemoveCommand {

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final SuggestionProvider<ServerCommandSource> TARDIS_SUGGESTION = (context, builder) ->
            CommandSource.suggestMatching(ServerTardisManager.getInstance()
                    .getLookup().keySet().stream().map(UUID::toString), builder);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID).then(literal("remove").requires(source -> source.hasPermissionLevel(2))
                .then(argument("id", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
                        .executes(RemoveCommand::removeCommand)))
        );
    }

    private static int removeCommand(CommandContext<ServerCommandSource> context) {
        UUID uuid = UuidArgumentType.getUuid(context, "id");
        ServerCommandSource source = context.getSource();

        Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);

        if (tardis == null) {
            source.sendFeedback(() -> Text.literal("No TARDIS with id [" + uuid + "]"), false);
            return 0;
        }

        source.sendFeedback(() -> Text.literal("Removing TARDIS with id [" + uuid + "]..."), false);

        // Remove the exterior if it exists
        AbsoluteBlockPos exterior = tardis.getHandlers().getExteriorPos();

        if (exterior != null) {
            exterior.getWorld().removeBlock(exterior, false);
            exterior.getWorld().removeBlockEntity(exterior);
        }

        // Remove the interior door
        AbsoluteBlockPos interiorDoor = tardis.getDesktop().getInteriorDoorPos();

        if (interiorDoor != null) {
            interiorDoor.getWorld().removeBlock(exterior, false);
            interiorDoor.getWorld().removeBlockEntity(interiorDoor);
        }

        // Remove the interior
        DesktopGenerator.clearArea((ServerWorld) TardisUtil.getTardisDimension(), tardis.getDesktop().getCorners());

        // Delete the file. File system operations are costly!
        EXECUTOR.execute(() -> {
            try {
                Path file = ServerTardisManager.getSavePath(context.getSource().getServer(), uuid, "json");

                if (Files.exists(file)) {
                    Files.delete(file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ServerTardisManager.getInstance().remove(uuid);
        });

        source.sendFeedback(() -> Text.literal("TARDIS [" + uuid + "] removed"), true);

        return Command.SINGLE_SUCCESS;
    }
}
