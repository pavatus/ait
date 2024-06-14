package loqor.ait.tardis.link.v2;

import loqor.ait.tardis.TardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractLinkableBlockEntity extends BlockEntity {

    protected TardisRef ref;

    public AbstractLinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public TardisRef tardis() {
        return ref;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (this.ref == null)
            return;

        nbt.putUuid("tardisId", this.ref.getId());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtElement id = nbt.get("tardisId");

        if (id == null)
            return;

        this.ref = new TardisRef(NbtHelper.toUuid(id), uuid -> TardisManager.with(
                this.world, (o, manager) -> manager.demandTardis(o, uuid))
        );
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    protected void sync() {
        if (this.world != null && this.world.getChunkManager() instanceof ServerChunkManager chunkManager)
            chunkManager.markForUpdate(this.pos);
    }
}