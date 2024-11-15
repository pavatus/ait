package loqor.ait.core.engine.link.block;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.engine.link.IFluidLink;
import loqor.ait.core.engine.link.IFluidSource;
import loqor.ait.core.engine.link.tracker.WorldFluidTracker;

public class FluidLinkBlockEntity extends BlockEntity implements IFluidLink {
    private IFluidLink last;
    private IFluidSource source;
    private BlockPos lastPos;

    protected FluidLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public FluidLinkBlockEntity(BlockPos pos, BlockState state) {
        this(AITBlockEntityTypes.FLUID_LINK_BLOCK_ENTITY, pos, state);
    }

    @Override
    public IFluidSource source(boolean search) {
        if (source == null && search) {
            if (this.last() != null) {
                this.setSource(this.last().source(true));
            } else {
                if (this.hasWorld()) {
                    this.search(this.getWorld(), this.getPos());
                }
            }
        }

        return this.source;
    }
    public IFluidSource source() {
        return this.source(true);
    }

    @Override
    public void setSource(IFluidSource source) {
        this.source = source;

        if (this.hasWorld()) {
            this.getWorld().updateNeighbors(this.getPos(), this.getCachedState().getBlock());
        }
    }

    @Override
    public IFluidLink last() {
        return this.last;
    }

    @Override
    public void setLast(IFluidLink last) {
        this.last = last;
    }

    public BlockPos getLastPos() {
        return lastPos;
    }

    public void setLastPos(BlockPos lastPos) {
        this.lastPos = lastPos;
    }

    public void onBroken(World world, BlockPos pos) {
        if (world.isClient())
            return;
    }

    public void onPlaced(World world, BlockPos pos, @Nullable LivingEntity placer) {
        if (world.isClient())
            return;

        this.search(world, pos);

        if (placer != null && this.source() != null) {
            placer.sendMessage(Text.literal("Fluid link placed, source: " + this.source().toString()));
        }
    }

    private void search(World world, BlockPos pos) {
        HashMap<Direction, IFluidLink> connections = WorldFluidTracker.getConnections((ServerWorld) world, pos, null);

        for (Direction dir : connections.keySet()) {
            IFluidLink i = connections.get(dir);
            if (i == this) continue;

            if (i != null && i.source(false) != null) {
                this.setLast(i);
                this.setLastPos(pos.offset(dir));

                if (i instanceof IFluidSource) {
                    this.setSource((IFluidSource) i);
                }

                break;
            }
        }
    }
    private void clear() {
        this.setLast(null);
        this.setLastPos(null);
        this.setSource(null);
    }

    public void onNeighborUpdate(World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos) {
        if (world.isClient())
            return;

        if (this.last() == null) { // if last is null, search for new source
            this.search(world, pos);
        }

        if (sourcePos.equals(this.getLastPos())) {
            this.onLastUpdate(world, pos);
        }
    }
    private void onLastUpdate(World world, BlockPos pos) {
        IFluidLink link = WorldFluidTracker.query((ServerWorld) world, this.getLastPos());
        if ((world.getBlockState(this.getLastPos()).isAir()) || (link != null && link.source(false) == null)) { // is last in chain and is removed
            this.clear();
        }

        if (link == null) return;
        if (this.source(false) == null && link.source(false) != null) {
            this.setSource(link.source(false));
        }
    }
}
