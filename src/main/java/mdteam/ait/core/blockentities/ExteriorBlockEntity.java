package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.item.SiegeTardisItem;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.tardis.exterior.CapsuleExterior;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.link.LinkableBlockEntity;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.*;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static mdteam.ait.tardis.TardisTravel.State.*;
import static mdteam.ait.tardis.util.TardisUtil.findTardisByPosition;
import static mdteam.ait.tardis.util.TardisUtil.isClient;

public class ExteriorBlockEntity extends LinkableBlockEntity implements BlockEntityTicker<ExteriorBlockEntity> { // fixme copy tardishandler and refactor to use uuids instead, this is incredibly inefficient and the main cause of lag.
    public int animationTimer = 0;
    public final AnimationState DOOR_STATE = new AnimationState();
    private ExteriorAnimation animation;

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(ServerWorld world, boolean sneaking, PlayerEntity player) {
        if (getTardis().isEmpty() || player == null)
            return;
        if (this.getTardis().get().isGrowth())
            return;

        if (player.getMainHandStack().getItem() instanceof KeyItem && !getTardis().get().isSiegeMode() && !getTardis().get().getHandlers().getInteriorChanger().isGenerating()) {
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

        if (sneaking && getTardis().get().isSiegeMode() && !getTardis().get().isSiegeBeingHeld()) {
            SiegeTardisItem.pickupTardis(getTardis().get(), (ServerPlayerEntity) player);
            return;
        }

        DoorData.useDoor(this.getTardis().get(), (ServerWorld) this.getWorld(), this.getPos(), (ServerPlayerEntity) player);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putFloat("alpha", this.getAlpha());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        //if (this.getAnimation() != null)
        //    this.getAnimation().setAlpha(nbt.getFloat("alpha"));
    }

    public void onEntityCollision(Entity entity) {
        if (this.getTardis().isPresent() && this.getTardis().get().getDoor().isOpen()) {
            if (!this.getTardis().get().getLockedTardis())
                if (!DependencyChecker.hasPortals() || !getTardis().get().getExterior().getType().hasPortals())
                    TardisUtil.teleportInside(this.getTardis().get(), entity);
        }
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState blockState, ExteriorBlockEntity blockEntity) {
        if(getTardis().isEmpty()) return;
        Random random = new Random();
        if (this.animation != null && this.getTardis().get().getTravel().getState() != LANDED)
            this.getAnimation().tick();

        if(world.isClient()) {
            this.checkAnimations();
        } else {
            if(getTardis().get().getHandlers().getInteriorChanger().isGenerating()) {
            world.addParticle(ParticleTypes.LARGE_SMOKE, true, pos.getX() + random.nextFloat(-0.25f, 0.25f), pos.getY() + 2.5,
                    pos.getZ() + random.nextFloat(-0.25f, 0.25f), random.nextFloat(-0.15f, 0.15f), 0.015, random.nextFloat(-0.15f, 0.15f));
            }
        }

        // Should be when tardis is set to landed / position is changed instead. fixme
        if (!world.isClient() && (blockState.getBlock() instanceof ExteriorBlock)) {
            // For checking falling
            ((ExteriorBlock) blockState.getBlock()).tryFall(blockState, (ServerWorld) world, pos);
        }

        if (!world.isClient() && !PropertiesHandler.getBool(this.getTardis().get().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED) && this.getTardis().get().getTravel().getState() == MAT && this.getAlpha() >= 0.9f) {
            for (ServerPlayerEntity entity : world.getEntitiesByClass(ServerPlayerEntity.class, new Box(this.getPos()).expand(0, 1, 0), EntityPredicates.EXCEPT_SPECTATOR)) {
                TardisUtil.teleportInside(this.getTardis().get(), entity); // fixme i dont like how this works you can just run into peoples tardises while theyre landing
            }
        }

        // ensures we dont exist during flight
        if (!world.isClient() && this.getTardis().get().getTravel().getState() == FLIGHT) {
            world.removeBlock(this.getPos(), false);
        }
    }

    // es caca
    public void verifyAnimation() {
        if (this.animation != null || this.getTardis().isEmpty() || this.getTardis().get().getExterior() == null)
            return;

        this.animation = this.getTardis().get().getExterior().getVariant().animation(this);
        AITMod.LOGGER.warn("Created new ANIMATION for " + this);
        this.animation.setupAnimation(this.getTardis().get().getTravel().getState());

        if (this.getWorld() != null) {
            if (!this.getWorld().isClient()) {
                this.animation.tellClientsToSetup(this.getTardis().get().getTravel().getState());
            }
        }
    }

    public void checkAnimations() {
    // DO NOT RUN THIS ON SERVER!!
        if(getTardis().isEmpty()) return;
        animationTimer++;
//        if (!DOOR_STATE.isRunning()) {
//            DOOR_STATE.startIfNotRunning(animationTimer);
//        }
        if(getTardis().get().getHandlers().getDoor().getAnimationExteriorState() == null) return;;
        if (getTardis().get().getHandlers().getDoor().getAnimationExteriorState() == null || !(getTardis().get().getHandlers().getDoor().getAnimationExteriorState().equals(getTardis().get().getDoor().getDoorState()))) {
            DOOR_STATE.start(animationTimer);
            getTardis().get().getHandlers().getDoor().tempExteriorState = getTardis().get().getDoor().getDoorState();
        }
    }

    @Override
    public Optional<Tardis> getTardis() {
        if(this.tardisId == null)
            findTardisFromPosition();
        return super.getTardis();
    }

    private void findTardisFromPosition() { // should only be used if tardisId is null so we can hopefully refind the tardis
        Tardis found = findTardisByPosition(new AbsoluteBlockPos(this.getPos(), this.getWorld()));

        if (found == null) return;

        this.tardisId = found.getUuid();
        markDirty();
    }

    public ExteriorAnimation getAnimation() {
        this.verifyAnimation();

        return this.animation;
    }

    public ExteriorSchema getExteriorType() {
        if(getTardis().isEmpty()) return ExteriorRegistry.REGISTRY.get(CapsuleExterior.REFERENCE);
        return this.getTardis().get().getExterior().getType();
    }

    public float getAlpha() {
        if (this.getAnimation() == null) {
            return 1f;
        }

        return this.getAnimation().getAlpha();
    }

    public void onBroken() {
        if(this.getTardis().isPresent())
            this.getTardis().get().getTravel().setState(TardisTravel.State.FLIGHT);
    }
}