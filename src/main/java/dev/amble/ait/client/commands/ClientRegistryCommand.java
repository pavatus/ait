package dev.amble.ait.client.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import dev.amble.ait.AITMod;
import dev.amble.ait.config.AITConfig;
import dev.amble.ait.registry.v2.AITRegistries;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientRegistryCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal(AITMod.MOD_ID + "-client").then(literal("registry").executes(context -> {
            AITRegistries.EXTERIOR_VARIANT.getClient().forEach(System.out::println);

            return Command.SINGLE_SUCCESS;
        })));
    }
}
