package loqor.ait;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import loqor.ait.api.AITModInitializer;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.compat.immersive.PortalsHandler;
import loqor.ait.core.*;
import loqor.ait.core.advancement.TardisCriterions;
import loqor.ait.core.commands.*;
import loqor.ait.core.data.schema.MachineRecipeSchema;
import loqor.ait.core.entities.ConsoleControlEntity;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.item.component.AbstractTardisPart;
import loqor.ait.core.item.part.MachineItem;
import loqor.ait.core.managers.RiftChunkManager;
import loqor.ait.core.screen_handlers.EngineScreenHandler;
import loqor.ait.core.util.AITConfig;
import loqor.ait.core.util.StackUtil;
import loqor.ait.registry.Registries;
import loqor.ait.registry.impl.*;
import loqor.ait.registry.impl.console.ConsoleRegistry;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.InteriorChangingHandler;
import loqor.ait.tardis.data.ServerHumHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.sound.HumSound;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class AITMod implements ModInitializer {

	public static final String MOD_ID = "ait";
	public static final Logger LOGGER = LoggerFactory.getLogger("ait");
	public static final AITConfig AIT_CONFIG = AITConfig.createAndLoad();
	public static final OwoItemGroup AIT_ITEM_GROUP = OwoItemGroup.builder(new Identifier(AITMod.MOD_ID, "item_group"), () ->
			Icon.of(AITItems.TARDIS_ITEM)).disableDynamicTitle().build();
	public static final Random RANDOM = new Random();
	public static final RegistryKey<PlacedFeature> CUSTOM_GEODE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(MOD_ID, "zeiton_geode"));

	public static final ScreenHandlerType<EngineScreenHandler> ENGINE_SCREEN_HANDLER;

	static {
		ENGINE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "engine"), EngineScreenHandler::new);
	}

	@Override
	public void onInitialize() {
		ConsoleRegistry.init();
		HumsRegistry.init();
		CreakRegistry.init();
		SequenceRegistry.init();

		// For all the addon devs
		FabricLoader.getInstance().invokeEntrypoints("ait-main", AITModInitializer.class, AITModInitializer::onInitializeAIT);

		Registries.getInstance().subscribe(Registries.InitType.COMMON);
		DoorRegistry.init();

		//ServerVortexDataHandler.init();

		AITArgumentTypes.register();

		FieldRegistrationHandler.register(AITItems.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITBlocks.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITSounds.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITBlockEntityTypes.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITEntityTypes.class, MOD_ID, false);

		// important to init after items registration
		BlueprintRegistry.init();

		TardisUtil.init();
		ServerTardisManager.init();
		RiftChunkManager.init();
		TardisCriterions.init();

		entityAttributeRegister();

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, CUSTOM_GEODE_PLACED_KEY);

		// ip support
		if (DependencyChecker.hasPortals())
			PortalsHandler.init();

		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			TeleportInteriorCommand.register(dispatcher);
			SummonTardisCommand.register(dispatcher);
			SetLockedCommand.register(dispatcher);
			GetInsideTardisCommand.register(dispatcher);
			RealWorldCommand.register(dispatcher);
			FuelCommand.register(dispatcher);
			SetRepairTicksCommand.register(dispatcher);
			RiftChunkCommand.register(dispatcher);
			SetNameCommand.register(dispatcher);
			GetNameCommand.register(dispatcher);
			GetCreatorCommand.register(dispatcher);
			SetMaxSpeedCommand.register(dispatcher);
			SetSiegeCommand.register(dispatcher);
			LinkCommand.register(dispatcher);
			PropertyCommand.register(dispatcher);
			RemoveCommand.register(dispatcher);
			PermissionCommand.register(dispatcher);
			LoyaltyCommand.register(dispatcher);
			UnlockCommand.register(dispatcher);
			DataCommand.register(dispatcher);
			TravelDebugCommand.register(dispatcher);
			VersionCommand.register(dispatcher);
		}));

		TardisEvents.CRASH.register(tardis -> {
			for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis)) {
				TardisCriterions.CRASH.trigger(player);
			}
		});

		TardisEvents.REGAIN_POWER.register(tardis -> tardis.getDesktop().playSoundAtEveryConsole(
				AITSounds.POWERUP, SoundCategory.AMBIENT, 10f, 1f)
		);

		ServerPlayNetworking.registerGlobalReceiver(InteriorChangingHandler.CHANGE_DESKTOP, ((server, player, handler, buf, responseSender) -> {
			ServerTardisManager.getInstance().getTardis(server, buf.readUuid(), tardis -> {
				TardisDesktopSchema desktop = DesktopRegistry.getInstance().get(buf.readIdentifier());

				if (tardis == null || desktop == null)
					return;

				// nuh uh no interior changing during flight
				if (tardis.travel().getState() != TravelHandlerBase.State.LANDED)
					return;

				tardis.<InteriorChangingHandler>handler(TardisComponent.Id.INTERIOR).queueInteriorChange(desktop);
			});
		}));

		ServerPlayNetworking.registerGlobalReceiver(ServerHumHandler.RECEIVE, ((server, player, handler, buf, responseSender) -> {
			ServerTardisManager.getInstance().getTardis(server, buf.readUuid(), tardis -> {
				HumSound hum = HumSound.fromName(buf.readString(), buf.readString());

				if (tardis == null || hum == null)
					return;

				tardis.<ServerHumHandler>handler(TardisComponent.Id.HUM).setHum(hum);
			});
		}));

		ServerPlayNetworking.registerGlobalReceiver(TardisDesktop.CACHE_CONSOLE, (server, player, handler, buf, responseSender) -> {
			ServerTardisManager.getInstance().getTardis(server, buf.readUuid(), tardis -> {
				BlockPos console = buf.readBlockPos();

				server.execute(() -> {
					if (tardis == null)
						return;

					tardis.getDesktop().cacheConsole(console);
				});
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(PropertiesHandler.LEAVEBEHIND, (server, player, handler, buf, responseSender) -> {
			ServerTardisManager.getInstance().getTardis(server, buf.readUuid(), tardis -> {
				boolean behind = buf.readBoolean();

				server.execute(() -> {
					if (tardis == null)
						return;

					PropertiesHandler.set(tardis, PropertiesHandler.LEAVE_BEHIND, behind);
				});
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(PropertiesHandler.HOSTILEALARMS, (server, player, handler, buf, responseSender) -> {
			ServerTardisManager.getInstance().getTardis(server, buf.readUuid(), tardis -> {
				boolean hostile = buf.readBoolean();

				server.execute(() -> {
					if (tardis == null)
						return;

					PropertiesHandler.set(tardis, PropertiesHandler.HOSTILE_PRESENCE_TOGGLE, hostile);
				});
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(MachineItem.MACHINE_DISASSEMBLE, (server, player, handler, buf, responseSender) -> {
			ItemStack machine = buf.readItemStack();

			Optional<MachineRecipeSchema> schema = MachineRecipeRegistry.getInstance().findMatching(machine);

			if (schema.isEmpty())
				return;

			// this should ALWAYS be executed on the main thread
			server.execute(() -> {
				SonicItem.playSonicSounds(player);
				MachineItem.disassemble(player, machine, schema.get());

				StackUtil.playBreak(player);
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(AbstractTardisPart.DISASSEMBLE, (server, player, handler, buf, responseSender) -> {
			ItemStack machine = buf.readItemStack();

			Optional<MachineRecipeSchema> schema = MachineRecipeRegistry.getInstance().findMatching(machine);

			if (schema.isEmpty())
				return;

			// this should ALWAYS be executed on the main thread
			server.execute(() -> {
				SonicItem.playSonicSounds(player);
				AbstractTardisPart.disassemble(player, machine, schema.get());

				StackUtil.playBreak(player);
			});
		});

		ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
			AIT_CONFIG.save();
		});

		TardisEvents.DEMAT.register(tardis -> {
			for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis)) {
				TardisCriterions.TAKEOFF.trigger(player);
			}

			return TardisEvents.Interaction.PASS;
		});

		AIT_ITEM_GROUP.initialize();
	}

	public void entityAttributeRegister() {
		FabricDefaultAttributeRegistry.register(AITEntityTypes.CONTROL_ENTITY_TYPE, ConsoleControlEntity.createDummyAttributes());
		FabricDefaultAttributeRegistry.register(AITEntityTypes.TARDIS_REAL_ENTITY_TYPE, TardisRealEntity.createLivingAttributes());
	}

	public static final Identifier OPEN_SCREEN = new Identifier(AITMod.MOD_ID, "open_screen");
	public static final Identifier OPEN_SCREEN_TARDIS = new Identifier(AITMod.MOD_ID, "open_screen_tardis");
	public static final Identifier OPEN_SCREEN_CONSOLE = new Identifier(AITMod.MOD_ID, "open_screen_console");

	public static void openScreen(ServerPlayerEntity player, int id, UUID tardis) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(id);
		buf.writeUuid(tardis);
		ServerPlayNetworking.send(player, OPEN_SCREEN_TARDIS, buf);
	}

	public static void openScreen(ServerPlayerEntity player, int id, UUID tardis, BlockPos console) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(id);
		buf.writeUuid(tardis);
		buf.writeBlockPos(console);

		ServerPlayNetworking.send(player, OPEN_SCREEN_CONSOLE, buf);
	}
}