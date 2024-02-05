package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.impl.SecurityControl;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.link.LinkableBlockEntity;
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

import java.util.Optional;
import java.util.UUID;

import static mdteam.ait.tardis.util.TardisUtil.findTardisByInterior;
import static mdteam.ait.tardis.util.TardisUtil.findTardisByPosition;

public class MonitorBlockEntity extends LinkableBlockEntity {

    public MonitorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.MONITOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if (world.isClient() || this.getTardis().isEmpty()) return;
        boolean security = PropertiesHandler.getBool(this.getTardis().get().getHandlers().getProperties(), SecurityControl.SECURITY_KEY);
        if (security) {
            if (!SecurityControl.hasMatchingKey((ServerPlayerEntity) player, this.getTardis().get())) {
                return;
            }
        }
        AITMod.openScreen((ServerPlayerEntity) player, 0, this.getTardis().get().getUuid()); // we can cast because we know its on server :p
    }


    @Override
    public Optional<Tardis> getTardis() {
        if(this.tardisId == null && this.hasWorld()) {
            assert this.getWorld() != null;
            Tardis found = findTardisByInterior(pos, !this.getWorld().isClient());
            if (found != null)
                this.setTardis(found);
        }
        return super.getTardis();
    }
}
