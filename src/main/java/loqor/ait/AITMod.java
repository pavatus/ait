package loqor.ait;

import static dev.pavatus.planet.core.planet.Crater.CRATER_ID;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import dev.pavatus.config.AITConfig;
import dev.pavatus.lib.container.RegistryContainer;
import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.pavatus.module.ModuleRegistry;
import dev.pavatus.planet.core.planet.Crater;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.feature.PlacedFeature;

import loqor.ait.api.AITModInitializer;
import loqor.ait.core.*;
import loqor.ait.core.advancement.TardisCriterions;
import loqor.ait.core.commands.*;
import loqor.ait.core.entities.ConsoleControlEntity;
import loqor.ait.core.entities.FlightTardisEntity;
import loqor.ait.core.item.blueprint.BlueprintRegistry;
import loqor.ait.core.item.component.AbstractTardisPart;
import loqor.ait.core.item.part.MachineItem;
import loqor.ait.core.screen_handlers.EngineScreenHandler;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.core.tardis.util.AsyncLocatorUtil;
import loqor.ait.core.tardis.util.NetworkUtil;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.core.util.CustomTrades;
import loqor.ait.core.util.StackUtil;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.core.world.LandingPadManager;
import loqor.ait.core.world.RiftChunkManager;
import loqor.ait.data.landing.LandingPadRegion;
import loqor.ait.data.schema.MachineRecipeSchema;
import loqor.ait.datagen.datagen_providers.loot.SetBlueprintLootFunction;
import loqor.ait.registry.impl.*;
import loqor.ait.registry.impl.console.ConsoleRegistry;
import loqor.ait.registry.impl.door.DoorRegistry;

public class AITMod implements ModInitializer {

    public static final String MOD_ID = "ait";
    public static final Logger LOGGER = LoggerFactory.getLogger("ait");
    public static final Random RANDOM = new Random();

    public static final AITConfig CONFIG = AITConfig.createAndLoad();
    public static final GameRules.Key<GameRules.BooleanRule> TARDIS_GRIEFING = GameRuleRegistry.register("tardisGriefing",
            GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));


    public static final RegistryKey<PlacedFeature> CUSTOM_GEODE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE,
            new Identifier(MOD_ID, "zeiton_geode"));

    public static final Crater CRATER = new Crater(ProbabilityConfig.CODEC);

    public static final ScreenHandlerType<EngineScreenHandler> ENGINE_SCREEN_HANDLER;

    static {
        ENGINE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "engine"),
                EngineScreenHandler::new);
    }

    public static final String BRANCH;

    static {
        // ait-1.x.x.xxx-1.20.1-xxxx-xxxx
        String version = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
        // get the last part of the version string after the -
        BRANCH = version.substring(version.lastIndexOf("-") + 1);
    }

    public static boolean isUnsafeBranch() {
        return !BRANCH.equals("release");
    }

    @Override
    public void onInitialize() {
        ServerLifecycleHooks.init();
        NetworkUtil.init();
        AsyncLocatorUtil.setupExecutorService();

        ConsoleRegistry.init();
        CreakRegistry.init();
        SequenceRegistry.init();
        MoodEventPoolRegistry.init();
        LandingPadManager.init();
        ControlRegistry.init();
        RiftChunkManager.init();

        // For all the addon devs
        FabricLoader.getInstance().invokeEntrypoints("ait-main", AITModInitializer.class,
                AITModInitializer::onInitializeAIT);

        DoorRegistry.init();

        AITStatusEffects.init();
        AITVillagers.init();
        AITArgumentTypes.register();
        AITSounds.init();
        AITDimensions.init();

        CustomTrades.registerCustomTrades();

        RegistryContainer.register(AITItemGroups.class, MOD_ID);
        RegistryContainer.register(AITItems.class, MOD_ID);
        RegistryContainer.register(AITBlocks.class, MOD_ID);
        RegistryContainer.register(AITBlockEntityTypes.class, MOD_ID);
        RegistryContainer.register(AITEntityTypes.class, MOD_ID);
        RegistryContainer.register(AITPaintings.class, MOD_ID);
        ModuleRegistry.instance().onCommonInit();

        BlueprintRegistry.BLUEPRINT_TYPE = Registry.register(Registries.LOOT_FUNCTION_TYPE,
                AITMod.id("set_blueprint"),
                new LootFunctionType(new SetBlueprintLootFunction.Serializer()));

        WorldUtil.init();
        TardisUtil.init();

        ServerTardisManager.init();
        TardisCriterions.init();

        entityAttributeRegister();

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                CUSTOM_GEODE_PLACED_KEY);

        Registry.register(net.minecraft.registry.Registries.FEATURE, CRATER_ID, CRATER);

        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            TeleportInteriorCommand.register(dispatcher);
            SummonTardisCommand.register(dispatcher);
            SetLockedCommand.register(dispatcher);
            GetInsideTardisCommand.register(dispatcher);
            FuelCommand.register(dispatcher);
            SetRepairTicksCommand.register(dispatcher);
            RiftChunkCommand.register(dispatcher);
            TriggerMoodRollCommand.register(dispatcher);
            SetNameCommand.register(dispatcher);
            GetNameCommand.register(dispatcher);
            GetCreatorCommand.register(dispatcher);
            SetMaxSpeedCommand.register(dispatcher);
            SetSiegeCommand.register(dispatcher);
            LinkCommand.register(dispatcher);
            RemoveCommand.register(dispatcher);
            PermissionCommand.register(dispatcher);
            LoyaltyCommand.register(dispatcher);
            UnlockCommand.register(dispatcher);
            DataCommand.register(dispatcher);
            TravelDebugCommand.register(dispatcher);
            VersionCommand.register(dispatcher);
            SafePosCommand.register(dispatcher);
            ListCommand.register(dispatcher);
            LoadCommand.register(dispatcher);
            DebugCommand.register(dispatcher);
            EraseChunksCommand.register(dispatcher);
            FlightCommand.register(dispatcher);
        }));

        ServerPlayNetworking.registerGlobalReceiver(TardisUtil.REGION_LANDING_CODE,
                (server, player, handler, buf, responseSender) -> {
                    BlockPos pos = buf.readBlockPos();
                    String landingCode = buf.readString();

                    server.execute(() -> {
                        LandingPadRegion region = LandingPadManager.getInstance((ServerWorld) player.getWorld()).getRegionAt(pos);

                        if (region == null)
                            return;

                        region.setLandingCode(landingCode);
                        LandingPadManager.Network.syncTracked(LandingPadManager.Network.Action.ADD, player.getServerWorld(),
                                new ChunkPos(player.getBlockPos()));
                    });
                });

        ServerPlayNetworking.registerGlobalReceiver(MachineItem.MACHINE_DISASSEMBLE,
                (server, player, handler, buf, responseSender) -> {
                    ItemStack machine = buf.readItemStack();

                    Optional<MachineRecipeSchema> schema = MachineRecipeRegistry.getInstance().findMatching(machine);

                    if (schema.isEmpty())
                        return;

                    // this should ALWAYS be executed on the main thread
                    server.execute(() -> {
                        MachineItem.disassemble(player, machine, schema.get());

                        StackUtil.playBreak(player);
                    });
                });

        ServerPlayNetworking.registerGlobalReceiver(AbstractTardisPart.DISASSEMBLE,
                (server, player, handler, buf, responseSender) -> {
                    ItemStack machine = buf.readItemStack();

                    Optional<MachineRecipeSchema> schema = MachineRecipeRegistry.getInstance().findMatching(machine);

                    if (schema.isEmpty())
                        return;

                    // this should ALWAYS be executed on the main thread
                    server.execute(() -> {
                        AbstractTardisPart.disassemble(player, machine, schema.get());

                        StackUtil.playBreak(player);
                    });
                });

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin()
                    && (id.equals(LootTables.NETHER_BRIDGE_CHEST) || id.equals(LootTables.DESERT_PYRAMID_CHEST)
                    || id.equals(LootTables.VILLAGE_ARMORER_CHEST))
                    || id.equals(LootTables.END_CITY_TREASURE_CHEST) || id.equals(LootTables.SHIPWRECK_MAP_CHEST)
                    || id.equals(LootTables.SIMPLE_DUNGEON_CHEST) || id.equals(LootTables.STRONGHOLD_LIBRARY_CHEST)) {


                LootPool.Builder poolBuilder = LootPool.builder().with(ItemEntry.builder(AITItems.BLUEPRINT).weight(10));

                tableBuilder.pool(poolBuilder);
            }
        });
    }

    public void entityAttributeRegister() {
        FabricDefaultAttributeRegistry.register(AITEntityTypes.CONTROL_ENTITY_TYPE,
                ConsoleControlEntity.createDummyAttributes());

        FabricDefaultAttributeRegistry.register(AITEntityTypes.FLIGHT_TARDIS_TYPE,
                FlightTardisEntity.createDummyAttributes());
    }

    public static final Identifier OPEN_SCREEN = AITMod.id("open_screen");
    public static final Identifier OPEN_SCREEN_TARDIS = AITMod.id("open_screen_tardis");
    public static final Identifier OPEN_SCREEN_CONSOLE = AITMod.id("open_screen_console");

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

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
