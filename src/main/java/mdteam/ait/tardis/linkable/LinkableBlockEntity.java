package mdteam.ait.tardis.linkable;

import mdteam.ait.tardis.ITardis;
import mdteam.ait.tardis.manager.TardisManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class LinkableBlockEntity extends BlockEntity implements Linkable {

    protected ITardis tardis;

    public LinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isLinked() {
        return this.tardis != null;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if(this.tardis != null) {
            nbt.putUuid("tardis", this.tardis.getUuid());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if(nbt.contains("tardis")) {
            boolean isServer = true;
            if (nbt.contains("client")) {
                isServer = !nbt.getBoolean("client");
            }

            TardisManager.getInstance(isServer).link(nbt.getUuid("tardis"), this);
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound compound = this.createNbt();
        compound.putBoolean("client", true);

        return compound;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void sync() {
        if (this.world == null)
            return;

        this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    @Override
    public ITardis getTardis() {
        return tardis;
    }

    @Override
    public void setTardis(ITardis tardis) {
        this.tardis = tardis;
    }
}
