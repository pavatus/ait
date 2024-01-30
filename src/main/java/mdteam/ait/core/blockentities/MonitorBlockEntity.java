package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

import static mdteam.ait.tardis.util.TardisUtil.findTardisByInterior;
import static mdteam.ait.tardis.util.TardisUtil.findTardisByPosition;

public class MonitorBlockEntity extends BlockEntity {

    public UUID tardisid;

    public MonitorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.MONITOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if(this.getTardisID() != null)
            nbt.putUuid("tardisid", this.getTardisID());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if(nbt.contains("tardisid"))
            this.setTardisID(nbt.getUuid("tardisid"));
        super.readNbt(nbt);
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if(world.isClient() || this.getTardisID() == null) return;
        AITMod.openScreen((ServerPlayerEntity) player, 0, this.getTardisID()); // we can cast because we know its on server :p
    }

    public void setTardisID(UUID id) {
        this.tardisid = id;
        markDirty();
    }

    public UUID getTardisID() {
        if (tardisid == null) findTardisFromPosition();

        return tardisid;
    }
    private void findTardisFromPosition() { // should only be used if tardisId is null so we can hopefully refind the tardis
        Tardis found = findTardisByInterior(this.getPos(), !this.getWorld().isClient());

        if (found == null) return;

        this.tardisid = found.getUuid();
        markDirty();
    }
}
