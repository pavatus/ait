package mdteam.ait.client;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.registry.ClientConsoleVariantRegistry;
import mdteam.ait.client.registry.ClientDoorRegistry;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.renderers.AITRadioRenderer;
import mdteam.ait.client.renderers.consoles.ConsoleRenderer;
import mdteam.ait.client.renderers.coral.CoralRenderer;
import mdteam.ait.client.renderers.doors.DoorRenderer;
import mdteam.ait.client.renderers.entities.ControlEntityRenderer;
import mdteam.ait.client.renderers.entities.FallingTardisRenderer;
import mdteam.ait.client.renderers.entities.TardisRealRenderer;
import mdteam.ait.client.renderers.exteriors.ExteriorRenderer;
import mdteam.ait.client.screens.MonitorScreen;
import mdteam.ait.client.screens.OwOFindPlayerScreen;
import mdteam.ait.client.screens.interior.OwOInteriorSelectScreen;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.core.item.SonicItem;
import mdteam.ait.core.item.WaypointItem;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.console.ConsoleSchema;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.client.rendering.WorldRenderContextImpl;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.UUID;
import java.util.function.BiConsumer;

@Environment(value = EnvType.CLIENT)
public class AITModClient implements ClientModInitializer {

    private static KeyBinding keyBinding;

    private final Identifier PORTAL_EFFECT_SHADER = new Identifier(AITMod.MOD_ID, "shaders/core/portal_effect.json");
    public static final Identifier OPEN_SCREEN = new Identifier(AITMod.MOD_ID, "open_screen");
    public static final Identifier OPEN_SCREEN_TARDIS = new Identifier(AITMod.MOD_ID, "open_screen_tardis");

    @Override
    public void onInitializeClient() {
        setupBlockRendering();
        blockEntityRendererRegister();
        entityRenderRegister();
        sonicModelPredicate();
        riftScannerPredicate();
        waypointPredicate();
        setKeyBinding();

        ClientExteriorVariantRegistry.init();
        ClientConsoleVariantRegistry.init();
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


        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((block, world) -> {
            if (block instanceof ExteriorBlockEntity exterior) {
                if (exterior.getTardis() == null || exterior.getTardis().getDoor() == null) return;
                ClientTardisManager.getInstance().ask(exterior.getTardis().getUuid());
                if (!ClientTardisManager.getInstance().exteriorToTardis.containsKey(exterior)) {
                    ClientTardisManager.getInstance().exteriorToTardis.put(exterior, exterior.getTardis());
                }
                if (!ClientTardisManager.getInstance().loadedTardises.contains(exterior.getTardis().getUuid())) {
                    ClientTardisManager.getInstance().loadedTardises.add(exterior.getTardis().getUuid());
                }
                exterior.getTardis().getDoor().clearExteriorAnimationState();
            } else if (block instanceof DoorBlockEntity door) {
                if (door.getTardis() == null || door.getTardis().getDoor() == null) return;
                ClientTardisManager.getInstance().ask(door.getTardis().getUuid());
                door.getTardis().getDoor().clearInteriorAnimationState();
                if (!ClientTardisManager.getInstance().interiorDoorToTardis.containsKey(door)) {
                    ClientTardisManager.getInstance().interiorDoorToTardis.put(door, door.getTardis());
                }
                if (!ClientTardisManager.getInstance().loadedTardises.contains(door.getTardis().getUuid())) {
                    ClientTardisManager.getInstance().loadedTardises.add(door.getTardis().getUuid());
                }
            } else if (block instanceof ConsoleBlockEntity console) {
                if (console.getTardis() == null) return;
                if (!ClientTardisManager.getInstance().consoleToTardis.containsKey(console)) {
                    ClientTardisManager.getInstance().consoleToTardis.put(console, console.getTardis());
                }
                if (!ClientTardisManager.getInstance().loadedTardises.contains(console.getTardis().getUuid())) {
                    ClientTardisManager.getInstance().loadedTardises.add(console.getTardis().getUuid());
                }
                console.ask();
            }
        });

        ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((block, world) -> {
            if (block instanceof ConsoleBlockEntity console) {
                if (console.getTardis() == null) return;
                ClientTardisManager.getInstance().consoleToTardis.remove(console);
                if (!ClientTardisManager.getInstance().consoleToTardis.containsValue(console.getTardis())) {
                    if (ClientTardisManager.getInstance().exteriorToTardis.isEmpty()) {
                        if (ClientTardisManager.getInstance().interiorDoorToTardis.isEmpty()) {

                            ClientTardisManager.getInstance().loadedTardises.remove(console.getTardis().getUuid());
                            ClientTardisManager.getInstance().letKnowUnloaded(console.getTardis().getUuid());
                        }
                    }
                }
            }
            else if (block instanceof ExteriorBlockEntity exterior) {
                ClientTardisManager.getInstance().exteriorToTardis.remove(exterior);
                if (exterior.getTardis() == null) return;
                if (!ClientTardisManager.getInstance().exteriorToTardis.containsValue(exterior.getTardis())) {

                    ClientTardisManager.getInstance().loadedTardises.remove(exterior.getTardis().getUuid());
                    ClientTardisManager.getInstance().letKnowUnloaded(exterior.getTardis().getUuid());
                }
            }
            else if (block instanceof DoorBlockEntity door) {
                ClientTardisManager.getInstance().interiorDoorToTardis.remove(door);
                if (door.getTardis() == null) return;
                if (!ClientTardisManager.getInstance().interiorDoorToTardis.containsValue(door.getTardis())) {
                    if (ClientTardisManager.getInstance().consoleToTardis.isEmpty()) {
                        if (ClientTardisManager.getInstance().exteriorToTardis.isEmpty()) {
                            ClientTardisManager.getInstance().loadedTardises.remove(door.getTardis().getUuid());
                            ClientTardisManager.getInstance().letKnowUnloaded(door.getTardis().getUuid());
                        }
                    }
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleBlockEntity.SYNC_TYPE, (client, handler, buf, responseSender) -> {
            assert client.world != null;
            if (client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;

            String id = buf.readString();
            ConsoleSchema type = ConsoleRegistry.REGISTRY.get(Identifier.tryParse(id));
            BlockPos consolePos = buf.readBlockPos();
            if (client.world.getBlockEntity(consolePos) instanceof ConsoleBlockEntity console) console.setType(type);
        });

        ClientPlayNetworking.registerGlobalReceiver(ConsoleBlockEntity.SYNC_VARIANT, (client, handler, buf, responseSender) -> {
            if (client.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;

            Identifier id = Identifier.tryParse(buf.readString());
            BlockPos consolePos = buf.readBlockPos();
            if (client.world.getBlockEntity(consolePos) instanceof ConsoleBlockEntity console) console.setVariant(id);
        });

        ClientPlayNetworking.registerGlobalReceiver(ExteriorAnimation.UPDATE,
                (client, handler, buf, responseSender) -> {
                    int p = buf.readInt();
                    UUID tardisId = buf.readUuid();
                    ClientTardisManager.getInstance().getTardis(tardisId, (tardis -> {
                        if (tardis == null) return; // idk how the consumer works tbh, but im sure theo is gonna b happy

                       BlockEntity block = MinecraftClient.getInstance().world.getBlockEntity(tardis.getExterior().getExteriorPos()); // todo remember to use the right world in future !!
                       if (!(block instanceof ExteriorBlockEntity exterior)) return;

                       exterior.getAnimation().setupAnimation(TardisTravel.State.values()[p]);
                    }));
                    // this.setupAnimation(TardisTravel.State.values()[p]);
                }
        );

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
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
        return screenFromId(id, null);
    }

    public static Screen screenFromId(int id, UUID tardis) {
        return switch (id) {
            default -> null;
            case 0 -> new MonitorScreen(tardis); // todo new OwoMonitorScreen(tardis); god rest ye merry gentlemen
            case 1 -> new OwOFindPlayerScreen(tardis);
            case 2 -> new OwOInteriorSelectScreen(tardis, new MonitorScreen(tardis));
        };
    }


    // @TODO creativious this is the model predicate for the rift scanner, all you have to do is make the value being returned go from 0.0f to 0.75f in a circle to simulate a compass-like item.
    public void riftScannerPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.RIFT_SCANNER, new Identifier("scanner"),new RiftClampBullshit((world, stack, entity) -> GlobalPos.create(entity.getWorld().getRegistryKey(), BlockPos.fromLong(stack.getOrCreateNbt().getLong("targetBlock")))));
    }

    public void sonicModelPredicate() { // fixme lord give me strength - amen brother
        ModelPredicateProviderRegistry.register(AITItems.MECHANICAL_SONIC_SCREWDRIVER, new Identifier("inactive"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 0 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.MECHANICAL_SONIC_SCREWDRIVER, new Identifier("interaction"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 1 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.MECHANICAL_SONIC_SCREWDRIVER, new Identifier("overload"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 2 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.MECHANICAL_SONIC_SCREWDRIVER, new Identifier("scanning"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 3 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.MECHANICAL_SONIC_SCREWDRIVER, new Identifier("tardis"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 4 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, new Identifier("inactive"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 0 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, new Identifier("interaction"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 1 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, new Identifier("overload"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 2 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, new Identifier("scanning"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 3 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, new Identifier("tardis"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 4 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.CORAL_SONIC_SCREWDRIVER, new Identifier("inactive"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 0 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.CORAL_SONIC_SCREWDRIVER, new Identifier("interaction"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 1 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.CORAL_SONIC_SCREWDRIVER, new Identifier("overload"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 2 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.CORAL_SONIC_SCREWDRIVER, new Identifier("scanning"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 3 ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(AITItems.CORAL_SONIC_SCREWDRIVER, new Identifier("tardis"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            return SonicItem.findModeInt(itemStack) == 4 ? 1.0F : 0.0F;
        });
    }

    public void waypointPredicate() {
        ModelPredicateProviderRegistry.register(AITItems.WAYPOINT_CARTRIDGE, new Identifier("type"), (itemStack, clientWorld, livingEntity, integer) -> {
            if (livingEntity == null) return 0.0F;
            if(itemStack.getItem() == AITItems.WAYPOINT_CARTRIDGE)
                if(itemStack.getOrCreateNbt().contains(WaypointItem.POS_KEY))
                    return 1.0f;
                else return 0.5f;
            else return 0.5f;
        });
    }


    //@TODO Shader stuff, decide whether or not to use this or glScissor stuff. - Loqor
	/*public void shaderStuffForBOTI() {
		CoreShaderRegistrationCallback.EVENT.register(manager -> {
			manager.register(PORTAL_EFFECT_SHADER, VertexFormats.POSITION_TEXTURE, ShaderProgram::attachReferencedShaders);
		});
	}

	public ShaderProgram getShader() throws IOException {
		return new FabricShaderProgram(MinecraftClient.getInstance().getResourceManager(), PORTAL_EFFECT_SHADER, VertexFormats.POSITION_TEXTURE);
	}*/

    public void blockEntityRendererRegister() {
        BlockEntityRendererRegistry.register(AITBlockEntityTypes.AIT_RADIO_BLOCK_ENTITY_TYPE, AITRadioRenderer::new);
        BlockEntityRendererRegistry.register(AITBlockEntityTypes.DISPLAY_CONSOLE_BLOCK_ENTITY_TYPE, ConsoleRenderer::new);
        BlockEntityRendererRegistry.register(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, ExteriorRenderer::new);
        BlockEntityRendererRegistry.register(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, DoorRenderer::new);
        BlockEntityRendererRegistry.register(AITBlockEntityTypes.CORAL_BLOCK_ENTITY_TYPE, CoralRenderer::new);
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
                        ItemStack stack = KeyItem.getFirstKeyStackInInventory(player);
                        if (stack != null && stack.getItem() instanceof KeyItem key && key.hasProtocol(KeyItem.Protocols.SNAP)) {
                            NbtCompound tag = stack.getOrCreateNbt();
                            if (!tag.contains("tardis")) {
                                return;
                            }
                            ClientTardisUtil.snapToOpenDoors(UUID.fromString(tag.getString("tardis")));
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
        //map.putBlock(FMCBlocks.RADIO, RenderLayer.getCutout());
    }
}