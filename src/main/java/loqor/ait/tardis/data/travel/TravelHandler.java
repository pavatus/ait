package loqor.ait.tardis.data.travel;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.data.*;
import loqor.ait.tardis.util.NetworkUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public final class TravelHandler extends AnimatedTravelHandler implements CrashableTardisTravel {

    public static final Identifier CANCEL_DEMAT_SOUND = new Identifier(AITMod.MOD_ID, "cancel_demat_sound");

    public TravelHandler() {
        super(Id.TRAVEL);
    }

    @Override
    public void speed(int value) {
        super.speed(value);
        this.tryFly();
    }

    @Override
    public void handbrake(boolean value) {
        super.handbrake(value);

        if (this.getState() == TravelHandlerBase.State.DEMAT && value) {
            this.cancelDemat();
            return;
        }

        this.tryFly();
    }

    private void tryFly() {
        int speed = this.speed();

        if (speed > 0 && this.getState() == State.LANDED && !this.handbrake()
                && !this.tardis.sonic().hasExteriorSonic()) {
            this.dematerialize();
            return;
        }

        if (speed != 0 || this.getState() != State.FLIGHT)
            return;

        if (this.tardis.crash().getState() == TardisCrashHandler.State.UNSTABLE)
            this.destination(cached -> TravelUtil.jukePos(cached, 1, 10));

        if (!this.tardis.flight().isActive())
            this.rematerialize();
    }

    @Override
    protected void onEarlyInit(InitContext ctx) {
        if (ctx.created() && ctx.pos() != null)
            this.initPos(ctx.pos());
    }

    @Override
    public void postInit(InitContext context) {
        if (this.isServer() && context.created())
            this.placeExterior(true);
    }

    public void deleteExterior() {
        DirectedGlobalPos.Cached globalPos = this.position.get();

        ServerWorld world = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        world.removeBlock(pos, false);

        ForcedChunkUtil.stopForceLoading(world, pos);
    }

    /**
     * Places an exterior, animates it if `animate` is true and schedules a block update.
     */
    public ExteriorBlockEntity placeExterior(boolean animate) {
        return placeExterior(animate, true);
    }

    public ExteriorBlockEntity placeExterior(boolean animate, boolean schedule) {
        return placeExterior(this.position(), animate, schedule);
    }

    private ExteriorBlockEntity placeExterior(DirectedGlobalPos.Cached globalPos, boolean animate, boolean schedule) {
        ServerWorld world = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        boolean hasPower = this.tardis.engine().hasPower();

        BlockState blockState = AITBlocks.EXTERIOR_BLOCK.getDefaultState().with(
                ExteriorBlock.ROTATION, (int) DirectionControl.getGeneralizedRotation(globalPos.getRotation())
        ).with(ExteriorBlock.LEVEL_9, hasPower ? 9 : 0);

        world.setBlockState(pos, blockState);

        ExteriorBlockEntity exterior = new ExteriorBlockEntity(pos, blockState, this.tardis);
        world.addBlockEntity(exterior);

        if (animate)
            this.runAnimations(exterior);

        BiomeHandler biome = this.tardis.handler(Id.BIOME);
        biome.update();

        if (schedule && !this.antigravs.get())
            world.scheduleBlockTick(pos, AITBlocks.EXTERIOR_BLOCK, 2);

        ForcedChunkUtil.keepChunkLoaded(world, pos);
        return exterior;
    }

    public void immediatelyLandHere(DirectedGlobalPos.Cached globalPos) {
        this.deleteExterior();
        this.state.set(TravelHandlerBase.State.LANDED);

        this.forceDestination(globalPos);
        this.forcePosition(globalPos);

        this.placeExterior(false);
        this.finishRemat();
    }

    private void runAnimations(ExteriorBlockEntity exterior) {
        State state = this.getState();
        ExteriorAnimation animation = exterior.getAnimation();

        if (animation == null) {
            AITMod.LOGGER.info("Null animation for exterior at {}", exterior.getPos());
            return;
        }

        animation.setupAnimation(state);
    }

    public void runAnimations() {
        DirectedGlobalPos.Cached globalPos = this.position();

        ServerWorld level = globalPos.getWorld();
        BlockEntity entity = level.getBlockEntity(globalPos.getPos());

        if (entity instanceof ExteriorBlockEntity exterior)
            this.runAnimations(exterior);
    }

    /**
     * Sets the current position to the destination progress one.
     */
    public void stopHere() {
        if (this.getState() != State.FLIGHT)
            return;

        this.forcePosition(this.getProgress());
    }

    public void dematerialize() {
        if (this.getState() != State.LANDED)
            return;

        if (!this.tardis.engine().hasPower())
            return;

        if (this.autopilot()) {
            // fulfill all the prerequisites
            this.tardis.door().closeDoors();
            this.tardis.setRefueling(false);

            if (this.speed() == 0)
                this.increaseSpeed();
        }

        if (TardisEvents.DEMAT.invoker().onDemat(this.tardis) == TardisEvents.Interaction.FAIL
                || tardis.door().isOpen() || tardis.isRefueling() || TravelUtil.dematCooldown(this.tardis)
                || tardis.<RealFlightHandler>handler(TardisComponent.Id.FLIGHT).falling().get()
        ) {
            this.failDemat();
            return;
        }

        this.forceDemat();
    }

    private void failDemat() {
        // demat will be cancelled
        this.position().getWorld().playSound(null, this.position().getPos(),
                AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f
        );

        this.tardis.getDesktop().playSoundAtEveryConsole(AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f);
        TravelUtil.runDematCooldown(this.tardis);
    }

    private void failRemat() {
        // Play failure sound at the current position
        this.position().getWorld().playSound(null, this.position().getPos(),
                AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f
        );

        // Play failure sound at the Tardis console position if the interior is not empty
        this.tardis.getDesktop().playSoundAtEveryConsole(AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f);

        // Create materialization delay and return
        TravelUtil.runMatCooldown(this.tardis);
    }

    public void forceDemat() {
        this.state.set(State.DEMAT);
        SoundEvent sound = this.getState().effect().sound();

        // Play materialize sound at the position
        this.position().getWorld().playSound(null,
                this.position().getPos(), sound, SoundCategory.BLOCKS
        );

        this.tardis.getDesktop().playSoundAtEveryConsole(sound, SoundCategory.BLOCKS, 10f, 1f);
        this.runAnimations();

        this.startFlight();
    }

    public void finishDemat() {
        this.crashing.set(false);
        this.previousPosition.set(this.position);
        this.state.set(State.FLIGHT);

        this.deleteExterior();

        if (tardis.stats().security().get())
            SecurityControl.runSecurityProtocols(this.tardis());
    }

    public void cancelDemat() {
        if (this.getState() != State.DEMAT)
            return;

        this.finishRemat();

        this.position().getWorld().playSound(null, this.position().getPos(), AITSounds.LAND_THUD, SoundCategory.AMBIENT);
        this.tardis.getDesktop().playSoundAtEveryConsole(AITSounds.LAND_THUD, SoundCategory.AMBIENT);

        NetworkUtil.sendToInterior(this.tardis(), CANCEL_DEMAT_SOUND, PacketByteBufs.empty());
    }

    public void rematerialize() {
        if (TardisEvents.MAT.invoker().onMat(tardis) == TardisEvents.Interaction.FAIL || TravelUtil.matCooldownn(tardis)) {
            this.failRemat();
            return;
        }

        this.forceRemat();
    }

    public void forceRemat() {
        if (this.getState() != State.FLIGHT)
            return;

        if (this.tardis.sequence().hasActiveSequence()) {
            this.tardis.sequence().setActiveSequence(null, true);
        }

        this.state.set(State.MAT);
        SoundEvent sound = this.getState().effect().sound();

        if (this.isCrashing())
            sound = AITSounds.EMERG_MAT;

        this.destination(this.getProgress());
        this.forcePosition(this.destination());

        // Play materialize sound at the destination
        this.position().getWorld().playSound(null,
                this.position().getPos(), sound, SoundCategory.BLOCKS
        );

        this.tardis.getDesktop().playSoundAtEveryConsole(sound, SoundCategory.BLOCKS, 10f, 1f);
        this.placeExterior(true, false); // we schedule block update in #finishRemat
    }

    public void finishRemat() {
        if (this.autopilot() && this.speed.get() > 0)
            this.speed.set(0);

        this.state.set(State.LANDED);
        this.resetFlight();

        this.position().getWorld().scheduleBlockTick(this.position().getPos(), AITBlocks.EXTERIOR_BLOCK, 2);
        DoorHandler.lockTardis(tardis.<DoorHandler>handler(Id.DOOR).previouslyLocked().get(), this.tardis, null, false);
        TardisEvents.LANDED.invoker().onLanded(this.tardis);
    }

    public void initPos(DirectedGlobalPos.Cached cached) {
        cached.init(TravelHandlerBase.server());

        if (this.position.get() == null)
            this.position.set(cached);

        if (this.destination.get() == null)
            this.destination.set(cached);

        if (this.previousPosition.get() == null)
            this.previousPosition.set(cached);
    }

    public boolean isLanded() {
        return this.getState() == State.LANDED;
    }

    public boolean inFlight() {
        return this.getState() == State.FLIGHT;
    }
}
