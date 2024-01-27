package mdteam.ait.tardis.link;


import blue.endless.jankson.annotation.Nullable;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisManager;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.UUID;

public abstract class LinkableBlockEntity extends BlockEntity implements Linkable {

    protected UUID tardisId;

    public LinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isLinked() {
        return this.tardisId != null;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if(this.tardisId != null) {
            nbt.putUuid("tardis", this.tardisId);
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

    // lord save us.
    public Optional<Tardis> getTardis() {
        if (TardisUtil.isClient()) { // todo replace deprecated check
            if (!ClientTardisManager.getInstance().hasTardis(this.tardisId)) {
                ClientTardisManager.getInstance().loadTardis(this.tardisId, tardis -> {});
                return Optional.empty();
                // todo add of `ifPresent()` of `isEmpty()` checks
                // eg if before it was PropertiesHandler.set(this.getTardis, ...)
                // it should become:
                // this.getTardis().ifPresent(tardis -> PropertiesHandler.set(tardis, ...))
                // or
                // if (this.getTardis().isEmpty()) return;
                //  because i dont want to rewrite a lot of the code base rn. this needs replacing badly but i am desperate for this release to come out and idc.
                // issues with doing it this way is that client will probably have to repeat things multiple times to get things to happen.
            }
            return Optional.of(ClientTardisManager.getInstance().getTardis(this.tardisId));
        }
        return Optional.ofNullable(ServerTardisManager.getInstance().getTardis(this.tardisId));
    }

    @Override
    public void setTardis(Tardis tardis) {
        this.tardisId = tardis.getUuid();
    }
}