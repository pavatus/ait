package mdteam.ait.core.blockentities;

import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.MaterialStateEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.helper.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisManager;

import static mdteam.ait.AITMod.EXTERIORNBT;

public class ExteriorBlockEntity extends BlockEntity implements ILinkable {

    private Tardis tardis;
    public final AnimationState ANIMATION_STATE = new AnimationState();
    private final ExteriorAnimation animation = new PulsatingAnimation(this);

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(ServerWorld world, boolean sneaking) {
        if (this.getLeftDoorRotation() == 0) {
            this.setLeftDoorRot(1.2f);
        } else {
            this.setLeftDoorRot(0);
        }

        if (sneaking)
            return;

        world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6f, 1f);
        DoorBlockEntity door = TardisUtil.getDoor(this.tardis);

        if (door != null) {
            TardisUtil.getTardisDimension().getChunk(door.getPos()); // force load the chunk

            door.setLeftDoorRot(this.getLeftDoorRotation());
            door.setRightDoorRot(this.getRightDoorRotation());
        }
    }

    public void setExterior(ExteriorEnum exterior) {
        EXTERIORNBT.get(this).setExterior(exterior);
    }

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

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        this.getAnimation().setAlpha(nbt.getFloat("alpha"));

        if (this.tardis != null) {
            nbt.putUuid("tardis", this.tardis.getUuid());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        nbt.putFloat("alpha", this.getAlpha());

        if (nbt.contains("tardis")) {
            TardisManager.getInstance().link(nbt.getUuid("tardis"), this);
        }
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        if (this.tardis != null && (this.getLeftDoorRotation() > 0 || this.getRightDoorRotation() > 0)) {
            TardisUtil.teleportInside(this.tardis, player);
        }
    }

    @Override
    public Tardis getTardis() {
        return tardis;
    }

    @Override
    public void setTardis(Tardis tardis) {
        this.tardis = tardis;

        this.animation.setupAnimation(this.tardis.getTravel().getState());
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T exterior) {
        ((ExteriorBlockEntity) exterior).getAnimation().tick();
    }

    public ExteriorAnimation getAnimation() {
        return this.animation;
    }

    public float getAlpha() {
        return this.getAnimation().getAlpha();
    }
}
