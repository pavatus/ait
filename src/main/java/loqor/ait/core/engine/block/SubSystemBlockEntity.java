package loqor.ait.core.engine.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITSounds;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.link.block.FluidLinkBlockEntity;

public class SubSystemBlockEntity extends FluidLinkBlockEntity {
    private SubSystem.IdLike id;

    public SubSystemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, SubSystem.IdLike id) {
        super(type, pos, state);
        this.id = id;
    }

    public SubSystem system() {
        if (this.tardis() == null || this.tardis().isEmpty()) return null;

        return this.tardis().get().subsystems().get(this.id);
    }
    protected SubSystem.IdLike id() {
        if (this.id == null) {
            this.id = ((SubSystemBlock) this.getCachedState().getBlock()).getSystemId();
        }

        return this.id;
    }

    @Override
    public void onGainFluid() {
        this.system().setEnabled(true);

        if (this.hasWorld()) {
            this.getWorld().playSound(null, this.getPos(), AITSounds.SIEGE_DISABLE, SoundCategory.BLOCKS, 0.25f, 1.0F);
        }
    }

    @Override
    public void onLoseFluid() {
        this.system().setEnabled(false);

        if (this.hasWorld()) {
            this.getWorld().playSound(null, this.getPos(), AITSounds.SIEGE_ENABLE, SoundCategory.BLOCKS, 0.25f, 1.0F);
        }
    }

    @Override
    public void onBroken(World world, BlockPos pos) {
        super.onBroken(world, pos);

        this.onLoseFluid();
    }
}
