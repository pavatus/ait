package loqor.ait.client;

import loqor.ait.AITMod;
import loqor.ait.client.renderers.machines.PlugBoardRenderer;
import loqor.ait.core.util.vortex.client.ClientVortexDataHandler;
import loqor.ait.core.util.vortex.server.ServerVortexDataHandler;
import loqor.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import loqor.ait.registry.impl.door.ClientDoorRegistry;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.client.renderers.CustomItemRendering;
import loqor.ait.client.renderers.consoles.ConsoleGeneratorRenderer;
import loqor.ait.client.renderers.consoles.ConsoleRenderer;
import loqor.ait.client.renderers.coral.CoralRenderer;
import loqor.ait.client.renderers.decoration.PlaqueRenderer;
import loqor.ait.client.renderers.doors.DoorRenderer;
import loqor.ait.client.renderers.entities.ControlEntityRenderer;
import loqor.ait.client.renderers.entities.FallingTardisRenderer;
import loqor.ait.client.renderers.entities.TardisRealRenderer;
import loqor.ait.client.renderers.exteriors.ExteriorRenderer;
import loqor.ait.client.renderers.machines.ArtronCollectorRenderer;
import loqor.ait.client.renderers.machines.EngineCoreBlockEntityRenderer;
import loqor.ait.client.renderers.machines.EngineRenderer;
import loqor.ait.client.renderers.monitors.MonitorRenderer;
import loqor.ait.client.renderers.monitors.WallMonitorRenderer;
import loqor.ait.client.renderers.wearables.AITHudOverlay;
import loqor.ait.client.screens.EngineScreen;
import loqor.ait.client.screens.MonitorScreen;
import loqor.ait.client.screens.interior.OwOInteriorSelectScreen;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.*;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.RiftTarget;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.core.item.*;
import loqor.ait.core.data.schema.SonicSchema;
import loqor.ait.registry.Registries;
import loqor.ait.registry.impl.SonicRegistry;
import loqor.ait.registry.impl.console.ConsoleRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.link.LinkableBlockEntity;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static loqor.ait.AITMod.*;

@Environment(value = EnvType.CLIENT)
public class AITModClient implements ClientModInitializer {
    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        Registries.getInstance().subscribe(Registries.InitType.CLIENT);

        ClientVortexDataHandler.init();
        ClientVortexDataHandler.subscribe();

        setupBlockRendering();
        sonicModelPredicate();
        blockEntityRendererRegister();
        entityRenderRegister();
        riftScannerPredicate();
        chargedZeitonCrystalPredicate();
        waypointPredicate();
        hammerPredicate();
        setKeyBinding();

        HandledScreens.register(ENGINE_SCREEN_HANDLER, EngineScreen::new);

        HudRenderCallback.EVENT.register(new AITHudOverlay());

        ClientExteriorVariantRegistry.getInstance().init();
        ClientConsoleVariantRegistry.getInstance().init();
        ClientDoorRegistry.init();

        ClientPlayNetworking.registerGlobalReceiver(OPEN_SCREEN,
                (client, handler, buf, responseSender) -> {
                    int id = buf.readInt();

                    Screen screen = screenFromId(id);
                    if (screen == null) return;
                    MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().setScreenAndRender(screen));
                });
        ClientPlayNetworking.registerGlobalReceiver(OPEN_SCREEN_TARDIS, // fixme this might not be necessary could just be easier to always use the other method.
                (client, handler, buf, responseSender) -> {
                    int id = buf.readInt();
                    UUID uuid = buf.readUuid();

                    Screen screen = screenFromId(id, uuid);
                    if (screen == null) return;
                    MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().setScreenAndRender(screen));
        });
        ClientPlayNetworking.registerGlobalReceiver(OPEN_SCREEN_CONSOLE, (client, handler, buf, responseSender) -> {
            int id = buf.readInt();
            UUID tardis = buf.readUuid();
            UUID console = buf.readUuid();

            Screen screen = screenFromId(id, tardis, console);
            if (screen == null) return;

            MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().setScreenAndRender(screen));
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleBlockEntity.SYNC_TYPE, (client, handler, buf, responseSender) -> {
            if (client.world == null || client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;

            String id = buf.readString();
            ConsoleTypeSchema type = ConsoleRegistry.REGISTRY.get(Identifier.tryParse(id));
            BlockPos consolePos = buf.readBlockPos();
            if (client.world.getBlockEntity(consolePos) instanceof ConsoleBlockEntity console) console.setType(type);
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleBlockEntity.SYNC_VARIANT, (client, handler, buf, responseSender) -> {
            if (client.world == null || client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;
            Identifier id = Identifier.tryParse(buf.readString());
            BlockPos consolePos = buf.readBlockPos();
            if (client.world.getBlockEntity(consolePos) instanceof ConsoleBlockEntity console) console.setVariant(id);
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleBlockEntity.SYNC_PARENT, (client, handler, buf, responseSender) -> {
            if (client.world == null || client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;
            UUID uuid = buf.readUuid();
            BlockPos consolePos = buf.readBlockPos();
            if (client.world.getBlockEntity(consolePos) instanceof ConsoleBlockEntity console) console.setParent(uuid);
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleGeneratorBlockEntity.SYNC_TYPE, (client, handler, buf, responseSender) -> {
            if (client.world == null || client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;

            String id = buf.readString();
            ConsoleTypeSchema type = ConsoleRegistry.REGISTRY.get(Identifier.tryParse(id));
            BlockPos consolePos = buf.readBlockPos();
            if (client.world.getBlockEntity(consolePos) instanceof ConsoleGeneratorBlockEntity console) console.setConsoleSchema(type.id());
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleGeneratorBlockEntity.SYNC_VARIANT, (client, handler, buf, responseSender) -> {
            if (client.world == null || client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;
            Identifier id = Identifier.tryParse(buf.readString());
            BlockPos consolePos = buf.readBlockPos();
            if (client.world.getBlockEntity(consolePos) instanceof ConsoleGeneratorBlockEntity console) console.setVariant(id);
        });

        ClientPlayNetworking.registerGlobalReceiver(ExteriorAnimation.UPDATE,
                (client, handler, buf, responseSender) -> {
                    int p = buf.readInt();
                    UUID tardisId = buf.readUuid();
                    ClientTardisManager.getInstance().getTardis(tardisId, (tardis -> {
                        if (tardis == null) return; // idk how the consumer works tbh, but im sure theo is gonna b happy

                       BlockEntity block = MinecraftClient.getInstance().world.getBlockEntity(tardis.getExterior().getExteriorPos()); // todo remember to use the right world in future !!
                       if (!(block instanceof ExteriorBlockEntity exterior)) return;
                       if (exterior.getAnimation() == null) return;

                       exterior.getAnimation().setupAnimation(TardisTravel.State.values()[p]);
                    }));
                }
        );

        // does all this clientplaynetwrokigng shite really have to go in here, theres probably somewhere else it can go right??
        ClientPlayNetworking.registerGlobalReceiver(AITMessages.CANCEL_DEMAT_SOUND, (client, handler, buf, responseSender) -> {
            client.getSoundManager().stopSounds(AITSounds.DEMAT.getId(), SoundCategory.BLOCKS);
        });

        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((block, world) -> {
            if (block instanceof LinkableBlockEntity) {
                if (((LinkableBlockEntity) block).findTardis().isEmpty()) return;
                ClientTardisManager.getInstance().loadTardis(((LinkableBlockEntity) block).findTardis().get().getUuid(), (t) -> {});
            }
        });
    }

    public static void openScreen(ServerPlayerEntity player, int id) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        ServerPlayNetworking.send(player, OPEN_SCREEN, buf);
    }

    /**
     * This is for screens without a tardis
     */
    public static Screen screenFromId(int id) {
        return screenFromId(id, null, null);
    }

    public static Screen screenFromId(int id, @Nullable UUID tardis) {
        return screenFromId(id, tardis, null);
    }
    public static Screen screenFromId(int id, @Nullable UUID tardis, @Nullable UUID console) {
        return switch (id) {
            default -> null;
            case 0 -> new MonitorScreen(tardis, console);
            //case 1 -> new EngineScreen(tardis);
            case 2 -> new OwOInteriorSelectScreen(tardis, new MonitorScreen(tardis, console));
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

    public void waypointPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.WAYPOINT_CARTRIDGE, new Identifier("type"), (itemStack, clientWorld, livingEntity, integer) -> {
            if(itemStack.getItem() == AITItems.WAYPOINT_CARTRIDGE)
                if(itemStack.getOrCreateNbt().contains(WaypointItem.POS_KEY))
                    return 1.0f;
                else return 0.5f;
            else return 0.5f;
        });
    }

    public void hammerPredicate() {
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

    public void blockEntityRendererRegister() {
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
    }

    public void entityRenderRegister() {
        EntityRendererRegistry.register(AITEntityTypes.CONTROL_ENTITY_TYPE, ControlEntityRenderer::new);
        EntityRendererRegistry.register(AITEntityTypes.FALLING_TARDIS_TYPE, FallingTardisRenderer::new);
        EntityRendererRegistry.register(AITEntityTypes.TARDIS_REAL_ENTITY_TYPE, TardisRealRenderer::new);
    }

    private boolean keyHeldDown = false;

    public void setKeyBinding() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + AITMod.MOD_ID + ".open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category." + AITMod.MOD_ID + ".snap"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;

            if (player == null)
                return;

            if (!keyBinding.isPressed()) {
                keyHeldDown = false;
                return;
            }

            if (!keyHeldDown) {
                keyHeldDown = true;

                if (player.getVehicle() instanceof TardisRealEntity entity) {
                    ClientTardisUtil.snapToOpenDoors(entity.getTardisID());
                    return;
                }

                Collection<ItemStack> keys = KeyItem.getKeysInInventory(player);

                for (ItemStack stack : keys) {
                    if (stack.getItem() instanceof KeyItem key && key.hasProtocol(KeyItem.Protocols.SNAP)) {
                        Tardis tardis = KeyItem.getTardis(stack);
                        Loyalty loyalty = tardis.getHandlers().getLoyalties().get(player);

                        //TODO: make a permissionhandler - yayy we have one :)
                        if (loyalty.level() > Loyalty.Type.PILOT.level)
                            ClientTardisUtil.snapToOpenDoors(tardis);
                    }
                }
            }
        });
    }

    public void setupBlockRendering() {
        BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
        map.putBlock(AITBlocks.ZEITON_BLOCK, RenderLayer.getCutout());
        map.putBlock(AITBlocks.BUDDING_ZEITON, RenderLayer.getCutout());
        map.putBlock(AITBlocks.ZEITON_CLUSTER, RenderLayer.getCutout());
        map.putBlock(AITBlocks.LARGE_ZEITON_BUD, RenderLayer.getCutout());
        map.putBlock(AITBlocks.MEDIUM_ZEITON_BUD, RenderLayer.getCutout());
        map.putBlock(AITBlocks.SMALL_ZEITON_BUD, RenderLayer.getCutout());
        map.putBlock(AITBlocks.MACHINE_CASING, RenderLayer.getTranslucent());
    }
}