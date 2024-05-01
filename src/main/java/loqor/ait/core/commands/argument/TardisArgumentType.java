package loqor.ait.core.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TardisArgumentType implements ArgumentType<ServerTardis> {

    public static final SimpleCommandExceptionType INVALID_UUID = new SimpleCommandExceptionType(Text.translatable("argument.uuid.invalid"));

    private static final Collection<String> EXAMPLES = List.of("dd12be42-52a9-4a91-a8a1-11c01849e498");
    private static final Pattern VALID_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");

    public static ServerTardis getTardis(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, ServerTardis.class);
    }

    public static TardisArgumentType tardis() {
        return new TardisArgumentType();
    }

    @Override
    public ServerTardis parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.getRemaining();
        Matcher matcher = VALID_CHARACTERS.matcher(string);
        if (matcher.find()) {
            String raw = matcher.group(1);

            try {
                UUID uuid = UUID.fromString(raw);
                reader.setCursor(reader.getCursor() + raw.length());

                ServerTardis tardis = ServerTardisManager.getInstance().getTardis(uuid);

                if (tardis == null)
                    throw INVALID_UUID.create();

                return tardis;
            } catch (IllegalArgumentException ignored) { }
        }

        throw INVALID_UUID.create();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(ServerTardisManager.getInstance().getLookup().keySet().stream().map(UUID::toString), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
