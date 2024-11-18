package loqor.ait.core.blockentities;



import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITSounds;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.SubSystemBlockEntity;
import loqor.ait.core.engine.impl.EngineSystem;
import loqor.ait.core.engine.link.IFluidLink;
import loqor.ait.core.engine.link.IFluidSource;
import loqor.ait.core.engine.link.ITardisSource;
import loqor.ait.core.tardis.Tardis;

public class EngineBlockEntity extends SubSystemBlockEntity implements ITardisSource {
    public EngineBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.ENGINE_BLOCK_ENTITY_TYPE, pos, state, SubSystem.Id.ENGINE);

        if (!this.hasWorld()) return;
    }

    @Override
    public void useOn(BlockState state, World world, boolean sneaking, PlayerEntity player, ItemStack hand) {
        if (world.isClient() || this.tardis().isEmpty())
            return;

        EngineSystem engine = this.tardis().get().subsystems().engine();

        if (engine.isRepairItem(hand) && engine.durability() < 100) {
            engine.addDurability(5);
            hand.decrement(1);
            world.playSound(null, this.getPos(), AITSounds.BWEEP, SoundCategory.BLOCKS, 0.5f, 1f);
            return;
        }

        engine.setEnabled(!engine.isEnabled());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, @Nullable LivingEntity placer) {
        super.onPlaced(world, pos, placer);
        if (world.isClient())
            return;

        this.tardis().ifPresent(tardis -> tardis.subsystems().engine().setEnabled(true));
    }

    @Override
    public Tardis getTardisForFluid() {
        return this.tardis().get();
    }

    @Override
    public void setSource(IFluidSource source) {

    }

    @Override
    public void setLast(IFluidLink last) {

    }

    @Override
    public IFluidSource source(boolean search) {
        return this;
    }

    @Override
    public IFluidLink last() {
        return this;
    }

    @Override
    public BlockPos getLastPos() {
        return this.getPos();
    }
}
