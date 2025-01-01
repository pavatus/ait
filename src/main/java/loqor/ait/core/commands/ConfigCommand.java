package loqor.ait.core.commands;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import dev.pavatus.config.AITConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.minecraft.client.MinecraftClient;

import loqor.ait.AITMod;


public class ConfigCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal(AITMod.MOD_ID).then(
                        literal("config").executes(context -> {
                            MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(AITConfig.class, null).get());
                            return Command.SINGLE_SUCCESS;
                        })));
    }
}
