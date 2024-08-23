package loqor.ait.tardis.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

import net.minecraft.block.BlockState;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;

import loqor.ait.core.util.FakeChunkSection;

public class Gaslighter3000 {

    private final ServerWorld world;
    private final Map<ChunkPos, ChunkHolder> lookup = new HashMap<>();

    public Gaslighter3000(ServerWorld world) {
        this.world = world;
    }

    public void spreadLies(BlockPos pos, BlockState state) {
        this.lookup.computeIfAbsent(new ChunkPos(pos), chunkPos -> new ChunkHolder(world, new ChunkPos(pos)))
                .spreadLies(pos, state);
    }

    public void tweet() {
        for (ChunkHolder holder : lookup.values()) {
            holder.tweet();
        }
    }

    static class ChunkHolder {

        private final ServerWorld world;
        private final ChunkPos pos;

        private final ShortSet[] blockUpdatesBySection;
        private final FakeChunkSection[] sections;

        private final Map<BlockPos, BlockState> states = new HashMap<>();

        private boolean pendingBlockUpdates = false;

        public ChunkHolder(ServerWorld world, ChunkPos pos) {
            this.world = world;
            this.pos = pos;

            this.blockUpdatesBySection = new ShortSet[world.countVerticalSections()];
            this.sections = new FakeChunkSection[this.blockUpdatesBySection.length];
        }

        public void spreadLies(BlockPos pos, BlockState state) {
            int i = this.world.getSectionIndex(pos.getY());

            if (this.blockUpdatesBySection[i] == null) {
                this.pendingBlockUpdates = true;
                this.blockUpdatesBySection[i] = new ShortOpenHashSet();
            }

            if (this.sections[i] == null)
                this.sections[i] = new FakeChunkSection(this.world);

            this.blockUpdatesBySection[i].add(ChunkSectionPos.packLocal(pos));
            this.sections[i].setBlockState(pos.getX() & 0xF, pos.getY() & 0xF, pos.getZ() & 0xF, state);
        }

        public void tweet() {
            if (!this.pendingBlockUpdates)
                return;

            Collection<ServerPlayerEntity> list = PlayerLookup.tracking(this.world, this.pos);

            if (!list.isEmpty()) {
                for (int i = 0; i < this.blockUpdatesBySection.length; ++i) {
                    ShortSet shortSet = this.blockUpdatesBySection[i];

                    if (shortSet == null)
                        continue;

                    int j = this.world.sectionIndexToCoord(i);
                    ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(this.pos, j);

                    sendPacketToPlayers(list, new ChunkDeltaUpdateS2CPacket(chunkSectionPos, shortSet, this.sections[i]));
                }
            }

            this.pendingBlockUpdates = false;
        }

        private static void sendPacketToPlayers(Collection<ServerPlayerEntity> players, Packet<?> packet) {
            players.forEach(player -> player.networkHandler.sendPacket(packet));
        }
    }
}
