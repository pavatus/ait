package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.Tardis;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class TelepathicControl extends Control {
    public TelepathicControl() {
        super("telepathic circuit");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        BlockPos destinationPos = tardis.getTravel().getDestination();
        ServerWorld newWorld = (ServerWorld) tardis.getTravel().getDestination().getWorld();
        RegistryEntry<Biome> biome = newWorld.getBiome(destinationPos);
        String biomeTranslationKey = biome.getKeyOrValue().orThrow().getValue().toShortTranslationKey();
        player.sendMessage(Text.translatable("Destination Biome: " + DimensionControl.capitalizeAndReplaceEach(biomeTranslationKey)), true);
        return true;
    }
}
