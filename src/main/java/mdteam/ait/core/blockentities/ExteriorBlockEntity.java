package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.MaterialStateEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.helper.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the.mdteam.ait.*;

import static mdteam.ait.AITMod.EXTERIORNBT;

public class ExteriorBlockEntity extends BlockEntity implements ILinkable, BlockEntityTicker<ExteriorBlockEntity> {

    private Tardis tardis;
    public final AnimationState ANIMATION_STATE = new AnimationState();
    public int tickCount = 0;

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void refindTardis() {
        if (this.tardis != null) // No issue
            return;
        if (this.getWorld().isClient())
            return;

        ServerTardisManager manager = ServerTardisManager.getInstance();

        for (Tardis tardis : manager.getLookup().values()) {
            if (!tardis.getTravel().getPosition().equals(this.pos)) continue;

            this.setTardis(tardis);
            return;
        }

        AITMod.LOGGER.warn("Deleting exterior block at " + this.pos + " due to lack of Tardis!");
        this.getWorld().removeBlock(this.pos, false);
    }
    public void refindTardisClient() {
        if (this.tardis != null) // No issue
            return;
        if (!this.getWorld().isClient())
            return;

        ClientTardisManager manager = ClientTardisManager.getInstance();

        for (Tardis tardis : manager.getLookup().values()) {
            if (!tardis.getTravel().getPosition().equals(this.pos)) continue;

            this.setTardis(tardis);
            return;
        }

    }

    public void useOn(ServerWorld world, boolean sneaking) {
        if (this.tardis == null) {
            refindTardis();
            return;
        }

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

        if (this.tardis != null) {
            nbt.putUuid("tardis", this.tardis.getUuid());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("tardis")) {
            TardisManager.getInstance().link(nbt.getUuid("tardis"), this);
        }
    }

    public void onEntityCollision(Entity entity) {
        if (this.tardis == null) {
            refindTardis();
            return;
        }
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        if (this.getLeftDoorRotation() > 0 || this.getRightDoorRotation() > 0) {
            TardisUtil.teleportInside(this.tardis, player);
        }
    }

    @Override
    public Tardis getTardis() {
        if (getWorld().isClient()) // ive come to the realisation client cannot be trusted so just always reask
            syncTardisFromServer();
        
        return tardis;
    }
    public void syncTardisFromServer() {
        // this.refindTardisClient();
        if (!this.getWorld().isClient())
            return;

        ClientTardisManager manager = ClientTardisManager.getInstance();

        manager.ask(this.pos);

        this.refindTardisClient();
    }

    @Override
    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, ExteriorBlockEntity blockEntity) {
        if (!ANIMATION_STATE.isRunning()) {
            tickCount = 0;
            ANIMATION_STATE.start(tickCount);
        } else {
            tickCount++;
        }

        if (this.tardis.getTravel().getState() == TardisTravel.State.DEMAT) {
            tickCount++;
        }
    }
}
