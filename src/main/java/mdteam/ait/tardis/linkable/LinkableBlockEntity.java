package mdteam.ait.tardis.linkable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.manager.TardisManager;

public abstract class LinkableBlockEntity extends BlockEntity implements Linkable {

    protected Tardis tardis;

    public LinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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
                isServer = nbt.getBoolean("client");
            }

            TardisManager.getInstance(isServer).link(nbt.getUuid("tardis"), this);
        }
    }

    @Override
    public Tardis getTardis() {
        return tardis;
    }

    @Override
    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
    }
}
