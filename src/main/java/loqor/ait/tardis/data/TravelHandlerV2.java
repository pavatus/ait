package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.TardisChunkUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class TravelHandlerV2 extends TravelHandlerBase implements TardisTickable {

    private int animationTicks;

    public TravelHandlerV2() {
        super(Id.TRAVEL2);
    }

    @Override
    public void tick(MinecraftServer server) {
        State state = this.getState();

        if (state.animated())
            this.tickAnimationProgress(state);

        if (server.getTicks() % 2 == 0)
            return;

        // im sure this is great for your server performace
        if (TardisChunkUtil.shouldExteriorChunkBeForced(this.tardis) && !TardisChunkUtil.isExteriorChunkForced(this.tardis)) {
            TardisChunkUtil.forceLoadExteriorChunk(this.tardis);
        } else if (!TardisChunkUtil.shouldExteriorChunkBeForced(this.tardis) && TardisChunkUtil.isExteriorChunkForced(this.tardis)) {
            TardisChunkUtil.stopForceExteriorChunk(this.tardis);
        }
    }

    private void tickAnimationProgress(State state) {
        AITMod.LOGGER.info("Travel: TAP-{}", this.animationTicks);

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

        if (this.isServer())
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

        BlockState blockState = AITBlocks.EXTERIOR_BLOCK.getDefaultState().with(
                ExteriorBlock.ROTATION, (int) DirectionControl.getGeneralizedRotation(globalPos.getRotation())
        ).with(ExteriorBlock.LEVEL_9, 0);

        world.setBlockState(pos, blockState);

        ExteriorBlockEntity exterior = new ExteriorBlockEntity(pos, blockState, this.tardis);
        world.addBlockEntity(exterior);

        if (animate)
            this.runAnimations(exterior);

        BiomeHandler biome = this.tardis.getHandlers().get(Id.BIOME);
        biome.update();

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
    }

    public void rematerialize() {
        this.state.set(State.MAT);
        SoundEvent sound = this.getState().effect().sound();

        this.position.set(this.destination());

        // Play materialize sound at the destination
        this.position().getWorld().playSound(null,
                this.position().getPos(), sound, SoundCategory.BLOCKS
        );

        this.tardis.getDesktop().playSoundAtEveryConsole(sound, SoundCategory.BLOCKS, 10f, 1f);
        this.placeAndAnimate();
    }

    public void finishRemat() {
        this.state.set(State.LANDED);

        DoorData.lockTardis(PropertiesHandler.getBool(this.tardis().properties(), PropertiesHandler.PREVIOUSLY_LOCKED), this.tardis(), null, false);
        TardisEvents.LANDED.invoker().onLanded(tardis());
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
    protected boolean checkDestination(int limit, boolean fullCheck) {
        return false;
    }
}
