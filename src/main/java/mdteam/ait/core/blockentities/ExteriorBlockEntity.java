package mdteam.ait.core.blockentities;

import mdteam.ait.core.item.SonicItem;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.item.SiegeTardisItem;
import mdteam.ait.registry.CategoryRegistry;
import mdteam.ait.tardis.data.SonicHandler;
import mdteam.ait.tardis.exterior.category.CapsuleCategory;
import mdteam.ait.tardis.exterior.category.ExteriorCategorySchema;
import mdteam.ait.tardis.link.LinkableBlockEntity;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.*;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static mdteam.ait.tardis.TardisTravel.State.*;
import static mdteam.ait.tardis.util.TardisUtil.findTardisByPosition;

public class ExteriorBlockEntity extends LinkableBlockEntity implements BlockEntityTicker<ExteriorBlockEntity> { // fixme copy tardishandler and refactor to use uuids instead, this is incredibly inefficient and the main cause of lag.
    public int animationTimer = 0;
    public final AnimationState DOOR_STATE = new AnimationState();
    private ExteriorAnimation animation;

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(ServerWorld world, boolean sneaking, PlayerEntity player) {
        if (findTardis().isEmpty() || player == null)
            return;
        if (this.findTardis().get().isGrowth())
            return;

        SonicHandler handler = this.findTardis().get().getHandlers().getSonic();
        boolean hasSonic = handler.hasSonic(SonicHandler.HAS_EXTERIOR_SONIC);
        boolean shouldEject = player.isSneaking();

        if (player.getMainHandStack().getItem() instanceof KeyItem && !findTardis().get().isSiegeMode() && !findTardis().get().getHandlers().getInteriorChanger().isGenerating() && this.findTardis().get().getHandlers().getCrashData().getRepairTicks() > 0) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if (!tag.contains("tardis")) {
                return;
            }
            if (Objects.equals(this.findTardis().get().getUuid().toString(), tag.getString("tardis"))) {
                DoorData.toggleLock(this.findTardis().get(), (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return;
        }

        if (hasSonic) {
            if(shouldEject) {
                player.giveItemStack(handler.get(SonicHandler.HAS_EXTERIOR_SONIC));
                handler.clear(false, SonicHandler.HAS_EXTERIOR_SONIC);
                handler.clearSonicMark(SonicHandler.HAS_EXTERIOR_SONIC);
                world.playSound(null, pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                return;
            }
            player.sendMessage(Text.literal("Repairing: " + this.findTardis().get().getHandlers().getCrashData().getRepairTicks()).formatted(Formatting.BOLD, Formatting.GOLD), true);
            return;
        }

        if (player.getMainHandStack().getItem() instanceof SonicItem && !findTardis().get().isSiegeMode() && !findTardis().get().getHandlers().getInteriorChanger().isGenerating()) {
            ItemStack sonic = player.getMainHandStack();
            NbtCompound tag = sonic.getOrCreateNbt();
            if (!tag.contains("tardis")) {
                return;
            }
            if (Objects.equals(this.findTardis().get().getUuid().toString(), tag.getString("tardis"))) {

                ItemStack stack = player.getMainHandStack();

                if (!(stack.getItem() instanceof SonicItem)) return;

                handler.set(stack, true, SonicHandler.HAS_EXTERIOR_SONIC);
                handler.markHasSonic(SonicHandler.HAS_EXTERIOR_SONIC);
                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                world.playSound(null, pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1F, 0.2F);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("Unable to repair TARDIS with current tool!"), true);
            }
            return;
        }

        if (sneaking && findTardis().get().isSiegeMode() && !findTardis().get().isSiegeBeingHeld()) {
            SiegeTardisItem.pickupTardis(findTardis().get(), (ServerPlayerEntity) player);
            return;
        }

        if((findTardis().get().getTravel().getState() == LANDED || findTardis().get().getTravel().getState() == CRASH)) {
            DoorData.useDoor(this.findTardis().get(), (ServerWorld) this.getWorld(), this.getPos(), (ServerPlayerEntity) player);
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    public void onEntityCollision(Entity entity) {
        if (this.findTardis().isPresent() && this.findTardis().get().getDoor().isOpen()) {
            if (!this.findTardis().get().getLockedTardis())
                if (!DependencyChecker.hasPortals() || !findTardis().get().getExterior().getVariant().hasPortals())
                    TardisUtil.teleportInside(this.findTardis().get(), entity);
        }
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState blockState, ExteriorBlockEntity blockEntity) {
        if(findTardis().isEmpty()) return;
        Random random = new Random();
        if (this.animation != null && this.findTardis().get().getTravel().getState() != LANDED)
            this.getAnimation().tick();

        if(world.isClient()) {
            this.checkAnimations();
        }
        /*if(getTardis().get().getHandlers().getInteriorChanger().isGenerating()) {
        world.addParticle(ParticleTypes.LARGE_SMOKE, true, pos.getX(), pos.getY() + 2.5,
                pos.getZ(), random.nextFloat(-0.15f, 0.15f), 0.015, random.nextFloat(-0.15f, 0.15f));
        }*/

        // Should be when tardis is set to landed / position is changed instead. fixme
        if (!world.isClient() && (blockState.getBlock() instanceof ExteriorBlock)) {
            // For checking falling
            ((ExteriorBlock) blockState.getBlock()).tryFall(blockState, (ServerWorld) world, pos);
        }

        if (!world.isClient() && !PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED) && this.findTardis().get().getTravel().getState() == MAT && this.getAlpha() >= 0.9f) {
            for (ServerPlayerEntity entity : world.getEntitiesByClass(ServerPlayerEntity.class, new Box(this.getPos()).expand(0, 1, 0), EntityPredicates.EXCEPT_SPECTATOR)) {
                TardisUtil.teleportInside(this.findTardis().get(), entity); // fixme i dont like how this works you can just run into peoples tardises while theyre landing
            }
        }

        // ensures we dont exist during flight
        if (!world.isClient() && this.findTardis().get().getTravel().getState() == FLIGHT) {
            world.removeBlock(this.getPos(), false);
        }
    }

    // es caca
    public void verifyAnimation() {
        if (this.animation != null || this.findTardis().isEmpty() || this.findTardis().get().getExterior() == null)
            return;

        this.animation = this.findTardis().get().getExterior().getVariant().animation(this);

        // Removed because of unnecessary spam, again. - Loqor
        //AITMod.LOGGER.warn("Created new ANIMATION for " + this);
        this.animation.setupAnimation(this.findTardis().get().getTravel().getState());

        if (this.getWorld() != null) {
            if (!this.getWorld().isClient()) {
                this.animation.tellClientsToSetup(this.findTardis().get().getTravel().getState());
            }
        }
    }

    public void checkAnimations() {
    // DO NOT RUN THIS ON SERVER!!
        if(findTardis().isEmpty()) return;
        animationTimer++;
//        if (!DOOR_STATE.isRunning()) {
//            DOOR_STATE.startIfNotRunning(animationTimer);
//        }
        if(findTardis().get().getHandlers().getDoor().getAnimationExteriorState() == null) return;;
        if (findTardis().get().getHandlers().getDoor().getAnimationExteriorState() == null || !(findTardis().get().getHandlers().getDoor().getAnimationExteriorState().equals(findTardis().get().getDoor().getDoorState()))) {
            DOOR_STATE.start(animationTimer);
            findTardis().get().getHandlers().getDoor().tempExteriorState = findTardis().get().getDoor().getDoorState();
        }
    }

    @Override
    public Optional<Tardis> findTardis() {
        if(this.tardisId == null)
            findTardisFromPosition();
        return super.findTardis();
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

    public ExteriorCategorySchema getExteriorType() {
        if(findTardis().isEmpty()) return CategoryRegistry.getInstance().get(CapsuleCategory.REFERENCE);
        return this.findTardis().get().getExterior().getCategory();
    }

    public float getAlpha() {
        if (this.getAnimation() == null) {
            return 1f;
        }

        return this.getAnimation().getAlpha();
    }

    public void onBroken() {
        if(this.findTardis().isPresent())
            this.findTardis().get().getTravel().setState(TardisTravel.State.FLIGHT);
    }
}