package mdteam.ait;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import mdteam.ait.core.*;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ConsoleBlock;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.commands.SetLockedCommand;
import mdteam.ait.core.commands.SummonTardisCommand;
import mdteam.ait.core.commands.TeleportInteriorCommand;
import mdteam.ait.core.components.block.radio.RadioNBTComponent;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mdteam.ait.tardis.TardisManager;

import java.util.UUID;

public class AITMod implements ModInitializer {
    public static final String MOD_ID = "ait";
    public static final Logger LOGGER = LoggerFactory.getLogger("ait");
    public static final OwoItemGroup AIT_ITEM_GROUP = OwoItemGroup.builder(new Identifier(AITMod.MOD_ID, "item_group"),
            () -> Icon.of(AITItems.TARDIS_ITEM.getDefaultStack())).build();
    public static final ComponentKey<RadioNBTComponent> RADIONBT =
            ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "radionbt"), RadioNBTComponent.class);

    @Override
    public void onInitialize() {
        AITDesktops.init();
        FieldRegistrationHandler.register(AITItems.class, MOD_ID, false);
        FieldRegistrationHandler.register(AITBlocks.class, MOD_ID, false);
        FieldRegistrationHandler.register(AITSounds.class, MOD_ID, false);
        FieldRegistrationHandler.register(AITBlockEntityTypes.class, MOD_ID, false);
        FieldRegistrationHandler.register(AITEntityTypes.class, MOD_ID, false);
        AIT_ITEM_GROUP.initialize();
        PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, blockEntity) -> {
            if (!world.isClient()) {
                if (world.getRegistryKey().getRegistry() == AITDimensions.TARDIS_DIM_WORLD.getRegistry()) {
                    return !(world.getBlockEntity(pos) instanceof ConsoleBlockEntity);
                }
            }
            return !(state.getBlock() instanceof ConsoleBlock || state.getBlock() instanceof ExteriorBlock);
        }));
        TardisUtil.init();
        TardisManager.getInstance();
        TardisManager.init();

        entityAttributeRegister();

        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            TeleportInteriorCommand.register(dispatcher);
            SummonTardisCommand.register(dispatcher);
            SetLockedCommand.register(dispatcher);
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