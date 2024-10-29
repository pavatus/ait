package loqor.ait.core.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.ServerCommandSource;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.handler.mood.MoodHandler;

public class TriggerMoodRollCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID).then(literal("trigger-mood-roll")
                .requires(source -> source.hasPermissionLevel(2)).then(argument("tardis", TardisArgumentType.tardis())
                        .executes(TriggerMoodRollCommand::triggerMoodRollCommand))));
    }

    private static int triggerMoodRollCommand(CommandContext<ServerCommandSource> context) {

        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

        tardis.<MoodHandler>handler(TardisComponent.Id.MOOD).rollForMoodDictatedEvent();

        return Command.SINGLE_SUCCESS;
    }
}
