package loqor.ait.core.commands;

import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.TardisUtil;

public class GetInsideTardisCommand {

    // TODO: add BlockPosition argument type
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID).then(literal("tardis_id_from_interior")
                .requires(source -> source.hasPermissionLevel(2)).executes(GetInsideTardisCommand::runCommand)));
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        Entity source = context.getSource().getEntity();
        Tardis tardis = TardisUtil.findTardisByInterior(source.getBlockPos(), true);

        source.sendMessage(Text.translatableWithFallback("tardis.id", "TARDIS ID: %s", tardis.getUuid()));

        return Command.SINGLE_SUCCESS;
    }
}
