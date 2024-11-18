package loqor.ait.core.engine.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITSounds;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.link.block.FluidLinkBlockEntity;
import loqor.ait.core.util.SoundData;

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
        super.onGainFluid();

        this.system().setEnabled(true);
    }

    @Override
    public void onLoseFluid() {
        super.onLoseFluid();

        this.system().setEnabled(false);
    }

    @Override
    protected SoundData getGainPowerSound() {
        return new SoundData(AITSounds.SIEGE_DISABLE, SoundCategory.BLOCKS, 0.25f, 1.0f);
    }

    @Override
    protected SoundData getLosePowerSound() {
        return new SoundData(AITSounds.SIEGE_ENABLE, SoundCategory.BLOCKS, 0.25f, 1.0f);
    }

    public void tick(World world, BlockPos pos, BlockState state) {}
    public void useOn(BlockState state, World world, boolean sneaking, PlayerEntity player, ItemStack hand) {

    }

    @Environment(EnvType.CLIENT)
    public void offsetModel(MatrixStack stack) {

    }
}