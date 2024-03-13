package mdteam.ait.client;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.VortexUtil;
import mdteam.ait.client.renderers.consoles.ConsoleGeneratorRenderer;
import mdteam.ait.client.renderers.decoration.PlaqueRenderer;
import mdteam.ait.client.renderers.machines.ArtronCollectorRenderer;
import mdteam.ait.client.renderers.monitors.MonitorRenderer;
import mdteam.ait.client.renderers.wearables.AITHudOverlay;
import mdteam.ait.core.*;
import mdteam.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import mdteam.ait.core.entities.TardisRealEntity;
import mdteam.ait.core.item.*;
import mdteam.ait.registry.*;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.client.registry.ClientConsoleVariantRegistry;
import mdteam.ait.client.registry.ClientDoorRegistry;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.renderers.consoles.ConsoleRenderer;
import mdteam.ait.client.renderers.coral.CoralRenderer;
import mdteam.ait.client.renderers.doors.DoorRenderer;
import mdteam.ait.client.renderers.entities.ControlEntityRenderer;
import mdteam.ait.client.renderers.entities.FallingTardisRenderer;
import mdteam.ait.client.renderers.entities.TardisRealRenderer;
import mdteam.ait.client.renderers.exteriors.ExteriorRenderer;
import mdteam.ait.client.screens.MonitorScreen;
import mdteam.ait.client.screens.interior.OwOInteriorSelectScreen;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.console.type.ConsoleTypeSchema;
import mdteam.ait.tardis.link.LinkableBlockEntity;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
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
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

import static mdteam.ait.AITMod.*;

@Environment(value = EnvType.CLIENT)
public class AITModClient implements ClientModInitializer {
    private static KeyBinding keyBinding;
    private final VortexUtil vortex = new VortexUtil("space");

    @Override
    public void onInitializeClient() {
        setupBlockRendering();
        blockEntityRendererRegister();
        entityRenderRegister();
        sonicModelPredicate();
        riftScannerPredicate();
        chargedZeitonCrystalPredicate();
        waypointPredicate();
        setKeyBinding();

        HudRenderCallback.EVENT.register(new AITHudOverlay());

        ClientExteriorVariantRegistry.getInstance().init();
        ClientConsoleVariantRegistry.getInstance().init();
        ClientDoorRegistry.init();

        WorldRenderEvents.END.register(context -> {
            try (ClientWorld world = context.world()){
                if (world.getRegistryKey() == AITDimensions.TIME_VORTEX_WORLD) {
                    vortex.renderVortex(context);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

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

        ClientPlayNetworking.registerGlobalReceiver(DesktopRegistry.SYNC_TO_CLIENT, (client, handler, buf, responseSender) -> {
            DesktopRegistry.getInstance().readFromServer(buf);
        });

        ClientPlayNetworking.registerGlobalReceiver(ExteriorVariantRegistry.SYNC_TO_CLIENT, (client, handler, buf, responseSender) -> {
            PacketByteBuf copy = PacketByteBufs.copy(buf);

            ClientExteriorVariantRegistry.getInstance().readFromServer(buf);
            ExteriorVariantRegistry.getInstance().readFromServer(copy);
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleVariantRegistry.SYNC_TO_CLIENT, (client, handler, buf, responseSender) -> {
            PacketByteBuf copy = PacketByteBufs.copy(buf);

            ClientConsoleVariantRegistry.getInstance().readFromServer(buf);
            ConsoleVariantRegistry.getInstance().readFromServer(copy);
        });

        ClientPlayNetworking.registerGlobalReceiver(CategoryRegistry.SYNC_TO_CLIENT, (client, handler, buf, responseSender) -> {
           CategoryRegistry.getInstance().readFromServer(buf);
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
            case 1 -> null;
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

    public void sonicModelPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.SONIC_SCREWDRIVER, new Identifier("inactive"), (itemStack, clientWorld, livingEntity, integer) -> SonicItem.findModeInt(itemStack) == 0 ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(AITItems.SONIC_SCREWDRIVER, new Identifier("sonic_type"), (itemStack, clientWorld, livingEntity, integer) -> SonicItem.findTypeInt(itemStack) / 5f);
        ModelPredicateProviderRegistry.register(AITItems.SONIC_SCREWDRIVER, new Identifier("interaction"), (itemStack, clientWorld, livingEntity, integer) -> SonicItem.findModeInt(itemStack) == 1 ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(AITItems.SONIC_SCREWDRIVER, new Identifier("overload"), (itemStack, clientWorld, livingEntity, integer) -> SonicItem.findModeInt(itemStack) == 2 ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(AITItems.SONIC_SCREWDRIVER, new Identifier("scanning"), (itemStack, clientWorld, livingEntity, integer) -> SonicItem.findModeInt(itemStack) == 3 ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(AITItems.SONIC_SCREWDRIVER, new Identifier("tardis"), (itemStack, clientWorld, livingEntity, integer) -> SonicItem.findModeInt(itemStack) == 4 ? 1.0F : 0.0F);
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

    public void blockEntityRendererRegister() {
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CONSOLE_BLOCK_ENTITY_TYPE, ConsoleRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CONSOLE_GENERATOR_ENTITY_TYPE, ConsoleGeneratorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, ExteriorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, DoorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.CORAL_BLOCK_ENTITY_TYPE, CoralRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.MONITOR_BLOCK_ENTITY_TYPE, MonitorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.ARTRON_COLLECTOR_BLOCK_ENTITY_TYPE, ArtronCollectorRenderer::new);
        BlockEntityRendererFactories.register(AITBlockEntityTypes.PLAQUE_BLOCK_ENTITY_TYPE, PlaqueRenderer::new);
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
            if (player != null) {
                if (keyBinding.isPressed()) {
                    if (!keyHeldDown) {
                        keyHeldDown = true;
                        if (player.getVehicle() instanceof TardisRealEntity entity) {
                            ClientTardisUtil.snapToOpenDoors(entity.getTardisID());
                            return;
                        }
                        ItemStack[] keys = KeyItem.getKeysInInventory(player);
                        for (ItemStack stack : keys) {
                            if (stack != null && stack.getItem() instanceof KeyItem key && key.hasProtocol(KeyItem.Protocols.SNAP)) {
                                NbtCompound tag = stack.getOrCreateNbt();
                                if (!tag.contains("tardis")) {
                                    return;
                                }
                                ClientTardisUtil.snapToOpenDoors(UUID.fromString(tag.getString("tardis")));
                            }
                        }
                    }
                } else {
                    keyHeldDown = false;
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
    }
}