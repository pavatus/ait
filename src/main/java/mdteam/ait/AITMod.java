package mdteam.ait;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import mdteam.ait.api.ICantBreak;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.*;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ConsoleBlock;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.commands.*;
import mdteam.ait.core.components.block.radio.RadioNBTComponent;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.core.managers.RiftChunkManager;
import mdteam.ait.core.util.AITConfig;
import mdteam.ait.datagen.datagen_providers.AITLanguageProvider;
import mdteam.ait.registry.*;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mdteam.ait.tardis.TardisManager;

import java.util.List;
import java.util.UUID;

public class AITMod implements ModInitializer {
    public static final String MOD_ID = "ait";
    public static final Logger LOGGER = LoggerFactory.getLogger("ait");

    public static final AITConfig AIT_CONFIG = AITConfig.createAndLoad();

    public static final OwoItemGroup AIT_ITEM_GROUP = OwoItemGroup.builder(new Identifier(AITMod.MOD_ID, "item_group"),
            () -> Icon.of(AITItems.TARDIS_ITEM.getDefaultStack())).build();
    public static final ComponentKey<RadioNBTComponent> RADIONBT =
            ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "radionbt"), RadioNBTComponent.class);

    @Override
    public void onInitialize() {
        DoorRegistry.init();
        ConsoleRegistry.init();
        ConsoleVariantRegistry.init();
        DesktopRegistry.init();
        ExteriorRegistry.init();
        ExteriorVariantRegistry.init();

        FieldRegistrationHandler.register(AITItems.class, MOD_ID, false);
        FieldRegistrationHandler.register(AITBlocks.class, MOD_ID, false);
        FieldRegistrationHandler.register(AITSounds.class, MOD_ID, false);
        FieldRegistrationHandler.register(AITBlockEntityTypes.class, MOD_ID, false);
        FieldRegistrationHandler.register(AITEntityTypes.class, MOD_ID, false);
        AIT_ITEM_GROUP.initialize();

        TardisUtil.init();
        TardisManager.getInstance();
        TardisManager.init();
        RiftChunkManager.init();

        entityAttributeRegister();

        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            TeleportInteriorCommand.register(dispatcher);
            SummonTardisCommand.register(dispatcher);
            SetLockedCommand.register(dispatcher);
            SetHumCommand.register(dispatcher);
            ToggleHumCommand.register(dispatcher);
            ToggleAlarmCommand.register(dispatcher);
            RiftChunkCommand.register(dispatcher);
        }));

        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register(((blockEntity, world) -> {
            // fixme this doesnt seem to run??
            if (blockEntity instanceof ConsoleBlockEntity console) {
                console.markNeedsSyncing();
            }
        }));

        TardisEvents.LANDED.register((tardis -> {
            // stuff for resetting the ExteriorAnimation
            if (tardis.getTravel().getPosition().getWorld().getBlockEntity(tardis.getTravel().getExteriorPos()) instanceof ExteriorBlockEntity entity) {
                entity.getAnimation().setupAnimation(tardis.getTravel().getState());
            };
        }));

        ServerPlayNetworking.registerGlobalReceiver(ConsoleBlockEntity.ASK, ((server, player, handler, buf, responseSender) -> {
            if (player.getServerWorld().getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;

            BlockPos consolePos = buf.readBlockPos();
            // fixme the gotten block entity is always null, shit.
            if (player.getServerWorld().getBlockEntity(consolePos) instanceof ConsoleBlockEntity console) console.markNeedsSyncing();
        }));
    }

    public void entityAttributeRegister() {
        FabricDefaultAttributeRegistry.register(AITEntityTypes.CONTROL_ENTITY_TYPE, ConsoleControlEntity.createControlAttributes());
    }

    public static final Identifier OPEN_SCREEN_TARDIS = new Identifier(AITMod.MOD_ID, "open_screen_tardis"); // fixes "AITModClient in env type SERVER"

    public static void openScreen(ServerPlayerEntity player, int id, UUID tardis) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeUuid(tardis);
        ServerPlayNetworking.send(player, OPEN_SCREEN_TARDIS, buf);
    }
}