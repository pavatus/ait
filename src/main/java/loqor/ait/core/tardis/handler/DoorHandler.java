package loqor.ait.core.tardis.handler;

import net.fabricmc.fabric.api.util.TriState;
import org.jetbrains.annotations.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.AITSounds;
import loqor.ait.core.entities.ConsoleControlEntity;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.DirectedBlockPos;
import loqor.ait.data.Exclude;
import loqor.ait.data.properties.Property;
import loqor.ait.data.properties.Value;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;
import loqor.ait.data.schema.door.DoorSchema;

public class DoorHandler extends KeyedTardisComponent implements TardisTickable {

    private static final BoolProperty LOCKED_DOORS = new BoolProperty("locked");
    private static final BoolProperty PREVIOUSLY_LOCKED = new BoolProperty("previously_locked");
    private static final BoolProperty DEADLOCKED = new BoolProperty("deadlocked");

    private static final Property<DoorState> DOOR_STATE = Property.forEnum("door_state", DoorState.class, DoorState.CLOSED);
    private static final Property<AnimationDoorState> TEMP_EXTERIOR_STATE = Property.forEnum("temp_interior_state", AnimationDoorState.class, AnimationDoorState.CLOSED);
    private static final Property<AnimationDoorState> TEMP_INTERIOR_STATE = Property.forEnum("temp_exterior_state", AnimationDoorState.class, AnimationDoorState.CLOSED);

    private final BoolValue locked = LOCKED_DOORS.create(this);
    private final BoolValue previouslyLocked = PREVIOUSLY_LOCKED.create(this);
    private final BoolValue deadlocked = DEADLOCKED.create(this);

    private final Value<DoorState> doorState = DOOR_STATE.create(this);

    /*
     this is the previous state before it was changed, used for
     checking when the door has been changed so the animation can start.
      Set on server, used on client
     */
    @Exclude(strategy = Exclude.Strategy.FILE)
    public final Value<AnimationDoorState> tempExteriorState = TEMP_EXTERIOR_STATE.create(this);

    @Exclude(strategy = Exclude.Strategy.FILE)
    private final Value<AnimationDoorState> tempInteriorState = TEMP_INTERIOR_STATE.create(this);

    static {
        TardisEvents.DEMAT.register(tardis -> tardis.door().isOpen() ? TardisEvents.Interaction.FAIL : TardisEvents.Interaction.PASS);
    }

    public DoorHandler() {
        super(Id.DOOR);
    }

    @Override
    public void onLoaded() {
        locked.of(this, LOCKED_DOORS);
        previouslyLocked.of(this, PREVIOUSLY_LOCKED);
        deadlocked.of(this, DEADLOCKED);

        doorState.of(this, DOOR_STATE);
        tempExteriorState.of(this, TEMP_EXTERIOR_STATE);
        tempInteriorState.of(this, TEMP_INTERIOR_STATE);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (this.shouldSucc())
            this.succ();
    }

    /**
     * Moves entities in the Tardis interior towards the door.
     */
    private void succ() {
        // Get all entities in the Tardis interior
        TardisUtil.getLivingEntitiesInInterior(tardis.asServer()).stream()
                .filter(entity -> !(entity instanceof ConsoleControlEntity)) // Exclude control entities
                .filter(entity -> !(entity instanceof ServerPlayerEntity && entity.isSpectator())) // Exclude spectators
                .forEach(entity -> {
                    // Calculate the motion vector away from the door

                    DirectedBlockPos directed = tardis.getDesktop().getDoorPos();
                    BlockPos pos = directed.getPos();

                    Vec3d motion = pos
                            .toCenterPos().subtract(entity.getPos()).normalize().multiply(0.05);

                    // Apply the motion to the entity
                    entity.setVelocity(entity.getVelocity().add(motion));
                    entity.velocityDirty = true;
                    entity.velocityModified = true;
                });
    }

    private boolean shouldSucc() {
        DirectedBlockPos directed = tardis.getDesktop().getDoorPos();

        if (directed == null)
            return false;

        return !tardis.travel().isLanded() && this.isOpen()
                && !tardis.areShieldsActive() && !tardis.travel().autopilot();
    }

    public boolean isRightOpen() {
        return this.doorState.get() == DoorState.BOTH;
    }

    public boolean isLeftOpen() {
        return this.doorState.get() == DoorState.HALF || this.doorState.get() == DoorState.BOTH;
    }

    public void setDeadlocked(boolean deadlocked) {
        this.deadlocked.set(deadlocked);
    }

    public void setLocked(boolean locked) {
        this.locked.set(locked);

        if (locked)
            this.setDoorState(DoorState.CLOSED);
    }

    public boolean locked() {
        return this.locked.get() || this.deadlocked.get();
    }

    public boolean hasDoubleDoor() {
        return this.tardis.getExterior().getVariant().door().isDouble();
    }

    public boolean isOpen() {
        return this.doorState.get() != DoorState.CLOSED;
    }

    public boolean isClosed() {
        return this.doorState.get() == DoorState.CLOSED;
    }

    public boolean isBothOpen() {
        return this.doorState.get() == DoorState.BOTH;
    }

    public void openDoors() {
        this.setDoorState(DoorState.HALF);

        if (this.hasDoubleDoor())
            this.setDoorState(DoorState.BOTH);
    }

    public void closeDoors() {
        this.setDoorState(DoorState.CLOSED);
    }

    private void setDoorState(DoorState newState) {
        if (this.locked() && newState != DoorState.CLOSED)
            return;

        DoorState oldState = this.doorState.get();

        if (oldState != newState) {
            AnimationDoorState animState = AnimationDoorState.match(newState, oldState);

            this.tempExteriorState.set(animState);
            this.tempInteriorState.set(animState);

            if (oldState == DoorState.CLOSED)
                TardisEvents.DOOR_OPEN.invoker().onOpen(tardis());

            if (newState == DoorState.CLOSED)
                TardisEvents.DOOR_CLOSE.invoker().onClose(tardis());
        }

        this.doorState.set(newState);
    }

    public DoorState getDoorState() {
        return doorState.get();
    }

    public boolean interact(ServerWorld world, @Nullable BlockPos pos, @Nullable ServerPlayerEntity player) {
        ServerWorld interior = tardis.asServer().getInteriorWorld();
        InteractionResult result = TardisEvents.USE_DOOR.invoker().onUseDoor(tardis, interior, world, player, pos);

        if (result == InteractionResult.KNOCK) {
            if (pos != null && world != interior)
                world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f,
                        world.getRandom().nextBoolean() ? 0.5f : 0.3f);

            interior.playSound(null, tardis.getDesktop().getDoorPos().getPos(), AITSounds.KNOCK,
                    SoundCategory.BLOCKS, 3f, world.getRandom().nextBoolean() ? 0.5f : 0.3f);
        }

        if (result == InteractionResult.BANG) {
            if (pos != null && world != interior)
                world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS, 1f,
                        1f);

            interior.playSound(null, tardis.getDesktop().getDoorPos().getPos(),
                    SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS);
        }

        if (result.returns != TriState.DEFAULT)
            return result.returns.get();

        if (this.locked()) {
            if (player != null && pos != null) {
                player.sendMessage(Text.literal("\uD83D\uDD12"), true);

                world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f,
                        world.getRandom().nextBoolean() ? 0.5f : 0.3f);

                interior.playSound(null, tardis.getDesktop().getDoorPos().getPos(), AITSounds.KNOCK,
                        SoundCategory.BLOCKS, 3f, world.getRandom().nextBoolean() ? 0.5f : 0.3f);
            }

            return false;
        }

        DoorSchema doorSchema = tardis.getExterior().getVariant().door();
        SoundEvent sound = doorSchema.isDouble() && this.isBothOpen()
                ? doorSchema.closeSound()
                : doorSchema.openSound();

        tardis.travel().position().getWorld().playSound(null, tardis.travel().position().getPos(), sound,
                SoundCategory.BLOCKS, 0.6F, world.getRandom().nextBoolean() ? 1f : 0.8f);

        interior.playSound(null, tardis.getDesktop().getDoorPos().getPos(), sound,
                SoundCategory.BLOCKS, 0.6F, world.getRandom().nextBoolean() ? 1f : 0.8f);

        if (player.isSneaking()) {
            if (this.isOpen()) {
                this.closeDoors();
            } else {
                this.openDoors();
            }

            return true;
        }

        this.setDoorState(this.getDoorState().next(doorSchema.isDouble()));
        return true;
    }

    public boolean interactToggleLock(@Nullable ServerPlayerEntity player) {
        return this.interactToggleLock(player, false);
    }

    public boolean interactToggleLock(@Nullable ServerPlayerEntity player, boolean forced) {
        return this.interactLock(!this.locked(), player, forced);
    }

    public boolean interactLock(boolean lock, @Nullable ServerPlayerEntity player, boolean forced) {
        if (this.locked() == lock)
            return true;

        if (!forced && (tardis.travel().getState() == TravelHandlerBase.State.DEMAT
                || tardis.travel().getState() == TravelHandlerBase.State.MAT))
            return false;

        this.setLocked(lock);
        this.setDoorState(DoorState.CLOSED);

        if (!forced)
            this.previouslyLocked().set(locked);

        String lockedState = tardis.door().locked() ? "\uD83D\uDD12" : "\uD83D\uDD13";

        if (player != null)
            player.sendMessage(Text.literal(lockedState), true);

        tardis.travel().position().getWorld().playSound(null, tardis.travel().position().getPos(),
                SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);

        tardis.asServer().getInteriorWorld().playSound(null, tardis.getDesktop().getDoorPos().getPos(),
                SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);

        return true;
    }

    public BoolValue previouslyLocked() {
        return this.previouslyLocked;
    }

    public enum InteractionResult {
        /**
         * Same as returning {@literal false} in {@link DoorHandler#interact(ServerWorld, BlockPos, ServerPlayerEntity)}
         */
        CANCEL(TriState.FALSE),
        /**
         * Continues the execution.
         */
        CONTINUE(TriState.DEFAULT),
        /**
         * Same as returning {@literal false} in {@link DoorHandler#interact(ServerWorld, BlockPos, ServerPlayerEntity)},
         * but also play knocking sound.
         */
        KNOCK(TriState.FALSE),
        /**
         * Same as returning {@literal false} in {@link DoorHandler#interact(ServerWorld, BlockPos, ServerPlayerEntity)},
         * but also play door banging sound.
         */
        BANG(TriState.FALSE),
        /**
         * Same as returning {@literal true} in {@link DoorHandler#interact(ServerWorld, BlockPos, ServerPlayerEntity)}
         */
        SUCCESS(TriState.TRUE);

        private final TriState returns;

        InteractionResult(TriState returns) {
            this.returns = returns;
        }

        public TriState result() {
            return returns;
        }
    }

    public enum DoorState {
        CLOSED,
        HALF,
        BOTH;

        public DoorState next(boolean isDouble) {
            return switch (this) {
                case CLOSED -> HALF;
                case HALF -> isDouble ? BOTH : HALF;
                case BOTH -> CLOSED;
            };
        }
    }

    public enum AnimationDoorState {
        CLOSED,
        FIRST,
        SECOND,
        BOTH;

        public boolean is(DoorState doorState) {
            if (this == CLOSED && doorState == DoorState.CLOSED)
                return true;

            if (this == FIRST && doorState == DoorState.HALF)
                return true;

            return (this == BOTH || this == SECOND) && doorState == DoorState.BOTH;
        }

        public static AnimationDoorState match(DoorState state) {
            return switch (state) {
                case BOTH -> AnimationDoorState.BOTH;
                case HALF -> AnimationDoorState.FIRST;
                case CLOSED -> AnimationDoorState.CLOSED;
            };
        }

        public static AnimationDoorState match(DoorState newState, DoorState oldState) {
            AnimationDoorState animState = null;

            if (oldState == DoorState.HALF && newState == DoorState.BOTH)
                animState = AnimationDoorState.SECOND;

            if (oldState == DoorState.CLOSED && newState == DoorState.BOTH)
                animState = AnimationDoorState.BOTH;

            if (oldState == DoorState.BOTH && newState == DoorState.CLOSED)
                animState = AnimationDoorState.CLOSED;

            if (oldState == DoorState.CLOSED && newState == DoorState.HALF)
                animState = AnimationDoorState.FIRST;

            return animState;
        }
    }
}
