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
import java.util.Optional;
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
        if (this.getTardis().isPresent()) {
            this.setDesktop(this.getDesktop());
            if(this.getDesktop() != null) {
                this.getDesktop().setInteriorDoorPos(new AbsoluteBlockPos.Directed(pos, TardisUtil.getTardisDimension(), this.getFacing()));
                this.getDesktop().updateDoor();
            }
        }
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T door) {
        if(world.isClient()) {
            ((DoorBlockEntity) door).checkAnimations();
        }
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if (player == null || this.getTardis().isEmpty())
            return;
        if (this.getTardis().get().isGrowth() && this.getTardis().get().hasGrowthExterior())
            return;
        if (player.getMainHandStack().getItem() instanceof KeyItem && !getTardis().get().isSiegeMode()) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if (!tag.contains("tardis")) {
                return;
            }
            if (Objects.equals(this.getTardis().get().getUuid().toString(), tag.getString("tardis"))) {
                DoorData.toggleLock(this.getTardis().get(), (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return;
        }
        DoorData.useDoor(this.getTardis().get(), (ServerWorld) world, this.getPos(), (ServerPlayerEntity) player);
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
        super.writeNbt(nbt);
        if (this.tardisId != null) // panick
            nbt.putString("tardis", this.tardisId.toString());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("tardis")) {
            this.setTardis(UUID.fromString(nbt.getString("tardis")));
        }
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player) || this.getWorld() != TardisUtil.getTardisDimension())
            return;
        if (this.getTardis().isPresent() && this.getTardis().get().getDoor().isOpen()) {
            if (!this.getTardis().get().getLockedTardis() && !PropertiesHandler.getBool(getTardis().get().getHandlers().getProperties(), PropertiesHandler.IS_FALLING)) {
                if (!DependencyChecker.hasPortals() || !this.getTardis().get().getExterior().getType().hasPortals())
                    TardisUtil.teleportOutside(this.getTardis().get(), player);
            }
        }
    }

    public void checkAnimations() {
        // DO NOT RUN THIS ON SERVER!!
        if(getTardis().isEmpty()) return;
        animationTimer++;

        if (getTardis().get().getHandlers().getDoor().getAnimationInteriorState() == null || !(getTardis().get().getHandlers().getDoor().getAnimationInteriorState().equals(getTardis().get().getDoor().getDoorState()))) {
            DOOR_STATE.start(animationTimer);
            getTardis().get().getHandlers().getDoor().tempInteriorState = getTardis().get().getDoor().getDoorState();
        }
    }

    // oh god.
    public Optional<Tardis> getTardis() {
        if (this.tardisId == null)
            this.findTardis();

        if (TardisUtil.isClient()) { // todo replace deprecated check
            if (!ClientTardisManager.getInstance().hasTardis(this.tardisId)) {
                ClientTardisManager.getInstance().loadTardis(this.tardisId, tardis -> {});
                return Optional.empty();
                // todo add of `ifPresent()` of `isEmpty()` checks
                // eg if before it was PropertiesHandler.set(this.getTardis, ...)
                // it should become:
                // this.getTardis().ifPresent(tardis -> PropertiesHandler.set(tardis, ...))
                // or
                // if (this.getTardis().isEmpty()) return;
                //  because i dont want to rewrite a lot of the code base rn. this needs replacing badly but i am desperate for this release to come out and idc.
                // issues with doing it this way is that client will probably have to repeat things multiple times to get things to happen.
            }
            return Optional.of(ClientTardisManager.getInstance().getTardis(this.tardisId));
        }
        return Optional.ofNullable(ServerTardisManager.getInstance().getTardis(this.tardisId));
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
        if (this.getTardis().isEmpty())
            return;
        this.setDesktop(this.getDesktop());
    }

    public TardisDesktop getDesktop() {
        if(this.getTardis().isEmpty()) return null;
        return this.getTardis().get().getDesktop();
    }

    public void setDesktop(TardisDesktop desktop) {
        if (isClient()) return;

        desktop.setInteriorDoorPos(new AbsoluteBlockPos.Directed(
                this.pos, TardisUtil.getTardisDimension(), this.getFacing())
        );
    }
}