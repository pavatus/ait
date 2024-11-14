package loqor.ait.core.blockentities;



import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.api.link.v2.block.InteriorLinkableBlockEntity;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.engine.impl.EngineSystem;

public class EngineBlockEntity extends InteriorLinkableBlockEntity {

    public EngineBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.ENGINE_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(BlockState state, World world, boolean sneaking, PlayerEntity player) {
        if (world.isClient() || this.tardis().isEmpty())
            return;

        EngineSystem engine = this.tardis().get().subsystems().engine();
        engine.setEnabled(!engine.isEnabled());
    }

    public void onBroken(World world, BlockPos pos) {
        this.tardis().ifPresent(tardis -> tardis.subsystems().engine().setEnabled(false));
    }

    public void onPlaced(World world, BlockPos pos, @Nullable LivingEntity placer) {
        this.tardis().ifPresent(tardis -> tardis.subsystems().engine().setEnabled(true));
    }
}
