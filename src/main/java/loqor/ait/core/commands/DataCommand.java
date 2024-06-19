package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import loqor.ait.AITMod;
import loqor.ait.core.commands.argument.TardisArgumentType;
import loqor.ait.registry.impl.TardisComponentRegistry;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.lang.reflect.ParameterizedType;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DataCommand {

    public static final SuggestionProvider<ServerCommandSource> COMPONENT_SUGGESTION = (context, builder) ->
            CommandSource.suggestMatching(TardisComponentRegistry.getInstance().getValues().stream().map(TardisComponent.IdLike::name), builder);

    public static final SuggestionProvider<ServerCommandSource> VALUE_SUGGESTION = (context, builder) -> {
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");
        String rawComponent = StringArgumentType.getString(context, "component");

        TardisComponent.IdLike id = TardisComponentRegistry.getInstance().get(rawComponent);

        if (!(tardis.handler(id) instanceof KeyedTardisComponent keyed))
            return builder.buildFuture(); // womp womp

        return CommandSource.suggestMatching(keyed.getPropertyData().values()
                .stream().map(value -> value.getProperty().getName()), builder);
    };


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("data").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("tardis", TardisArgumentType.tardis())
                                .then(argument("component", StringArgumentType.word())
                                        .suggests(COMPONENT_SUGGESTION)
                                        .then(argument("value", StringArgumentType.word())
                                                .suggests(VALUE_SUGGESTION)
                                                .then(literal("set")
                                                        .then(argument("data", NbtElementArgumentType.nbtElement())
                                                                .executes(DataCommand::runSet)))
                                                .then(literal("get")
                                                        .executes(DataCommand::runGet))
                                        )
                                )
                        )
                )
        );
    }

    private static <T> int runGet(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

        String rawComponent = StringArgumentType.getString(context, "component");
        String valueName = StringArgumentType.getString(context, "value");

        TardisComponent.IdLike id = TardisComponentRegistry.getInstance().get(rawComponent);

        if (!(tardis.handler(id) instanceof KeyedTardisComponent keyed)) {
            source.sendMessage(Text.translatable("command.ait.data.fail", valueName, rawComponent));
            return 0; // womp womp
        }

        Value<T> value = keyed.getPropertyData().getExact(valueName);
        T obj = value.get();

        source.sendMessage(Text.translatable("command.ait.data.get", valueName, obj.toString()));
        return Command.SINGLE_SUCCESS;
    }

    private static <T> int runSet(CommandContext<ServerCommandSource> context) {
        try {
            ServerCommandSource source = context.getSource();
            ServerTardis tardis = TardisArgumentType.getTardis(context, "tardis");

            String rawComponent = StringArgumentType.getString(context, "component");
            String valueName = StringArgumentType.getString(context, "value");

            NbtElement rawData = NbtElementArgumentType.getNbtElement(context, "data");
            //JsonElement rawData = JsonElementArgumentType.getJsonElement(context, "data");
            TardisComponent.IdLike id = TardisComponentRegistry.getInstance().get(rawComponent);

            if (!(tardis.handler(id) instanceof KeyedTardisComponent keyed)) {
                source.sendMessage(Text.translatable("command.ait.data.fail", valueName, rawComponent));
                return 0; // womp womp
            }

            Value<T> value = keyed.getPropertyData().getExact(valueName);
            Class<T> classOfT = DataCommand.reflectClassType(value);

            T obj = ServerTardisManager.getInstance().getFileGson().fromJson(rawData.toString(), classOfT);

            value.set(obj);
            source.sendMessage(Text.translatable("command.ait.data.set", valueName, obj.toString()));
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> reflectClassType(Value<T> value) {
        return (Class<T>) ((ParameterizedType) value.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
