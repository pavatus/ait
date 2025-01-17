package loqor.ait.mixin.server;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.chunk.BlendingData;

import loqor.ait.api.Clearable;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin extends Chunk implements Clearable {

    @Shadow @Final
    World world;

    @Shadow protected abstract <T extends BlockEntity> void removeGameEventListener(T blockEntity, ServerWorld world);

    @Shadow @Final private Map<BlockPos, WorldChunk.WrappedBlockEntityTickInvoker> blockEntityTickers;

    @Shadow @Final private static BlockEntityTickInvoker EMPTY_BLOCK_ENTITY_TICKER;

    public WorldChunkMixin(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry<Biome> biomeRegistry, long inhabitedTime, @Nullable ChunkSection[] sectionArray, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, biomeRegistry, inhabitedTime, sectionArray, blendingData);
    }

    @Override
    public void ait$clear() {
        for (ChunkSection section : this.getSectionArray()) {
            if (section instanceof Clearable clearable)
                clearable.ait$clear();
        }

        for (BlockEntity blockEntity : this.blockEntities.values()) {
            this.removeGameEventListener(blockEntity, (ServerWorld) this.world);
            blockEntity.markRemoved();
        }

        for (WorldChunk.WrappedBlockEntityTickInvoker invoker : this.blockEntityTickers.values()) {
            invoker.setWrapped(EMPTY_BLOCK_ENTITY_TICKER);
        }

        this.blockEntities.clear();
        this.blockEntityTickers.clear();
        this.blockEntityNbts.clear();
    }
}
