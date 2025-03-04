package dev.amble.ait.core.tardis.util.network.s2c;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import dev.amble.ait.AITMod;
import dev.amble.ait.core.blockentities.ExteriorBlockEntity;
import dev.amble.ait.core.tardis.Tardis;

public class BOTIDataS2CPacket implements FabricPacket {
    public static final PacketType<BOTIDataS2CPacket> TYPE = PacketType.create(AITMod.id("send_boti_data"), BOTIDataS2CPacket::new);

    private final BlockPos botiPos;
    public Map<BlockPos, BlockState> posStates = new HashMap<>();
    public Map<BlockPos, BlockEntity> posTile = new HashMap<>();
    public final NbtCompound chunkData;

    public BOTIDataS2CPacket(BlockPos botiPos, WorldChunk chunk, BlockPos targetPos) {
        chunkData = new NbtCompound();
        this.botiPos = botiPos;
        NbtCompound blockEntities = new NbtCompound();
        int targetY = targetPos.getY();
        int baseY = targetY & ~15;
        int sectionIndex = chunk.getSectionIndex(targetY);
        ChunkSection section = chunk.getSection(sectionIndex);
        ChunkPos chunkPos = chunk.getPos();
        int r = 17;
        try {
            Map<BlockState, Integer> stateToIndex = new HashMap<>();
            stateToIndex.put(Blocks.AIR.getDefaultState(), 0);
            for (int y = 0; y < 16; y++) {
                for (int x = 0; x < r; x++) {
                    for (int z = 0; z < r; z++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (state != null && !state.isAir() && !stateToIndex.containsKey(state)) {
                            this.posStates.put(new BlockPos(x, y, z), state);
                        }
                    }
                }
            }

            // Collect block entity data
            for (int y = 0; y < 16; y++) {
                for (int x = 0; x < r; x++) {
                    for (int z = 0; z < r; z++) {
                        BlockPos worldPos = new BlockPos(chunkPos.getStartX() + x, baseY + y, chunkPos.getStartZ() + z);
                        BlockEntity be = chunk.getBlockEntity(worldPos);
                        if (be != null) {
                            NbtCompound blockEntityNbt = be.createNbtWithIdentifyingData();
                            String key = x + "_" + y + "_" + z;
                            blockEntities.put(key, blockEntityNbt);
                        }
                    }
                }
            }
            if (!blockEntities.isEmpty()) {
                this.chunkData.put("block_entities", blockEntities);
            }
        } catch (Exception e) {
            System.out.println("Exception in packet construction: " + e.getMessage());
            AITMod.LOGGER.atTrace();
            System.out.println("Using fallback stone data due to serialization failure");
        }
    }

    public BOTIDataS2CPacket(PacketByteBuf buf) {
        this.botiPos = buf.readBlockPos();
        this.posStates = buf.readMap(
                PacketByteBuf::readBlockPos,    // Key reader
                buffer -> {                     // Value reader
                    NbtCompound nbt = buffer.readNbt();
                    return BlockState.CODEC.parse(NbtOps.INSTANCE, nbt)
                            .result().orElse(Blocks.AIR.getDefaultState());
                }
        );

        this.chunkData = buf.readNbt();
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(botiPos);
        buf.writeMap(
                posStates,
                PacketByteBuf::writeBlockPos,   // Key writer
                (buffer, state) -> {            // Value writer
                    NbtCompound nbt = (NbtCompound) BlockState.CODEC.encodeStart(NbtOps.INSTANCE, state)
                            .result().orElse(new NbtCompound());
                    buffer.writeNbt(nbt);
                }
        );
        buf.writeNbt(chunkData);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    public <T> boolean handle(ClientPlayerEntity source, PacketSender response) {
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;

        if (world == null) return false;

        BlockEntity exterior = world.getBlockEntity(this.botiPos);

        if (exterior instanceof ExteriorBlockEntity exteriorBlockEntity) {
            if (exteriorBlockEntity.tardis() == null) return false;
            Tardis tardis = exteriorBlockEntity.tardis().get();
            tardis.stats().updateMap(this.posStates);
            tardis.stats().chunkData = this.chunkData;

            tardis.stats().updateChunkModel(exteriorBlockEntity);
//            this.posStates.forEach((pos, state) -> {
//
//                MinecraftClient.getInstance().getBlockRenderManager().getModel(state).getQuads(state, Direction.NORTH, MinecraftClient.getInstance().world.random);
//            });
        }
        return true;
    }
}