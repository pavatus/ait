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
import loqor.ait.tardis.data.travel.ProgressiveTravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.NetworkUtil;
import loqor.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class TravelHandlerV2 extends ProgressiveTravelHandler {

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

        if (tardis.crash().getState() == TardisCrashData.State.UNSTABLE) {
            Random random = TardisUtil.random();
            int multiplier = random.nextInt(0, 2) == 0 ? 1 : -1;

            this.destination(cached -> cached.offset(
                    random.nextInt(1, 10) * multiplier, 0,
                    random.nextInt(1, 10) * multiplier
            ));
        }

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

    public void dematerialize() {
        if (this.getState() != State.LANDED)
            return;

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
        if (this.getState() != State.FLIGHT)
            return;

        this.state.set(State.MAT);
        SoundEvent sound = this.getState().effect().sound();

        this.position.set(this.destination());

        // Play materialize sound at the destination
        this.position().getWorld().playSound(null,
                this.position().getPos(), sound, SoundCategory.BLOCKS
        );

        this.tardis.getDesktop().playSoundAtEveryConsole(sound, SoundCategory.BLOCKS, 10f, 1f);
        this.placeAndAnimate();

        AITMod.LOGGER.info("Remat called", new Throwable());
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
    public boolean checkDestination(int limit, boolean fullCheck) {
        return false;
    }
}
