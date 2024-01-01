package mdteam.ait.tardis.control.impl;

import mdteam.ait.AITMod;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Objects;

public class TelepathicControl extends Control {
    public TelepathicControl() {
        super("telepathic_circuit");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        if(PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE) &&
                !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.IS_FALLING)) {
            if (player.getMainHandStack().getItem() instanceof KeyItem) {
            /*ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            List<PlayerEntity> playerList = TardisUtil.findPlayerByTardisKey(world, tardis);
            if (!tag.contains("tardis") || playerList == null) {
                return false;
            }*/
                AITMod.openScreen(player, 1, tardis.getUuid());
            /*if (Objects.equals(tardis.getUuid().toString(), tag.getString("tardis"))) {
                AITMod.openScreen(player, 1, tardis.getUuid());
            } else {
                if(tardis.getDesktop().getConsolePos() != null)
                    world.playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }*/
                return true;
            }
        }

        BlockPos destinationPos = tardis.getTravel().getDestination();

        ServerWorld newWorld = (ServerWorld) tardis.getTravel().getDestination().getWorld();

        RegistryEntry<Biome> biome = newWorld.getBiome(destinationPos);

        String biomeTranslationKey = biome.getKeyOrValue().orThrow().getValue().toShortTranslationKey();
        Text text = Text.translatable("tardis.message.destination_biome").append(Text.literal(DimensionControl.capitalizeAndReplaceEach(biomeTranslationKey)));
        player.sendMessage(text, true);

        return true;
    }
}
