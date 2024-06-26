package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.TardisChunkUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class TravelHandlerV2 extends TravelHandlerBase implements TardisTickable {

    private int dematTicks;

    public TravelHandlerV2() {
        super(Id.TRAVEL2);
    }

    @Override
    public void tick(MinecraftServer server) {
        tickDemat();

        // im sure this is great for your server performace
        if (TardisChunkUtil.shouldExteriorChunkBeForced(this.tardis) && !TardisChunkUtil.isExteriorChunkForced(this.tardis)) {
            TardisChunkUtil.forceLoadExteriorChunk(this.tardis);
        } else if (!TardisChunkUtil.shouldExteriorChunkBeForced(this.tardis) && TardisChunkUtil.isExteriorChunkForced(this.tardis)) {
            TardisChunkUtil.stopForceExteriorChunk(this.tardis);
        }
    }

    private void tickDemat() {
        if (this.getState() != State.DEMAT) {
            if (this.dematTicks != 0)
                this.dematTicks = 0;

            return;
        }

        if (this.dematTicks++ > this.getState().effect().maxTime() * 2)
            this.finishDemat();
    }

    @Override
    protected void onEarlyInit(InitContext ctx) {
        if (ctx.created() && ctx.pos() != null) {
            AITMod.LOGGER.error("EXT AT : " + ctx.pos());
            this.initPos(ctx.pos());
        }
    }

    @Override
    public void postInit(InitContext context) {
        if (this.isServer() && context.created())
            this.placeExterior();
    }

    public void deleteExterior() {
        DirectedGlobalPos.Cached globalPos = this.position.get();

        ServerWorld world = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        world.removeBlock(pos, false);

        if (this.isServer())
            ForcedChunkUtil.stopForceLoading(world, pos);
    }

    public ExteriorBlockEntity placeExterior() {
        return placeExterior(true);
    }

    public ExteriorBlockEntity placeExterior(boolean animate) {
        BiomeHandler biome = this.tardis.getHandlers().get(Id.BIOME);
        biome.update();

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

        return exterior;
    }

    private void runAnimations(ExteriorBlockEntity exterior) {
        if (exterior.getAnimation() == null) {
            AITMod.LOGGER.info("Null animation for exterior at {}", exterior.getPos());
            return;
        }

        exterior.getAnimation().setupAnimation(this.getState());
        exterior.getAnimation().tellClientsToSetup(this.getState());
    }

    public void runAnimations() {
        ServerWorld level = this.position().getWorld();
        BlockEntity entity = level.getBlockEntity(this.position().getPos());

        if (entity instanceof ExteriorBlockEntity exterior) {
            this.runAnimations(exterior);
        }
    }

    public void dematerialize() {
        this.state.set(State.DEMAT);

        SoundEvent sound = this.getState().effect().sound();

        this.position().getWorld().playSound(null, this.position().getPos(),
                sound, SoundCategory.BLOCKS
        );

        FlightUtil.playSoundAtEveryConsole(this.tardis().getDesktop(), sound, SoundCategory.BLOCKS, 10f, 1f);
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
