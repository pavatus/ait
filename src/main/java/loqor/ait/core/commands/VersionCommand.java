package loqor.ait.core.commands;

import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import loqor.ait.tardis.data.landing.LandingPadManager;
import loqor.ait.tardis.data.landing.LandingPadRegion;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.WorldWithTardis;
import net.minecraft.world.World;

public class VersionCommand {

    private static final ModContainer AIT = FabricLoader.getInstance().getModContainer(AITMod.MOD_ID).get();
    private static final String VERSION = AIT.getMetadata().getVersion().getFriendlyString();

    private static final Text LOGO = Text.literal("""
                ::::::\\\\     ::::::::::::|| ::::::::::::::::||
               == ==\\\\      ==||      ==||
              =======\\\\     ==||      ==||
             ##//   ##\\\\    ##||      ##||
            ##//     ##\\\\ ######||    ##||""").copy().setStyle(Style.EMPTY.withFont(new Identifier("uniform")));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID).then(
                literal("version").requires(source -> source.hasPermissionLevel(2)).executes(VersionCommand::run)));
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        source.sendMessage(LOGO.copy().formatted(Formatting.GOLD));
        source.sendMessage(Text.translatable("message.ait.version").formatted(Formatting.GOLD)
                .append(Text.literal(": ").append(VERSION).formatted(Formatting.WHITE)));

        if (!source.isExecutedByPlayer())
            return Command.SINGLE_SUCCESS;

        ServerWorld world = source.getWorld();
        PlayerEntity player = source.getPlayer();

        ((WorldWithTardis) world).ait$withLookup(lookup -> {
            source.sendMessage(Text.empty());
            source.sendMessage(Text.literal("TARDIS in chunk: " + lookup.get(player.getChunkPos())));
        });

        LandingPadRegion region = LandingPadManager.getInstance(world).getRegion(player.getChunkPos());

        if (region != null) {
            source.sendMessage(Text.literal("LP in chunk: " + region));
        }

        return Command.SINGLE_SUCCESS;
    }
}
