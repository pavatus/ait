package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.permissions.Permission;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static loqor.ait.core.commands.TeleportInteriorCommand.TARDIS_SUGGESTION;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PermissionTestCommand {

    private static final String TEXT = "%s permission '%s' for player %s with value %s. Result: %s";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("permission").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("tardis", UuidArgumentType.uuid()).suggests(TARDIS_SUGGESTION)
                                .then(argument("player", EntityArgumentType.player())
                                        .then(argument("permission", StringArgumentType.string())
                                                .then(argument("value", BoolArgumentType.bool())
                                                        .then(literal("set").executes(PermissionTestCommand::set))
                                                        .then(literal("get").executes(PermissionTestCommand::get))
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int get(CommandContext<ServerCommandSource> context) {
        return run(context, Operation.CHECK);
    }

    private static int set(CommandContext<ServerCommandSource> context) {
        return run(context, Operation.SET);
    }

    private static int run(CommandContext<ServerCommandSource> context, Operation op) {
        try {
            Tardis tardis = ServerTardisManager.getInstance().getTardis(UuidArgumentType.getUuid(context, "tardis"));
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            Permission permission = Permission.from(StringArgumentType.getString(context, "permission"));
            boolean value = BoolArgumentType.getBool(context, "value");

            boolean result = op.run(tardis, player, permission, value);
            AITMod.LOGGER.debug(String.format(TEXT, op, permission.toString(), player.getName(), value, result));
            context.getSource().sendMessage(Text.literal(
                    String.format(TEXT, op, permission, player.getName(), value, result)
            ));
        } catch (Exception e) {
            context.getSource().sendFeedback(() -> Text.literal(
                    "Failed to parse command! " + e.getMessage()
            ), false);
        }

        return Command.SINGLE_SUCCESS;
    }

    enum Operation {
        SET {
            @Override
            public boolean run(Tardis tardis, ServerPlayerEntity player, Permission permission, boolean value) {
                tardis.getHandlers().getPermissions().set(player, permission, value);
                System.out.println("set");
                return value;
            }
        },
        CHECK {
            @Override
            public boolean run(Tardis tardis, ServerPlayerEntity player, Permission permission, boolean value) {
                return tardis.getHandlers().getPermissions().check(player, permission) == value;
            }
        };

        public abstract boolean run(Tardis tardis, ServerPlayerEntity player, Permission permission, boolean value);
    }
}
