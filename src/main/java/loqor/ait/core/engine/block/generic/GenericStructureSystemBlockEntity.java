package loqor.ait.core.engine.block.generic;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITSounds;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;
import loqor.ait.core.engine.block.multi.StructureSystemBlockEntity;
import loqor.ait.core.engine.item.SubSystemItem;
import loqor.ait.core.util.StackUtil;

/**
 * a mutable version of the structure system block entity
 * it can have its id changed
 * usually set by the SubSystemItem
 * @see loqor.ait.core.engine.item.SubSystemItem
 * @author duzo
 */
public class GenericStructureSystemBlockEntity extends StructureSystemBlockEntity {
    private ItemStack idSource;

    protected GenericStructureSystemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, null);
    }
    public GenericStructureSystemBlockEntity(BlockPos pos, BlockState state) {
        this(AITBlockEntityTypes.GENERIC_SUBSYSTEM_BLOCK_TYPE, pos, state);
    }

    @Override
    public ActionResult useOn(BlockState state, World world, boolean sneaking, PlayerEntity player, ItemStack hand) {
        if (!(world.isClient())) {
            if (hand.getItem() instanceof SubSystemItem link) {
                this.setId(link.id());
                this.idSource = hand.copy();
                this.idSource.setCount(1);
                hand.decrement(1);
                world.playSound(null, this.getPos(), AITSounds.WAYPOINT_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return ActionResult.SUCCESS;
            }
        }

        return super.useOn(state, world, sneaking, player, hand);
    }

    private void setId(SubSystem.IdLike id) {
        this.id = id;
        this.onChangeId();
    }
    protected StructureHolder getHolder() {
        if (!(this.system() instanceof StructureHolder holder)) return null;

        return holder;
    }
    protected void onChangeId() {
        this.processStructure();
        this.markDirty();
        this.sync();
    }
    public boolean hasSystem() {
        return this.id() != null;
    }

    @Override
    protected MultiBlockStructure getStructure() {
        StructureHolder holder = this.getHolder();
        if (holder == null) return null;

        return holder.getStructure();
    }
    @Override
    public boolean isStructureComplete(World world, BlockPos pos) {
        if (this.getStructure() == null) return false;

        return super.isStructureComplete(world, pos);
    }

    @Override
    protected boolean shouldRefresh(ServerWorld world, BlockPos pos) {
        if (this.getStructure() == null) return false;

        return super.shouldRefresh(world, pos);
    }
    @Override
    public void onLoseFluid() {
        if (this.system() == null) return;

        super.onLoseFluid();
    }

    @Override
    public void onBroken(World world, BlockPos pos) {
        super.onBroken(world, pos);

        if (world.isClient() || this.idSource == null) return;
        StackUtil.spawn(world, pos, this.idSource.copyAndEmpty());
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (this.idSource != null) {
            nbt.put("SourceStack", this.idSource.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("SourceStack")) {
            this.idSource = ItemStack.fromNbt(nbt.getCompound("SourceStack"));
        }
    }
}
