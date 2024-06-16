package loqor.ait.tardis.link.v2;

import loqor.ait.tardis.Tardis;
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

import java.util.UUID;

public abstract class AbstractLinkableBlockEntity extends BlockEntity implements Linkable {

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

        nbt.putUuid("tardis", this.ref.getId());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtElement id = nbt.get("tardis");

        if (id == null)
            return;

        this.ref = new TardisRef(NbtHelper.toUuid(id), uuid -> TardisManager.with(
                this.world, (o, manager) -> manager.demandTardis(o, uuid))
        );

        if (this.world == null)
            return;

        this.onLinked();
    }

    @Override
    public void link(Tardis tardis) {
        this.ref = new TardisRef(tardis, uuid -> TardisManager.with(
                this.world, (o, manager) -> manager.demandTardis(o, uuid))
        );

        this.onLinked();

        this.sync();
        this.markDirty();
    }

    @Override
    public void link(UUID id) {
        this.ref = new TardisRef(id, uuid -> TardisManager.with(
                this.world, (o, manager) -> manager.demandTardis(o, uuid))
        );

        this.onLinked();

        this.sync();
        this.markDirty();
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