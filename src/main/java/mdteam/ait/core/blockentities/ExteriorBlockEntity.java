package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.*;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Objects;

import static mdteam.ait.tardis.TardisTravel.State.MAT;
import static mdteam.ait.tardis.util.TardisUtil.isClient;

public class ExteriorBlockEntity extends BlockEntity implements ILinkable {

    private Tardis tardis;
    public final AnimationState ANIMATION_STATE = new AnimationState();
    private ExteriorAnimation animation;

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(ServerWorld world, boolean sneaking, PlayerEntity player) {
        if(player == null)
            return;
        if(player.getMainHandStack().getItem() instanceof KeyItem) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if(!tag.contains("tardis")) {
                return;
            }
            if(Objects.equals(this.tardis().getUuid().toString(), tag.getUuid("tardis").toString())) {
                DoorHandler.toggleLock(this.tardis(), world, (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return;
        }
        // fixme this sucks
        if (this.tardis == null) {
            this.tardis();
            return;
        }
        DoorHandler.useDoor(this.tardis(), (ServerWorld) this.getWorld(), this.getPos(), (ServerPlayerEntity) player);
        System.out.println(this.tardis().getDoor().getDoorPos());
        if (sneaking)
            return;
        this.sync();
        this.tardis().getDoor().sync();
    }

    public float getLeftDoorRotation() {

        if (this.tardis == null) return 5;

        return this.tardis().getDoor().isLeftOpen() ? 1.2f : 0;
    }

    public float getRightDoorRotation() {

        if (this.tardis == null) return 5;

        return this.tardis().getDoor().isRightOpen() ? 1.2f : 0;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (this.tardis != null) {
            nbt.putUuid("tardis", this.tardis.getUuid());
        }

        nbt.putFloat("alpha", this.getAlpha());

        /*nbt.putFloat("leftDoor", this.getLeftDoorRotation());
        nbt.putFloat("rightDoor", this.getRightDoorRotation());*/
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("tardis")) {
            TardisManager.getInstance().link(nbt.getUuid("tardis"), this);
        }

        if(this.getAnimation() != null)
            this.getAnimation().setAlpha(nbt.getFloat("alpha"));
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        if (this.tardis() != null && this.tardis().getDoor().isLeftOpen()) {
            if(this.tardis().getDoor().isLeftOpen()) {
                if (!this.tardis().getLockedTardis()) {
                    TardisUtil.teleportInside(this.tardis(), player);
                }
            }
        }
    }

    // flip you im readdng it (the client doesnt have the tardis and this fixes it)
    public void refindTardisClient() {
        if (this.tardis != null) // No issue
            return;
        if (!this.getWorld().isClient())
            return;

        ClientTardisManager manager = ClientTardisManager.getInstance();

        if (manager.getLookup().isEmpty()) {
            manager.ask(this.getPos());
            return;
        }

        for (Tardis tardis : manager.getLookup().values()) {
            if (!tardis.getDoor().getExteriorPos().equals(this.pos)) continue;

            this.setTardis(tardis);
            return;
        }
        manager.ask(this.getPos());
    }
    // millionth jank sync method
    public void syncFromClientManager() {
        if (this.tardis == null)
            return;

        TardisTravel.State last = this.tardis.getTravel().getState();

        ClientTardisManager.getInstance().getTardis(this.tardis.getUuid(), (var) -> this.tardis = var);

        if (last != this.tardis.getTravel().getState()) {
            this.animation = null;
            this.animation = this.getExteriorType().createAnimation(this);
            AITMod.LOGGER.debug("Created new ANIMATION for " + this);
            this.animation.setupAnimation(this.tardis().getTravel().getState());
            // this.getAnimation();
        }

        // fixme oh god all this code is so bad just because i realised the client stops syncing propelry on relaoad and im too lazy to do it properly

        if (this.getWorld() != null && this.getWorld().isClient()) {
            if (this.tardis != null)
                ClientTardisManager.getInstance().ask(this.tardis.getUuid());
            else
                ClientTardisManager.getInstance().ask(this.getPos());
        }
    }
    // same here
    public void refindTardis() {
        //System.out.println(this.tardis);
        if (this.tardis != null) // No issue
            return;
        if (this.getWorld().isClient())
            return;

        ServerTardisManager manager = ServerTardisManager.getInstance();

        for (Tardis tardis : manager.getLookup().values()) {
            if (!tardis.getDoor().getExteriorPos().equals(this.pos)) continue;

            this.setTardis(tardis);
            return;
        }

        if (this.tardis != null) return;

        AITMod.LOGGER.warn("Deleting exterior block at " + this.pos + " due to lack of Tardis!");
        // this.getWorld().removeBlock(this.pos, false);
    }

    public Tardis tardis() {
        if (isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(this.getTardis().getUuid());
        }

        return ServerTardisManager.getInstance().getTardis(this.getTardis().getUuid());
    }

    @Override
    public Tardis getTardis() {
        // fixme unsure if still needed
        if (this.tardis == null && this.getWorld() != null) {
            if (!this.getWorld().isClient())
                refindTardis();
            else
                refindTardisClient();
        }

        if (this.getWorld() != null && this.getWorld().isClient() && this.tardis != null)
            syncFromClientManager(); // i accidentally made it make a bajillion different instances when it syncs so this fixes that and i dont know how to fix the other thing so theo can do it

        return this.tardis;
    }

    @Override
    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
        if (this.getAnimation() == null) return;
        this.getAnimation().setupAnimation(this.tardis.getTravel().getState());
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T exterior) {
        if (((ExteriorBlockEntity) exterior).animation != null)
            ((ExteriorBlockEntity) exterior).getAnimation().tick();


        if (!world.isClient() && ((ExteriorBlockEntity) exterior).getTardis() != null && !PropertiesHandler.get(((ExteriorBlockEntity) exterior).getTardis().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED) && ((ExteriorBlockEntity) exterior).getTardis().getTravel().getState() == MAT && ((ExteriorBlockEntity) exterior).getAlpha() >= 0.9f) {
            for (ServerPlayerEntity entity : world.getEntitiesByClass(ServerPlayerEntity.class, new Box(exterior.getPos()).expand(0,1,0), EntityPredicates.EXCEPT_SPECTATOR)) {
                TardisUtil.teleportInside(((ExteriorBlockEntity) exterior).getTardis(), entity);
            }
        }
    }


    // theo please stop deleting my shit theres a reason its there rarely its not just schizophrenic code rambles that are useless
    public void verifyAnimation() {
        if (this.animation != null || this.getTardis() == null || this.getTardis().getExterior() == null || this.getExteriorType() == null)
            return;

        this.animation = this.getExteriorType().createAnimation(this);
        AITMod.LOGGER.debug("Created new ANIMATION for " + this);
        this.animation.setupAnimation(this.getTardis().getTravel().getState());
    }

    public ExteriorAnimation getAnimation() {
        this.verifyAnimation();

        return this.animation;
    }

    public ExteriorEnum getExteriorType() {
        return this.tardis.getExterior().getType();
    }

    public float getAlpha() {
        if (this.getAnimation() == null) {
            return 1f;
        }

        return this.getAnimation().getAlpha();
    }

    public void onBroken() {}
}