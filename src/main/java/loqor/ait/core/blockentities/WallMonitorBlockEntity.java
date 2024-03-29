package loqor.ait.core.blockentities;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.link.LinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

import static loqor.ait.tardis.util.TardisUtil.findTardisByInterior;

public class WallMonitorBlockEntity extends LinkableBlockEntity {

    public WallMonitorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.WALL_MONITOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if (world.isClient() || this.findTardis().isEmpty()) return;
        boolean security = PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), SecurityControl.SECURITY_KEY);
        if (security) {
            if (!SecurityControl.hasMatchingKey((ServerPlayerEntity) player, this.findTardis().get())) {
                return;
            }
        }
        player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
        AITMod.openScreen((ServerPlayerEntity) player, 0, this.findTardis().get().getUuid()); // we can cast because we know its on server :p
    }

    @Override
    public Optional<Tardis> findTardis() {
        if(this.tardisId == null && this.hasWorld()) {
            assert this.getWorld() != null;
            Tardis found = findTardisByInterior(pos, !this.getWorld().isClient());
            if (found != null)
                this.setTardis(found);
        }
        return super.findTardis();
    }
}
