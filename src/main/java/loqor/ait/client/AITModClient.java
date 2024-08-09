package loqor.ait.client;

import loqor.ait.client.renderers.CustomItemRendering;
import loqor.ait.client.renderers.TardisStar;
import loqor.ait.client.renderers.consoles.ConsoleGeneratorRenderer;
import loqor.ait.client.renderers.consoles.ConsoleRenderer;
import loqor.ait.client.renderers.coral.CoralRenderer;
import loqor.ait.client.renderers.decoration.PlaqueRenderer;
import loqor.ait.client.renderers.doors.DoorRenderer;
import loqor.ait.client.renderers.entities.ControlEntityRenderer;
import loqor.ait.client.renderers.entities.FallingTardisRenderer;
import loqor.ait.client.renderers.exteriors.ExteriorRenderer;
import loqor.ait.client.renderers.machines.*;
import loqor.ait.client.renderers.monitors.MonitorRenderer;
import loqor.ait.client.renderers.monitors.WallMonitorRenderer;
import loqor.ait.client.renderers.wearables.AITHudOverlay;
import loqor.ait.client.screens.EngineScreen;
import loqor.ait.client.screens.MonitorScreen;
import loqor.ait.client.screens.interior.OwOInteriorSelectScreen;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.client.util.SkyboxUtil;
import loqor.ait.core.*;
import loqor.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.RiftTarget;
import loqor.ait.core.data.schema.SonicSchema;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.core.item.*;
import loqor.ait.registry.Registries;
import loqor.ait.registry.impl.SonicRegistry;
import loqor.ait.registry.impl.console.ConsoleRegistry;
import loqor.ait.registry.impl.door.ClientDoorRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.link.LinkableBlockEntity;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static loqor.ait.AITMod.*;

@Environment(value = EnvType.CLIENT)
public class AITModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Registries.getInstance().subscribe(Registries.InitType.CLIENT);

        // TODO move to Registries
        ClientDoorRegistry.init();
        ClientTardisManager.init();

        setupBlockRendering();
        sonicModelPredicate();
        blockEntityRendererRegister();
        entityRenderRegister();
        riftScannerPredicate();
        chargedZeitonCrystalPredicate();
        waypointPredicate();
        hammerPredicate();
        siegeItemPredicate();

        DimensionRenderingRegistry.registerSkyRenderer(AITDimensions.TARDIS_DIM_WORLD, SkyboxUtil::renderTardisSky);

        AITKeyBinds.init();

        HandledScreens.register(ENGINE_SCREEN_HANDLER, EngineScreen::new);
        HudRenderCallback.EVENT.register(new AITHudOverlay());

        /*
        ClientVortexDataHandler.init();
        WorldRenderEvents.END.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            World world = client.player.getWorld();
            if(world.getRegistryKey() == AITDimensions.TIME_VORTEX_WORLD) {
                System.out.println("rendering");
                VortexUtil vortex = new VortexUtil("space");
                VortexData vortexData = ClientVortexDataHandler.getCachedVortexData(WorldUtil.getName(client));

                if(vortexData != null) {
                    for (VortexNode node : vortexData.nodes()) {
                        vortex.renderVortexNodes(context, node);
                    }
                }
            }
        });
        */

        WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
            if (!ClientTardisUtil.isPlayerInATardis())
                return;

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

        ClientPlayNetworking.registerGlobalReceiver(ConsoleGeneratorBlockEntity.SYNC_TYPE, (client, handler, buf, responseSender) -> {
            if (client.world == null || client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
                return;

            String id = buf.readString();
            ConsoleTypeSchema type = ConsoleRegistry.REGISTRY.get(Identifier.tryParse(id));
            BlockPos consolePos = buf.readBlockPos();

            if (client.world.getBlockEntity(consolePos) instanceof ConsoleGeneratorBlockEntity console)
                console.setConsoleSchema(type.id());
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleGeneratorBlockEntity.SYNC_VARIANT, (client, handler, buf, responseSender) -> {
            if (client.world == null || client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
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
                        BlockEntity block = MinecraftClient.getInstance().world.getBlockEntity(tardis.travel().position().getPos());

                        if (!(block instanceof ExteriorBlockEntity exterior))
                            return;

                        if (exterior.getAnimation() == null)
                            return;

                        exterior.getAnimation().setupAnimation(TravelHandlerBase.State.values()[p]);
                    });
                }
        );

        // does all this clientplaynetwrokigng shite really have to go in here, theres probably somewhere else it can go right??
        ClientPlayNetworking.registerGlobalReceiver(TravelHandler.CANCEL_DEMAT_SOUND, (client, handler, buf, responseSender) ->
                client.getSoundManager().stopSounds(AITSounds.DEMAT.getId(), SoundCategory.BLOCKS));

        // FIXME well this seems pointless
        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((block, world) -> {
            if (block instanceof LinkableBlockEntity) {
                if (((LinkableBlockEntity) block).findTardis().isEmpty())
                    return;

                ClientTardisManager.getInstance().loadTardis(((LinkableBlockEntity) block).findTardis().get().getUuid(), null);
            }
        });
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
            default -> null;
            case 0 -> new MonitorScreen(tardis, console);
            //case 1 -> new EngineScreen(tardis);
            case 2 -> new OwOInteriorSelectScreen(tardis.getUuid(), new MonitorScreen(tardis, console));
        };
    }

    public void riftScannerPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.RIFT_SCANNER, new Identifier("scanner"),new RiftTarget((world, stack, entity) -> GlobalPos.create(entity.getWorld().getRegistryKey(), RiftScannerItem.getTarget(stack).getCenterAtY(75))));
    }

    public void chargedZeitonCrystalPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.CHARGED_ZEITON_CRYSTAL, new Identifier("fuel"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            if(itemStack.getItem() instanceof ChargedZeitonCrystalItem item) {
                float value = (float) (item.getCurrentFuel(itemStack) / item.getMaxFuel(itemStack));
                if(value > 0.0f && value < 0.5f) {
                    return 0.5f;
                } else if(value > 0.5f && value < 1.0f) {
                    return 1.0f;
                } else {
                    return 0.0f;
                }
            }

            return 0.0F;
        });
    }

    public static void sonicModelPredicate() {
        SonicRegistry.getInstance().populateModels(CustomItemRendering::load);

        CustomItemRendering.register(new Identifier(MOD_ID, "sonic_screwdriver"), (model, stack, world, entity, seed) -> {
            SonicItem.Mode mode = SonicItem.findMode(stack);
            SonicSchema.Models models = SonicItem.findSchema(stack).models();

            return switch (mode) {
                case INACTIVE -> models.inactive();
                case INTERACTION -> models.interaction();
                case OVERLOAD -> models.overload();
                case SCANNING -> models.scanning();
                case TARDIS -> models.tardis();
            };
        });
    }

    public static void waypointPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.WAYPOINT_CARTRIDGE, new Identifier("type"), (itemStack, clientWorld, livingEntity, integer) -> {
            if(itemStack.getOrCreateNbt().contains(WaypointItem.POS_KEY))
                return 1.0f;
            else return 0f;
        });

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex != 0)
                return -1;

            WaypointItem waypoint = (WaypointItem) stack.getItem();
            return waypoint.getColor(stack);
        }, AITItems.WAYPOINT_CARTRIDGE);
    }

    public static void hammerPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.HAMMER, new Identifier("toymakered"), (itemStack, clientWorld, livingEntity, integer) -> {
            if(itemStack.getItem() instanceof HammerItem) {
                if(itemStack.getName().getString().equalsIgnoreCase("Toymaker Hammer"))
                    return 1.0f;
                else
                    return 0.0f;
            }
            return 0.0F;
        });
    }

    public static void siegeItemPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.HAMMER, new Identifier("bricked"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (itemStack.getOrCreateNbt().contains(SiegeTardisItem.CURRENT_TEXTURE_KEY)) {
                return itemStack.getOrCreateNbt().getInt(SiegeTardisItem.CURRENT_TEXTURE_KEY);
            }
            return 0.0f;
        });
    }

    public static void blockEntityRendererRegister() {
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CONSOLE_BLOCK_ENTITY_TYPE, ConsoleRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CONSOLE_GENERATOR_ENTITY_TYPE, ConsoleGeneratorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, ExteriorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, DoorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CORAL_BLOCK_ENTITY_TYPE, CoralRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.MONITOR_BLOCK_ENTITY_TYPE, MonitorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.ARTRON_COLLECTOR_BLOCK_ENTITY_TYPE, ArtronCollectorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.PLAQUE_BLOCK_ENTITY_TYPE, PlaqueRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.WALL_MONITOR_BLOCK_ENTITY_TYPE, WallMonitorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.ENGINE_BLOCK_ENTITY_TYPE, EngineRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.ENGINE_CORE_BLOCK_ENTITY_TYPE, EngineCoreBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.PLUGBOARD_ENTITY_TYPE, PlugBoardRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.FABRICATOR_BLOCK_ENTITY_TYPE, FabricatorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.WAYPOINT_BANK_BLOCK_ENTITY_TYPE, WaypointBankBlockEntityRenderer::new);
    }

    public static void entityRenderRegister() {
        EntityRendererRegistry.register(AITEntityTypes.CONTROL_ENTITY_TYPE, ControlEntityRenderer::new);
        EntityRendererRegistry.register(AITEntityTypes.FALLING_TARDIS_TYPE, FallingTardisRenderer::new);
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
        map.putBlock(AITBlocks.MACHINE_CASING, RenderLayer.getTranslucent());
        map.putBlock(AITBlocks.FABRICATOR, RenderLayer.getTranslucent());
        map.putBlock(AITBlocks.ENVIRONMENT_PROJECTOR, RenderLayer.getTranslucent());
        map.putBlock(AITBlocks.WAYPOINT_BANK, RenderLayer.getCutout());
    }
}