package mdteam.ait.core.blockentities.door;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.util.TardisUtil;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class ExteriorBlockEntity extends AbstractDoorBlockEntity {

    private ExteriorAnimation animation;

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public ExteriorAnimation getAnimation() {
        if (this.animation != null)
            return this.animation;

        this.animation = this.tardis.getExterior().getType().createAnimation(this);
        AITMod.LOGGER.debug("Created new animation for " + this);

        if (this.tardis != null)
            this.animation.setupAnimation(this.tardis.getTravel().getState());

        return this.animation;
    }

    public float getAlpha() {
        return this.getAnimation().getAlpha();
    }

    @Override
    protected void teleport(Entity entity) {
        TardisUtil.teleportInside(this.tardis, (ServerPlayerEntity) entity);
    }

    @Override
    public void setTardis(Tardis tardis) {
        super.setTardis(tardis);
        this.linkTravel();
    }

    @Override
    public void setTravel(TardisTravel travel) {
        this.animation = this.tardis.getExterior().getType().createAnimation(this);
        this.animation.setupAnimation(travel.getState());
    }

    public static <T extends BlockEntity> void tick(T t) {
        if (!(t instanceof ExteriorBlockEntity exterior))
            return;

        if (exterior.animation != null)
            exterior.getAnimation().tick();
    }
}
