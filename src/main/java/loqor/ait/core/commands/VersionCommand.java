package loqor.ait.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.AITMod;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.literal;

public class VersionCommand {

    private static final ModContainer AIT = FabricLoader.getInstance().getModContainer(AITMod.MOD_ID).get();

    private static final String LOGO = """
           ::::::\\\\     ::::::::::::|| ::::::::::::::::||
          == ==\\\\      ==||      ==||
         =======\\\\     ==||      ==||
        ##//   ##\\\\    ##||      ##||
       ##//     ##\\\\ ######||    ##||
            """;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID)
                .then(literal("version").requires(source -> source.hasPermissionLevel(2))
                        .executes(VersionCommand::run)
                )
        );
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        source.sendMessage(Text.literal(LOGO).copy().setStyle(Style.EMPTY.withFont(new Identifier("uniform"))).formatted(Formatting.GOLD));
        source.sendMessage(Text.translatable("message.ait.version", AIT.getMetadata().getVersion().getFriendlyString()));

        return Command.SINGLE_SUCCESS;
    }
}
