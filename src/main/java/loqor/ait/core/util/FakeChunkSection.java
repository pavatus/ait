package loqor.ait.core.util;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.ChunkSection;

public class FakeChunkSection extends ChunkSection {

    public FakeChunkSection(ServerWorld world) {
        super(world.getRegistryManager().get(RegistryKeys.BIOME));
    }
}
