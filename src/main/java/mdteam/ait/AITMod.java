package mdteam.ait;

import com.google.common.eventbus.Subscribe;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import mdteam.ait.core.*;
import mdteam.ait.core.components.block.exterior.ExteriorNBTComponent;
import mdteam.ait.core.components.block.radio.RadioNBTComponent;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import mdteam.ait.core.components.world.tardis.TARDISComponent;
import mdteam.ait.core.helper.desktop.DesktopInit;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.client.util.telemetry.WorldLoadedEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiling.jfr.event.WorldLoadFinishedEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static io.wispforest.owo.network.serialization.PacketBufSerializer.maybeGet;

public class AITMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	//Component Keys

	//Items

	//Blocks/Block Entities
	public static final ComponentKey<RadioNBTComponent> RADIONBT =
			ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "radionbt"), RadioNBTComponent.class);

	public static final ComponentKey<ExteriorNBTComponent> EXTERIORNBT =
			ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "exteriornbt"), ExteriorNBTComponent.class);

	public static final ComponentKey<TARDISComponent> TARDISNBT =
			ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "tardisnbt"), TARDISComponent.class);
	public static MinecraftServer mcServer = null;
	public static final OwoItemGroup AIT_ITEM_GROUP = OwoItemGroup.builder(new Identifier(AITMod.MOD_ID, "item_group"), () -> Icon.of(AITItems.AITMODCREATIVETAB.getDefaultStack())).build();

	public static final String MOD_ID = "ait";

    public static final Logger LOGGER = LoggerFactory.getLogger("ait");

	@Override
	public void onInitialize() {
		FieldRegistrationHandler.register(AITItems.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITBlocks.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITSounds.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITBlockEntityTypes.class, MOD_ID, false);
		//FieldRegistrationHandler.register(AITDimensions.class, MOD_ID, false);
		AIT_ITEM_GROUP.initialize();
		DesktopInit.init();

		ServerWorldEvents.LOAD.register((server, world) -> {
			if (world.getRegistryKey().equals(World.OVERWORLD)) {
				mcServer = server;
			}
		});
		ServerWorldEvents.UNLOAD.register((server, world) -> {
			if (world.getRegistryKey().equals(World.OVERWORLD)) {
				mcServer = null; // Prevents an annoying crash
			}
		});
	}

}