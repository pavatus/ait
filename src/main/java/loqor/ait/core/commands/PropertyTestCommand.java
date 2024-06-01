package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.PropertyTestHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PropertyTestCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("property-test").requires(source -> source.hasPermissionLevel(2))
                        .then(literal("get").then(argument("tardis", TardisArgumentType.tardis())
                                .executes(PropertyTestCommand::get)))
                        .then(literal("set").then(argument("tardis", TardisArgumentType.tardis())
                                .executes(PropertyTestCommand::set)))
                )
        );
    }

    private static int get(CommandContext<ServerCommandSource> context) {
        Tardis tardis = TardisArgumentType.getTardis(context, "tardis");
        PropertyTestHandler handler = tardis.handler(TardisComponent.Id.TESTING);
        context.getSource().sendMessage(Text.literal("Value: " + handler.getBool().get()));


        return Command.SINGLE_SUCCESS;
    }

    private static int set(CommandContext<ServerCommandSource> context) {
        Tardis tardis = TardisArgumentType.getTardis(context, "tardis");
        boolean value = BoolArgumentType.getBool(context, "value");

        PropertyTestHandler handler = tardis.handler(TardisComponent.Id.TESTING);
        handler.getBool().set(value);

        context.getSource().sendMessage(Text.literal("New value: " + value));
        return Command.SINGLE_SUCCESS;
    }
}
