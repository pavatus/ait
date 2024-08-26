package loqor.ait.core.commands;

import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import loqor.ait.AITMod;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.core.util.TextUtil;

public class GetInsideTardisCommand {

    // TODO: add BlockPosition argument type
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID).then(literal("this").requires(source -> source.hasPermissionLevel(2))
                .executes(GetInsideTardisCommand::runCommand)));
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        Entity source = context.getSource().getEntity();
        Tardis tardis = TardisUtil.findTardisByInterior(source.getBlockPos(), true);

        source.sendMessage(Text.translatable("message.ait.id").append(TextUtil.forTardis(tardis)));
        return Command.SINGLE_SUCCESS;
    }
}
