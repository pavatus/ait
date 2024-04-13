package loqor.ait.tardis.data;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.structure.OceanRuinStructure;

public class BiomeHandler extends TardisLink {

    public static final String SNOWY = "snowy";

    public BiomeHandler(Tardis tardis) {
        super(tardis, TypeId.BIOME);
    }

    @Override
    public void tick(ServerWorld world) {
        super.tick(world);

        if(this.findTardis().isEmpty() || world.getBiomeFabric(this.findTardis().get().position()).getKey().isEmpty()) return;

        PropertiesHandler.set(this.findTardis().get(), SNOWY, world.getBiome(this.findTardis().get().position()).getKey().get() == BiomeKeys.SNOWY_TAIGA, true);
    }
}
