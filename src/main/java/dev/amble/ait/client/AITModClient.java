package dev.amble.ait.client;

import static dev.amble.ait.AITMod.*;
import static dev.amble.ait.core.AITItems.isUnlockedOnThisDay;

import java.util.Calendar;
import java.util.UUID;

import dev.amble.lib.register.AmbleRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import dev.amble.ait.client.commands.ConfigCommand;
import dev.amble.ait.client.data.ClientLandingManager;
import dev.amble.ait.client.overlays.FabricatorOverlay;
import dev.amble.ait.client.overlays.SonicOverlay;
import dev.amble.ait.client.renderers.SonicRendering;
import dev.amble.ait.client.renderers.TardisStar;
import dev.amble.ait.client.renderers.consoles.ConsoleGeneratorRenderer;
import dev.amble.ait.client.renderers.consoles.ConsoleRenderer;
import dev.amble.ait.client.renderers.coral.CoralRenderer;
import dev.amble.ait.client.renderers.decoration.FlagBlockEntityRenderer;
import dev.amble.ait.client.renderers.decoration.PlaqueRenderer;
import dev.amble.ait.client.renderers.decoration.SnowGlobeRenderer;
import dev.amble.ait.client.renderers.doors.DoorRenderer;
import dev.amble.ait.client.renderers.entities.ControlEntityRenderer;
import dev.amble.ait.client.renderers.entities.FallingTardisRenderer;
import dev.amble.ait.client.renderers.entities.FlightTardisRenderer;
import dev.amble.ait.client.renderers.entities.GallifreyFallsPaintingEntityRenderer;
import dev.amble.ait.client.renderers.exteriors.ExteriorRenderer;
import dev.amble.ait.client.renderers.machines.*;
import dev.amble.ait.client.renderers.monitors.MonitorRenderer;
import dev.amble.ait.client.renderers.monitors.WallMonitorRenderer;
import dev.amble.ait.client.screens.BlueprintFabricatorScreen;
import dev.amble.ait.client.screens.MonitorScreen;
import dev.amble.ait.client.sonic.SonicModels;
import dev.amble.ait.client.tardis.ClientTardis;
import dev.amble.ait.client.tardis.manager.ClientTardisManager;
import dev.amble.ait.client.util.ClientTardisUtil;
import dev.amble.ait.core.*;
import dev.amble.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import dev.amble.ait.core.blockentities.ExteriorBlockEntity;
import dev.amble.ait.core.item.*;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.animation.ExteriorAnimation;
import dev.amble.ait.core.tardis.handler.travel.TravelHandler;
import dev.amble.ait.core.tardis.handler.travel.TravelHandlerBase;
import dev.amble.ait.data.schema.console.ConsoleTypeSchema;
import dev.amble.ait.module.ModuleRegistry;
import dev.amble.ait.module.gun.core.item.BaseGunItem;
import dev.amble.ait.registry.impl.SonicRegistry;
import dev.amble.ait.registry.impl.console.ConsoleRegistry;
import dev.amble.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import dev.amble.ait.registry.impl.door.ClientDoorRegistry;
import dev.amble.ait.registry.impl.exterior.ClientExteriorVariantRegistry;

@Environment(value = EnvType.CLIENT)
public class AITModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // TODO move to Registries
        ClientDoorRegistry.init();
        ClientTardisManager.init();

        ModuleRegistry.instance().onClientInit();

        setupBlockRendering();
        blockEntityRendererRegister();
        entityRenderRegister();
        chargedZeitonCrystalPredicate();
        waypointPredicate();
        hammerPredicate();
        siegeItemPredicate();
        adventItemPredicates();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ConfigCommand.register(dispatcher);
        });

        AITKeyBinds.init();

        ClientLandingManager.init();

        HudRenderCallback.EVENT.register(new SonicOverlay());
        HudRenderCallback.EVENT.register(new FabricatorOverlay());

        /*
         * ClientVortexDataHandler.init(); WorldRenderEvents.END.register(context -> {
         * MinecraftClient client = MinecraftClient.getInstance(); World world =
         * client.player.getWorld(); if(world.getRegistryKey() ==
         * AITDimensions.TIME_VORTEX_WORLD) { System.out.println("rendering");
         * VortexUtil vortex = new VortexUtil("space"); VortexData vortexData =
         * ClientVortexDataHandler.getCachedVortexData(WorldUtil.getName(client));
         *
         * if(vortexData != null) { for (VortexNode node : vortexData.nodes()) {
         * vortex.renderVortexNodes(context, node); } } } });
         */
        ClientPreAttackCallback.EVENT.register((client, player, clickCount) -> (player.getMainHandStack().getItem() instanceof BaseGunItem));

        WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
            Tardis tardis = ClientTardisUtil.getCurrentTardis();

            if (tardis == null)
                return;

            TardisStar.render(context, tardis);
        });

        ClientPlayNetworking.registerGlobalReceiver(OPEN_SCREEN, (client, handler, buf, responseSender) -> {
            int id = buf.readInt();
            Screen screen = screenFromId(id);

            if (screen == null)
                return;

            client.execute(() -> client.setScreenAndRender(screen));
        });

        ClientPlayNetworking.registerGlobalReceiver(OPEN_SCREEN_TARDIS, (client, handler, buf, responseSender) -> {
            int id = buf.readInt();
            UUID uuid = buf.readUuid();

            ClientTardisManager.getInstance().getTardis(uuid, tardis -> {
                Screen screen = screenFromId(id, tardis);

                if (screen == null)
                    return;

                client.execute(() -> client.setScreenAndRender(screen));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(OPEN_SCREEN_CONSOLE, (client, handler, buf, responseSender) -> {
            int id = buf.readInt();
            UUID uuid = buf.readUuid();
            BlockPos console = buf.readBlockPos();

            ClientTardisManager.getInstance().getTardis(uuid, tardis -> {
                Screen screen = screenFromId(id, tardis, console);

                if (screen == null)
                    return;

                client.execute(() -> client.setScreenAndRender(screen));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleGeneratorBlockEntity.SYNC_TYPE,
                (client, handler, buf, responseSender) -> {
                    if (client.world == null)
                        return;

                    String id = buf.readString();
                    ConsoleTypeSchema type = ConsoleRegistry.REGISTRY.get(Identifier.tryParse(id));
                    BlockPos consolePos = buf.readBlockPos();

                    if (client.world.getBlockEntity(consolePos) instanceof ConsoleGeneratorBlockEntity console)
                        console.setConsoleSchema(type.id());
                });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleGeneratorBlockEntity.SYNC_VARIANT,
                (client, handler, buf, responseSender) -> {
                    if (client.world == null)
                        return;

                    Identifier id = Identifier.tryParse(buf.readString());
                    BlockPos consolePos = buf.readBlockPos();

                    if (client.world.getBlockEntity(consolePos) instanceof ConsoleGeneratorBlockEntity console)
                        console.setVariant(id);
                });

        ClientPlayNetworking.registerGlobalReceiver(ExteriorAnimation.UPDATE,
                (client, handler, buf, responseSender) -> {
                    int p = buf.readInt();
                    UUID tardisId = buf.readUuid();

                    ClientTardisManager.getInstance().getTardis(client, tardisId, tardis -> {
                        if (tardis == null)
                            return;

                        // todo remember to use the right world in future !!
                        BlockEntity block = MinecraftClient.getInstance().world
                                .getBlockEntity(tardis.travel().position().getPos());

                        if (!(block instanceof ExteriorBlockEntity exterior))
                            return;

                        if (exterior.getAnimation() == null)
                            return;

                        exterior.getAnimation().setupAnimation(TravelHandlerBase.State.values()[p]);
                    });
                });

        // does all this clientplaynetwrokigng shite really have to go in here, theres
        // probably
        // somewhere else it can go
        // right??
        ClientPlayNetworking.registerGlobalReceiver(TravelHandler.CANCEL_DEMAT_SOUND, (client, handler, buf,
                responseSender) -> {
            ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

            if (tardis == null)
                return;

            client.getSoundManager().stopSounds(tardis.stats().getTravelEffects().get(TravelHandlerBase.State.DEMAT).soundId(), SoundCategory.BLOCKS);
        });

        WorldRenderEvents.END.register((context) -> SonicRendering.getInstance().renderWorld(context));
        HudRenderCallback.EVENT.register((context, delta) -> SonicRendering.getInstance().renderGui(context, delta));

        AmbleRegistries.getInstance().registerAll(SonicRegistry.getInstance(), ClientExteriorVariantRegistry.getInstance(), ClientConsoleVariantRegistry.getInstance());
        sonicModelPredicate();
    }

    /**
     * This is for screens without a tardis
     */
    public static Screen screenFromId(int id) {
        return screenFromId(id, null, null);
    }

    public static Screen screenFromId(int id, @Nullable ClientTardis tardis) {
        return screenFromId(id, tardis, null);
    }

    public static Screen screenFromId(int id, @Nullable ClientTardis tardis, @Nullable BlockPos console) {
        return switch (id) {
            case 0 -> new MonitorScreen(tardis, console);
            case 1 -> new BlueprintFabricatorScreen();
            default -> null;
        };
    }

    public void chargedZeitonCrystalPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.CHARGED_ZEITON_CRYSTAL, new Identifier("fuel"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (livingEntity == null)
                        return 0.0F;
                    if (itemStack.getItem() instanceof ChargedZeitonCrystalItem item) {
                        float value = (float) (item.getCurrentFuel(itemStack) / item.getMaxFuel(itemStack));
                        if (value > 0.0f && value < 0.5f) {
                            return 0.5f;
                        } else if (value > 0.5f && value < 1.0f) {
                            return 1.0f;
                        } else {
                            return 0.0f;
                        }
                    }

                    return 0.0F;
                });
    }

    public static void sonicModelPredicate() {
        SonicModels.init();
    }

    public static void waypointPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.WAYPOINT_CARTRIDGE, new Identifier("type"),
                (stack, clientWorld, livingEntity, integer) ->
                        stack.getOrCreateNbt().contains(WaypointItem.POS_KEY) ? 1 : 0);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex != 0)
                return -1;

            WaypointItem waypoint = (WaypointItem) stack.getItem();
            return waypoint.getColor(stack);
        }, AITItems.WAYPOINT_CARTRIDGE);
    }

    public static void hammerPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.HAMMER, new Identifier("toymakered"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (itemStack.getItem() instanceof HammerItem) {
                        if (itemStack.getName().getString().equalsIgnoreCase("Toymaker Hammer"))
                            return 1.0f;
                        else
                            return 0.0f;
                    }
                    return 0.0F;
                });
    }

    public static void siegeItemPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.HAMMER, new Identifier("bricked"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (itemStack.getOrCreateNbt().contains(SiegeTardisItem.CURRENT_TEXTURE_KEY)) {
                        return itemStack.getOrCreateNbt().getInt(SiegeTardisItem.CURRENT_TEXTURE_KEY);
                    }
                    return 0.0f;
                });
    }

    public static void adventItemPredicates() {
        ModelPredicateProviderRegistry.register(AITItems.HYPERCUBE, new Identifier("advent"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (itemStack.getItem() instanceof HypercubeItem) {
                        return isUnlockedOnThisDay(Calendar.JANUARY, 1) ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });

        ModelPredicateProviderRegistry.register(AITItems.HAZANDRA, new Identifier("advent"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (itemStack.getItem() instanceof InteriorTeleporterItem) {
                        return isUnlockedOnThisDay(Calendar.DECEMBER, 28) ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });

        ModelPredicateProviderRegistry.register(AITItems.IRON_KEY, new Identifier("advent"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (itemStack.getItem() instanceof KeyItem) {
                        return isUnlockedOnThisDay(Calendar.DECEMBER, 26) ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });

        ModelPredicateProviderRegistry.register(AITItems.GOLD_KEY, new Identifier("advent"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (itemStack.getItem() instanceof KeyItem) {
                        return isUnlockedOnThisDay(Calendar.DECEMBER, 26) ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });

        ModelPredicateProviderRegistry.register(AITItems.NETHERITE_KEY, new Identifier("advent"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (itemStack.getItem() instanceof KeyItem) {
                        return isUnlockedOnThisDay(Calendar.DECEMBER, 26) ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });

        ModelPredicateProviderRegistry.register(AITItems.CLASSIC_KEY, new Identifier("advent"),
                (itemStack, clientWorld, livingEntity, integer) -> {
                    if (itemStack.getItem() instanceof KeyItem) {
                        return isUnlockedOnThisDay(Calendar.DECEMBER, 26) ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });
    }

    public static void blockEntityRendererRegister() {
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CONSOLE_BLOCK_ENTITY_TYPE, ConsoleRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CONSOLE_GENERATOR_ENTITY_TYPE,
                ConsoleGeneratorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, ExteriorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, DoorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CORAL_BLOCK_ENTITY_TYPE, CoralRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.MONITOR_BLOCK_ENTITY_TYPE, MonitorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.ARTRON_COLLECTOR_BLOCK_ENTITY_TYPE,
                ArtronCollectorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.PLAQUE_BLOCK_ENTITY_TYPE, PlaqueRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.WALL_MONITOR_BLOCK_ENTITY_TYPE,
                WallMonitorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.ENGINE_BLOCK_ENTITY_TYPE, EngineRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.ENGINE_CORE_BLOCK_ENTITY_TYPE,
                EngineCoreBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.FABRICATOR_BLOCK_ENTITY_TYPE,
                FabricatorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.WAYPOINT_BANK_BLOCK_ENTITY_TYPE,
                WaypointBankBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.FLAG_BLOCK_ENTITY_TYPE, FlagBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.ZEITON_CAGE_BLOCK_ENTITY_TYPE,
                ZeitonCageRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.GENERIC_SUBSYSTEM_BLOCK_TYPE,
                GenericSubSystemRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.POWER_CONVERTER_BLOCK_TYPE,
                PowerConverterRenderer::new);
        if (isUnlockedOnThisDay(Calendar.DECEMBER, 30)) {
            BlockEntityRendererFactories.register(AITBlockEntityTypes.SNOW_GLOBE_BLOCK_ENTITY_TYPE,
                    SnowGlobeRenderer::new);
        }
    }

    public static void entityRenderRegister() {
        EntityRendererRegistry.register(AITEntityTypes.CONTROL_ENTITY_TYPE, ControlEntityRenderer::new);
        EntityRendererRegistry.register(AITEntityTypes.FALLING_TARDIS_TYPE, FallingTardisRenderer::new);
        EntityRendererRegistry.register(AITEntityTypes.FLIGHT_TARDIS_TYPE, FlightTardisRenderer::new);
        EntityRendererRegistry.register(AITEntityTypes.GALLIFREY_FALLS_PAINTING_TYPE, GallifreyFallsPaintingEntityRenderer::new);
        if (isUnlockedOnThisDay(Calendar.DECEMBER, 26)) {
            EntityRendererRegistry.register(AITEntityTypes.COBBLED_SNOWBALL_TYPE, FlyingItemEntityRenderer::new);
        }
    }

    public static void setupBlockRendering() {
        BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
        map.putBlock(AITBlocks.ZEITON_BLOCK, RenderLayer.getCutout());
        map.putBlock(AITBlocks.BUDDING_ZEITON, RenderLayer.getCutout());
        map.putBlock(AITBlocks.ENGINE_BLOCK, RenderLayer.getCutout());
        map.putBlock(AITBlocks.ZEITON_CLUSTER, RenderLayer.getCutout());
        map.putBlock(AITBlocks.LARGE_ZEITON_BUD, RenderLayer.getCutout());
        map.putBlock(AITBlocks.MEDIUM_ZEITON_BUD, RenderLayer.getCutout());
        map.putBlock(AITBlocks.SMALL_ZEITON_BUD, RenderLayer.getCutout());
        map.putBlock(AITBlocks.MACHINE_CASING, RenderLayer.getCutout());
        map.putBlock(AITBlocks.FABRICATOR, RenderLayer.getTranslucent());
        map.putBlock(AITBlocks.ENVIRONMENT_PROJECTOR, RenderLayer.getTranslucent());
        map.putBlock(AITBlocks.WAYPOINT_BANK, RenderLayer.getCutout());
        map.putBlock(AITBlocks.ENGINE_CORE_BLOCK, RenderLayer.getCutout());
        if (isUnlockedOnThisDay(Calendar.DECEMBER, 30)) {
            map.putBlock(AITBlocks.SNOW_GLOBE, RenderLayer.getCutout());
        }
    }
}
