package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.data.DoorData;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static mdteam.ait.tardis.util.TardisUtil.isClient;

public class DoorBlockEntity extends BlockEntity {
    private UUID tardisId;
    public AnimationState DOOR_STATE = new AnimationState();
    public int animationTimer = 0;

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);

        // even though TardisDesktop links the door, we need to link it here as well to avoid desync
        Tardis found = TardisUtil.findTardisByPosition(pos);
        if (found != null)
            this.setTardis(found);
        if (this.getTardis() != null) {
            this.setDesktop(this.getDesktop());
            /*if(this.getDesktop() != null) {
                this.getDesktop().setInteriorDoorPos(new AbsoluteBlockPos.Directed(pos, TardisUtil.getTardisDimension(), this.getFacing()));
                this.getDesktop().updateDoor();
            }*/
        }
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T door) {
        if(world.isClient()) {
            ((DoorBlockEntity) door).checkAnimations();
        }
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if (player == null)
            return;
        if (this.getTardis().isGrowth() && this.getTardis().hasGrowthExterior())
            return;
        if (player.getMainHandStack().getItem() instanceof KeyItem && !getTardis().isSiegeMode()) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if (!tag.contains("tardis")) {
                return;
            }
            if (Objects.equals(this.getTardis().getUuid().toString(), tag.getString("tardis"))) {
                DoorData.toggleLock(this.getTardis(), (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return;
        }
        DoorData.useDoor(this.getTardis(), (ServerWorld) world, this.getPos(), (ServerPlayerEntity) player);
        // fixme maybe this is required idk the doorhandler already marks the tardis dirty || tardis().markDirty();
        if (sneaking)
            return;
    }
    public Direction getFacing() {
        return this.getCachedState().get(HorizontalDirectionalBlock.FACING);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (this.getTardis() == null) {
            AITMod.LOGGER.error("this.getTardis() is null! Is " + this + " invalid? BlockPos: " + "(" + this.getPos().toShortString() + ")");
        }
        super.writeNbt(nbt);
        if (this.getTardis() != null) // panick
            nbt.putString("tardis", this.getTardis().getUuid().toString());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("tardis")) {
            this.setTardis(UUID.fromString(nbt.getString("tardis")));
        }
        if(this.getTardis() != null)
            this.getTardis().markDirty();
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player) || this.getWorld() != TardisUtil.getTardisDimension())
            return;
        if (this.getTardis() != null && this.getTardis().getDoor().isOpen()) {
            if (!this.getTardis().getLockedTardis() && !PropertiesHandler.getBool(getTardis().getHandlers().getProperties(), PropertiesHandler.IS_FALLING)) {
                if (!DependencyChecker.hasPortals() || !this.getTardis().getExterior().getType().hasPortals())
                    TardisUtil.teleportOutside(this.getTardis(), player);
            }
        }
    }

    public void checkAnimations() {
        // DO NOT RUN THIS ON SERVER!!
        if(getTardis() == null) return;
        animationTimer++;

        if (getTardis().getHandlers().getDoor().getAnimationInteriorState() == null || !(getTardis().getHandlers().getDoor().getAnimationInteriorState().equals(getTardis().getDoor().getDoorState()))) {
            DOOR_STATE.start(animationTimer);
            getTardis().getHandlers().getDoor().tempInteriorState = getTardis().getDoor().getDoorState();
        }
    }

    public Tardis getTardis() {
        if (this.tardisId == null) {
            AITMod.LOGGER.warn("Door at " + this.getPos() + " is finding TARDIS!");
            this.findTardis();
        }

        if (isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(this.tardisId);
        }

        return ServerTardisManager.getInstance().getTardis(this.tardisId);
    }

    private void findTardis() {
        this.setTardis(TardisUtil.findTardisByInterior(pos));
    }

    public void setTardis(Tardis tardis) {
        if (tardis == null) {
            AITMod.LOGGER.error("Tardis was null in DoorBlockEntity at " + this.getPos());
            return;
        }

        this.tardisId = tardis.getUuid();
        // force re-link a desktop if it's not null
        this.linkDesktop();
    }

    public void setTardis(UUID uuid) {
        this.tardisId = uuid;

        this.linkDesktop();
    }

    public void linkDesktop() {
        if (this.getTardis() == null)
            return;
        if (this.getTardis() != null)
            this.setDesktop(this.getDesktop());
    }

    public TardisDesktop getDesktop() {
        return this.getTardis().getDesktop();
    }

    public void setDesktop(TardisDesktop desktop) {
        if (isClient()) return;

        desktop.setInteriorDoorPos(new AbsoluteBlockPos.Directed(
                this.pos, TardisUtil.getTardisDimension(), this.getFacing())
        );
    }
}