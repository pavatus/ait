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

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RemoveCommand {

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
        AbsoluteBlockPos exterior = tardis.getExteriorPos();

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
        ServerTardisManager.getInstance().remove(context.getSource().getServer(), tardis.getUuid());

        source.sendFeedback(() -> Text.translatableWithFallback("tardis.remove.done",
                "TARDIS [%s] removed", tardis.getUuid()), true
        );

        return Command.SINGLE_SUCCESS;
    }
}
