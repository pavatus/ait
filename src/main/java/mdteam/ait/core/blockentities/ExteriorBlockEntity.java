package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
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
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static mdteam.ait.tardis.TardisTravel.State.LANDED;
import static mdteam.ait.tardis.TardisTravel.State.MAT;
import static mdteam.ait.tardis.util.TardisUtil.findTardisByPosition;
import static mdteam.ait.tardis.util.TardisUtil.isClient;

public class ExteriorBlockEntity extends BlockEntity { // fixme copy tardishandler and refactor to use uuids instead, this is incredibly inefficient and the main cause of lag.

    private UUID tardisId;
    public final AnimationState ANIMATION_STATE = new AnimationState();
    private ExteriorAnimation animation;

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(ServerWorld world, boolean sneaking, PlayerEntity player) {
        if (player == null)
            return;

        if (player.getMainHandStack().getItem() instanceof KeyItem) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if (!tag.contains("tardis")) {
                return;
            }
            if (Objects.equals(this.tardis().getUuid().toString(), tag.getString("tardis"))) {
                DoorHandler.toggleLock(this.tardis(), world, (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return;
        }
        DoorHandler.useDoor(this.tardis(), (ServerWorld) this.getWorld(), this.getPos(), (ServerPlayerEntity) player);

        if (sneaking)
            return;
        this.sync();
        this.tardis().getDoor().markDirty();
    }

    public float getLeftDoorRotation() {

        if (this.tardis() == null) return 5;

        return this.tardis().getDoor().isLeftOpen() ? 1.2f : 0;
    }

    public float getRightDoorRotation() {

        if (this.tardis() == null) return 5;

        return this.tardis().getDoor().isRightOpen() ? 1.2f : 0;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (this.tardis() == null) {
            AITMod.LOGGER.error("this.tardis() is null! Is " + this + " invalid? BlockPos: " + "(" + this.getPos().toShortString() + ")");
        }
        super.writeNbt(nbt);
        nbt.putString("tardis", this.tardisId.toString());
        nbt.putFloat("alpha", this.getAlpha());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("tardis")) {
            this.tardisId = UUID.fromString(nbt.getString("tardis"));
        }
        if (this.getAnimation() != null)
            this.getAnimation().setAlpha(nbt.getFloat("alpha"));
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        if (this.tardis() != null && (this.getLeftDoorRotation() > 0 || this.getRightDoorRotation() > 0)) {
            if (!this.tardis().getLockedTardis())
                TardisUtil.teleportInside(this.tardis(), player);
        }
    }

    public Tardis tardis() {
        if (this.tardisId == null) {
            AITMod.LOGGER.warn("Exterior at " + this.getPos() + " is finding TARDIS!");
            this.findTardisFromPosition();
        }

        if (isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(this.tardisId);
        }

        return ServerTardisManager.getInstance().getTardis(this.tardisId);
    }

    public void setTardis(Tardis tardis) {
        this.tardisId = tardis.getUuid();
    }

    public void sync() {
        if (isClient()) return;

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis());
    }

    private void findTardisFromPosition() { // should only be used if tardisId is null so we can hopefully refind the tardis
        Tardis found = findTardisByPosition(this.getPos());

        if (found == null) return;

        this.tardisId = found.getUuid();
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T exterior) {
        if (((ExteriorBlockEntity) exterior).animation != null)
            ((ExteriorBlockEntity) exterior).getAnimation().tick();


        if (!world.isClient() && ((ExteriorBlockEntity) exterior).tardis() != null && !PropertiesHandler.getBool(((ExteriorBlockEntity) exterior).tardis().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED) && ((ExteriorBlockEntity) exterior).tardis().getTravel().getState() == MAT && ((ExteriorBlockEntity) exterior).getAlpha() >= 0.9f) {
            for (ServerPlayerEntity entity : world.getEntitiesByClass(ServerPlayerEntity.class, new Box(exterior.getPos()).expand(0, 1, 0), EntityPredicates.EXCEPT_SPECTATOR)) {
                TardisUtil.teleportInside(((ExteriorBlockEntity) exterior).tardis(), entity); // fixme i dont like how this works you can just run into peoples tardises while theyre landing
            }
        }
    }

    // es caca
    public void verifyAnimation() {
        if (this.animation != null || this.tardis() == null || this.tardis().getExterior() == null || this.getExteriorType() == null)
            return;

        this.animation = this.getExteriorType().createAnimation(this);
        AITMod.LOGGER.warn("Created new ANIMATION for " + this);
        this.animation.setupAnimation(this.tardis().getTravel().getState());

        if (this.getWorld() != null) {
            if (!this.getWorld().isClient()) {
                this.animation.tellClientsToSetup(this.tardis().getTravel().getState());
            }
        }
    }

    public ExteriorAnimation getAnimation() {
        if(this.tardis() != null)
            if(this.tardis().getTravel().getState() != LANDED)
                this.verifyAnimation();

        return this.animation;
    }

    public ExteriorEnum getExteriorType() {
        return this.tardis().getExterior().getType();
    }

    public float getAlpha() {
        if (this.getAnimation() == null) {
            return 1f;
        }

        return this.getAnimation().getAlpha();
    }

    public void onBroken() {
        if(this.tardis() != null)
            this.tardis().getTravel().setState(TardisTravel.State.FLIGHT);
    }
}