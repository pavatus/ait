package mdteam.ait.tardis.control.impl;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.LinkableItem;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.link.Linkable;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class TelepathicControl extends Control {
    public TelepathicControl() {
        super("telepathic_circuit");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        if (player.getMainHandStack().getItem() instanceof LinkableItem linker) {
            linker.link(player.getMainHandStack(), tardis);
            world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        }

        BlockPos destinationPos = tardis.getTravel().getDestination();

        ServerWorld newWorld = (ServerWorld) tardis.getTravel().getDestination().getWorld();

        RegistryEntry<Biome> biome = newWorld.getBiome(destinationPos);

        String biomeTranslationKey = biome.getKeyOrValue().orThrow().getValue().toShortTranslationKey();
        Text text = Text.translatable("tardis.message.destination_biome").append(Text.literal(DimensionControl.capitalizeAndReplaceEach(biomeTranslationKey)));
        player.sendMessage(text, true);

        return true;
    }

    @Override
    public boolean shouldFailOnNoPower() {
        return false;
    }
}
