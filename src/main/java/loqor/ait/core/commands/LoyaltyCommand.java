package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;
import java.util.function.Function;

import static loqor.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LoyaltyCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("loyalty").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(LoyaltyCommand::get)
                                        .then(argument("value", BoolArgumentType.bool())
                                                .executes(LoyaltyCommand::set))
                                )
                        )
                )
        );
    }

    private static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        CommonArgs args = CommonArgs.create(context);
        int value = IntegerArgumentType.getInteger(context, "value");

        args.run("ait.command.loyalty.set", "Set loyalty for player %s to rank %s level %s",
                handler -> handler.set(args.player, Loyalty.fromLevel(value)));

        return Command.SINGLE_SUCCESS;
    }

    private static int get(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        CommonArgs args = CommonArgs.create(context);

        return args.run("ait.command.loyalty.get", "Player %s has rank %s with level %s",
                handler -> handler.get(args.player)).level();
    }

    record CommonArgs(ServerCommandSource source, Tardis tardis, ServerPlayerEntity player) {

        public static CommonArgs create(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            UUID uuid = UuidArgumentType.getUuid(context, "tardis");
            Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);

            if (tardis == null)
                throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), () -> "No tardis with id '" + uuid + "'");

            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            return new CommonArgs(context.getSource(), tardis, player);
        }

        public Loyalty run(String key, String fallback, Function<LoyaltyHandler, Loyalty> func) {
            Loyalty result = func.apply(this.tardis.getHandlers().getLoyalties());

            this.source.sendFeedback(() -> Text.translatableWithFallback(
                    key, fallback, this.player.getName(), result.type(), result.level()
            ), false);

            return result;
        }
    }
}
