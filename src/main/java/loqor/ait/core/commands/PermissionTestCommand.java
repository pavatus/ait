package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.permissions.Permission;
import loqor.ait.tardis.data.permissions.PermissionHandler;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Function;

import static loqor.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PermissionTestCommand {

    public static final SuggestionProvider<ServerCommandSource> PERMISSION = (context, builder) ->
            CommandSource.suggestMatching(Permission.collect(), builder);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("permission").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
                                .then(argument("player", EntityArgumentType.player())
                                        .then(argument("permission", StringArgumentType.string()).suggests(PERMISSION)
                                                .executes(PermissionTestCommand::get)
                                                .then(argument("value", BoolArgumentType.bool())
                                                        .executes(PermissionTestCommand::set))
                                        )
                                )
                        )
                )
        );
    }

    private static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        CommonArgs args = CommonArgs.create(context);
        boolean value = BoolArgumentType.getBool(context, "value");

        args.run("ait.command.permission.set", "Set permission '%s' for player %s to '%s'",
                handler -> handler.set(args.player, args.permission, value));

        return Command.SINGLE_SUCCESS;
    }

    private static int get(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        CommonArgs args = CommonArgs.create(context);
        return args.run("ait.command.permission.get", "Permission check '%s' for player %s: '%s'",
                handler -> handler.check(args.player, args.permission)) ? 1 : 0;
    }

    record CommonArgs(ServerCommandSource source, Tardis tardis, ServerPlayerEntity player, Permission permission) {

        public static CommonArgs create(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            Permission permission = Permission.from(StringArgumentType.getString(context, "permission"));

            return new CommonArgs(context.getSource(), tardis, player, permission);
        }

        public boolean run(String key, String fallback, Function<PermissionHandler, Boolean> func) {
            boolean result = func.apply(this.tardis.getHandlers().getPermissions());

            this.source.sendFeedback(() -> Text.translatableWithFallback(
                    key, fallback, this.permission, this.player.getName(), result
            ), false);

            return result;
        }
    }
}
