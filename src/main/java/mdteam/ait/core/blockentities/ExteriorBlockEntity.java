package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.linkable.LinkableBlockEntity;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.MaterialStateEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.util.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

import static mdteam.ait.AITMod.EXTERIORNBT;

public class ExteriorBlockEntity extends LinkableBlockEntity {

    public final AnimationState ANIMATION_STATE = new AnimationState();
    private ExteriorAnimation animation;

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(ServerWorld world, boolean sneaking, PlayerEntity player) {
        if(this.tardis.getTravel().getState() == TardisTravel.State.LANDED) {
            if (!this.tardis.getExterior().isLocked()) {
                this.setLeftDoorRot(this.getLeftDoorRotation() == 0 ? 1.2f : 0);
                world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6f, 1f);
            } else {
                if (player != null) {
                    world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.6F, 1F);
                    player.sendMessage(Text.literal("TARDIS is locked!"), true);
                }
            }
        }

        if (sneaking)
            return;

        DoorBlockEntity door = TardisUtil.getDoor(this.tardis);

        if(this.tardis.getTravel().getState() == TardisTravel.State.LANDED && door != null) {
            TardisUtil.getTardisDimension().getChunk(door.getPos()); // force load the chunk

            door.setLeftDoorRot(this.getLeftDoorRotation());
            door.setRightDoorRot(this.getRightDoorRotation());
        }
    }

    public void setExterior(ExteriorEnum exterior) {
        EXTERIORNBT.get(this).setExterior(exterior);
    }

    @Deprecated
    public ExteriorEnum getExterior() {
        return EXTERIORNBT.get(this).getExterior();
    }

    public void setLeftDoorRot(float rotation) {
        EXTERIORNBT.get(this).setLeftDoorRotation(rotation);
    }

    public void setRightDoorRot(float rotation) {
        EXTERIORNBT.get(this).setRightDoorRotation(rotation);
    }

    public float getLeftDoorRotation() {
        return EXTERIORNBT.get(this).getLeftDoorRotation();
    }

    public float getRightDoorRotation() {
        return EXTERIORNBT.get(this).getRightDoorRotation();
    }

    public void setMaterialState(MaterialStateEnum materialState) {
        EXTERIORNBT.get(this).setMaterialState(materialState);
    }

    public MaterialStateEnum getMaterialState() {
        return EXTERIORNBT.get(this).getCurrentMaterialState();
    }

    public ExteriorAnimation getAnimation() {
        if (this.animation != null)
            return this.animation;

        this.animation = this.getExterior().createAnimation(this);
        AITMod.LOGGER.debug("Created new animation for " + this);

        if (this.tardis != null)
            this.animation.setupAnimation(this.tardis.getTravel().getState());

        return this.animation;
    }

    public float getAlpha() {
        return this.getAnimation().getAlpha();
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        if (this.getLeftDoorRotation() > 0 || this.getRightDoorRotation() > 0) {
            TardisUtil.teleportInside(this.getTardis(), player);
        }
    }

    @Override
    public void setTardis(Tardis tardis) {
        super.setTardis(tardis);
        this.linkTravel(); // force re-link travel if it's not null
    }

    @Override
    public void setTravel(TardisTravel travel) {
        this.getAnimation().setupAnimation(travel.getState());
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T t) {
        if (!(t instanceof ExteriorBlockEntity exterior))
            return;

        if (exterior.animation != null)
            exterior.getAnimation().tick();
    }

    public void onBroken() {}
}
