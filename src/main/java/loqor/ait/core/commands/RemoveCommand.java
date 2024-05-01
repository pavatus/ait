package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.util.desktop.structures.DesktopGenerator;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RemoveCommand {

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("remove").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("tardis", TardisArgumentType.tardis())
                                .executes(RemoveCommand::removeCommand)))
        );
    }

    private static int removeCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

        source.sendFeedback(() -> Text.translatableWithFallback("tardis.remove.progress",
                "Removing TARDIS with id [%s]...", tardis.getUuid()), true
        );

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
                Path file = ServerTardisManager.getSavePath(context.getSource().getServer(), tardis.getUuid(), "json");

                if (Files.exists(file)) {
                    Files.delete(file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ServerTardisManager.getInstance().remove(tardis.getUuid());
        });

        source.sendFeedback(() -> Text.translatableWithFallback("tardis.remove.done",
                "TARDIS [%s] removed", tardis.getUuid()), true
        );

        return Command.SINGLE_SUCCESS;
    }
}
