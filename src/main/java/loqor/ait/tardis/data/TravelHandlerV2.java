package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.CrashableTardisTravel;
import loqor.ait.tardis.data.travel.ProgressiveTravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.data.travel.TravelUtil;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.NetworkUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TravelHandlerV2 extends ProgressiveTravelHandler implements CrashableTardisTravel {

    public static final Identifier CANCEL_DEMAT_SOUND = new Identifier(AITMod.MOD_ID, "cancel_demat_sound");

    private int animationTicks;

    public TravelHandlerV2() {
        super(Id.TRAVEL2);
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
        State state = this.getState();

        if (state.animated())
            this.tickAnimationProgress(state);
    }

    @Override
    protected void speed(int value) {
        super.speed(value);
        this.tryFly();
    }

    @Override
    public void handbrake(boolean value) {
        super.handbrake(value);
        this.tryFly();
    }

    private void tryFly() {
        int speed = this.speed.get();

        if (speed > 0 && this.getState() == State.LANDED && !this.handbrake() && !tardis.sonic().hasSonic(SonicHandler.HAS_EXTERIOR_SONIC))
            this.dematerialize();

        if (speed != 0 || this.getState() != State.FLIGHT)
            return;

        if (tardis.crash().getState() == TardisCrashData.State.UNSTABLE)
            this.destination(cached -> TravelUtil.jukePos(cached, 1, 10, 1));

        if (!PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.IS_IN_REAL_FLIGHT))
            this.rematerialize();
    }

    private void tickAnimationProgress(State state) {
        if (this.animationTicks++ < state.effect().length())
            return;

        this.animationTicks = 0;
        state.finish(this);
    }

    @Override
    protected void onEarlyInit(InitContext ctx) {
        if (ctx.created() && ctx.pos() != null)
            this.initPos(ctx.pos());
    }

    @Override
    public void postInit(InitContext context) {
        if (this.isServer() && context.created())
            this.placeAndAnimate();
    }

    public void deleteExterior() {
        DirectedGlobalPos.Cached globalPos = this.position.get();

        ServerWorld world = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        world.removeBlock(pos, false);

        ForcedChunkUtil.stopForceLoading(world, pos);
    }

    public ExteriorBlockEntity placeAndAnimate() {
        return placeExterior(true);
    }

    public ExteriorBlockEntity placeExterior(boolean animate) {
        return placeExterior(this.position(), animate);
    }

    private ExteriorBlockEntity placeExterior(DirectedGlobalPos.Cached globalPos, boolean animate) {
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

        BiomeHandler biome = this.tardis.getHandlers().get(Id.BIOME);
        biome.update();

        ForcedChunkUtil.keepChunkLoaded(world, pos);
        return exterior;
    }

    public void immediatelyLandHere(DirectedGlobalPos.Cached globalPos) {
        this.deleteExterior();
        this.state.set(TravelHandlerBase.State.LANDED);

        this.placeExterior(false);
        this.finishRemat();

        this.destination.set(globalPos);
        this.position.set(globalPos);
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

        DirectedGlobalPos.Cached pos = FlightUtil.getPositionFromPercentage(
                this.position(), this.destination(),
                this.tardis().travel2().getDurationAsPercentage()
        );

        this.forcePosition(pos);
    }

    public void dematerialize() {
        if (this.getState() != State.LANDED)
            return;

        if (!this.tardis.engine().hasPower())
            return;

        if (TardisEvents.DEMAT.invoker().onDemat(this.tardis)
                || tardis.door().isOpen() || tardis.isRefueling()
                || FlightUtil.isDematerialiseOnCooldown(this.tardis)
                || PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.IS_FALLING)
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
        FlightUtil.createDematerialiseDelay(this.tardis);
    }

    private void failRemat() {
        // Play failure sound at the current position
        this.position().getWorld().playSound(null, this.position().getPos(),
                AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f
        );

        // Play failure sound at the Tardis console position if the interior is not empty
        this.tardis.getDesktop().playSoundAtEveryConsole(AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f);

        // Create materialization delay and return
        FlightUtil.createMaterialiseDelay(this.tardis);
    }

    public void forceDemat() {
        if (this.autopilot()) {
            // fulfill all the prerequisites
            this.tardis.door().closeDoors();
            this.tardis.setRefueling(false);

            if (this.tardis.travel2().speed().get() == 0)
                this.increaseSpeed();
        }

        this.state.set(State.DEMAT);
        SoundEvent sound = this.getState().effect().sound();

        // Play materialize sound at the position
        this.position().getWorld().playSound(null,
                this.position().getPos(), sound, SoundCategory.BLOCKS
        );

        this.tardis.getDesktop().playSoundAtEveryConsole(sound, SoundCategory.BLOCKS, 10f, 1f);
        this.runAnimations();
    }

    public void finishDemat() {
        this.crashing.set(false);
        this.previousPosition.set(this.position);
        this.state.set(State.FLIGHT);

        this.deleteExterior();

        if (PropertiesHandler.getBool(this.tardis().properties(), SecurityControl.SECURITY_KEY))
            SecurityControl.runSecurityProtocols(this.tardis());

        this.startFlight();
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
        boolean bypass = tardis.hasGrowthExterior();
        this.destination(FlightUtil.getPositionFromPercentage(this.position(), this.destination(),
                tardis.travel2().getDurationAsPercentage()));

        if (bypass) {
            this.forceRemat();
            return;
        }

        if (TardisEvents.MAT.invoker().onMat(tardis) || FlightUtil.isMaterialiseOnCooldown(tardis))
            this.failRemat();
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

        this.position.set(this.destination());

        // Play materialize sound at the destination
        this.position().getWorld().playSound(null,
                this.position().getPos(), sound, SoundCategory.BLOCKS
        );

        this.tardis.getDesktop().playSoundAtEveryConsole(sound, SoundCategory.BLOCKS, 10f, 1f);
        this.placeAndAnimate();
    }

    public void finishRemat() {
        if (this.autopilot.get() && this.speed.get() > 0)
            this.speed.set(0);

        AITMod.LOGGER.info("Finish remat called", new Throwable());

        this.state.set(State.LANDED);
        this.resetFlight();

        DoorData.lockTardis(PropertiesHandler.getBool(this.tardis.properties(), PropertiesHandler.PREVIOUSLY_LOCKED), this.tardis, null, false);
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

    @Override
    protected DirectedGlobalPos.Cached checkDestination(DirectedGlobalPos.Cached destination, int limit, boolean fullCheck) {
        ServerWorld world = destination.getWorld();
        BlockPos.Mutable temp = destination.getPos().mutableCopy();

        destination = destination.pos(temp.getX(), MathHelper.clamp(
                temp.getY(), world.getBottomY(), world.getTopY() - 1
        ), temp.getZ());

        BlockState current;
        BlockState top;
        BlockState ground;

        if (fullCheck) {
            for (int i = 0; i < limit; i++) {
                current = world.getBlockState(temp);
                top = world.getBlockState(temp.up());
                ground = world.getBlockState(temp.down());

                if (isReplaceable(current, top) && !isReplaceable(ground)) // check two blocks cus tardis is two blocks tall yk and check for grond
                    return destination.pos(temp);

                temp = temp.down().mutableCopy();
            }
        }

        temp = temp.mutableCopy();

        current = world.getBlockState(temp);
        top = world.getBlockState(temp.up());

        return isReplaceable(current, top) ? destination.pos(temp) : destination;
    }
}
