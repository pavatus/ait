package loqor.ait.tardis.data;

import io.wispforest.owo.ops.WorldOps;
import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.NetworkUtil;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("removal")
public class TravelHandler extends TravelHandlerBase implements TardisTickable {

    public static final Identifier CANCEL_DEMAT_SOUND = new Identifier(AITMod.MOD_ID, "cancel_demat_sound");

    public TravelHandler() {
        super(Id.TRAVEL2);
    }

    public int getDematTicks() {
        return PropertiesHandler.getInt(this.tardis().properties(), PropertiesHandler.DEMAT_TICKS);
    }

    private void setDematTicks(int ticks) {
        PropertiesHandler.set(this.tardis(), PropertiesHandler.DEMAT_TICKS, ticks, false);
    }

    /**
     * Stops demat while its happening - then plays a boom sound to signify
     */
    private void cancelDemat() {
        if (this.getState() != State.DEMAT)
            return; // rip

        if (this.position() == null || this.tardis().getDesktop() == null)
            return;

        this.finishLanding();

        this.position().getWorld().playSound(null, this.position().getPos(), AITSounds.LAND_THUD, SoundCategory.AMBIENT);
        FlightUtil.playSoundAtEveryConsole(this.tardis().getDesktop(), AITSounds.LAND_THUD, SoundCategory.AMBIENT);

        NetworkUtil.sendToInterior(this.tardis(), CANCEL_DEMAT_SOUND, PacketByteBufs.empty());
    }

    private void tickDemat() {
        if (this.getState() != State.DEMAT) {
            if (getDematTicks() != 0)
                setDematTicks(0);

            return;
        }

        setDematTicks(getDematTicks() + 1);

        if (tardis.travel2().handbrake()) {
            // cancel materialise
            this.cancelDemat();
            return;
        }

        if (getDematTicks() > FlightUtil.getSoundLength(this.getState().effect()) * 40) {
            this.finishDemat();
            this.setDematTicks(0);
        }
    }

    private void tickMat() {
        if (this.getState() != State.MAT) {
            if (getDematTicks() != 0) setDematTicks(0);
            return;
        }

        setDematTicks(getDematTicks() + 1);

        if (getDematTicks() > (FlightUtil.getSoundLength(this.getState().effect()) * 40)) {
            this.finishLanding();
            this.setDematTicks(0);
        }
    }

    @Override
    public void tick(MinecraftServer server) {
        this.tickDemat();
        this.tickMat();

        ServerTardis tardis = (ServerTardis) this.tardis();
        int speed = this.tardis.travel2().speed().get();
        State state = this.getState();

        boolean handbrake = tardis.travel2().handbrake();
        boolean autopilot = tardis.travel2().autopilot().get();

        if (speed > 0 && state == State.LANDED && !handbrake && !tardis.sonic().hasSonic(SonicHandler.HAS_EXTERIOR_SONIC))
            this.dematerialize(autopilot);

        // Should we just disable autopilot if the speed goes above 1?
        if (speed > 1 && state == State.FLIGHT && autopilot) {
            this.tardis.travel2().speed().set(speed - 1);
        }
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
        ExteriorBlockEntity exterior = new ExteriorBlockEntity(pos, blockState);

        exterior.link(this.tardis);
        world.addBlockEntity(exterior);

        if (animate)
            this.runAnimations(exterior);

        return exterior;
    }

    private void runAnimationsAt(DirectedGlobalPos.Cached globalPos) {
        if (globalPos.getWorld().getBlockEntity(globalPos.getPos()) instanceof ExteriorBlockEntity exterior)
            this.runAnimations(exterior);
    }

    private void runAnimations(ExteriorBlockEntity exterior) {
        if (exterior.getAnimation() == null)
            return;

        exterior.getAnimation().setupAnimation(this.getState());
        exterior.getAnimation().tellClientsToSetup(this.getState());
    }

    public void deleteExterior() {
        DirectedGlobalPos.Cached globalPos = this.position.get();

        ServerWorld world = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        world.removeBlock(pos, false);

        if (this.isServer())
            ForcedChunkUtil.stopForceLoading(world, pos);
    }

    public void dematerialize(boolean withRemat) {
        if (TardisEvents.DEMAT.invoker().onDemat(this.tardis)) {
            // demat will be cancelled
            DirectedGlobalPos.Cached globalPos = this.position.get();
            globalPos.getWorld().playSound(null, globalPos.getPos(),
                    AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f);

            if (TardisUtil.isInteriorNotEmpty(this.tardis))
                FlightUtil.playSoundAtEveryConsole(this.tardis.getDesktop(),
                        AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f);

            FlightUtil.createDematerialiseDelay(this.tardis);
            return;
        }

        if (!this.tardis.engine().hasPower())
            return; // no flying for you if you have no powa :)

        if (FlightUtil.isDematerialiseOnCooldown(this.tardis))
            return; // cancelled

        this.forceDematerialize(withRemat);
    }

    public void forceDematerialize(boolean withRemat) {
        if (this.getState() != State.LANDED)
            return;

        if (this.tardis.travel2().autopilot().get()) {
            // fulfill all the prerequisites
            this.tardis.travel2().handbrake(false);

            this.tardis.door().closeDoors();
            this.tardis.setRefueling(false);

            if (this.speed.get() == 0)
                this.increaseSpeed();
        }

        DirectedGlobalPos.Cached globalPos = this.position.get();
        ServerWorld world = globalPos.getWorld();

        this.tardis.travel2().autopilot().set(withRemat);
        this.state.set(State.DEMAT);

        SoundEvent sound = this.getState().effect().sound();

        FlightUtil.playSoundAtEveryConsole(this.tardis().getDesktop(), sound, SoundCategory.BLOCKS, 10f, 1f);
        world.playSound(null, globalPos.getPos(), sound, SoundCategory.BLOCKS);

        this.runAnimationsAt(globalPos);
    }

    public void materialise() {
        // Check if materialization is on cooldown and return if it is
        if (FlightUtil.isMaterialiseOnCooldown(tardis))
            return;

        // Check if the Tardis materialization is prevented by event listeners
        if (TardisEvents.MAT.invoker().onMat(tardis)) {
            // Play failure sound at the current position
            DirectedGlobalPos.Cached position = this.position.get();
            World world = position.getWorld();
            BlockPos pos = position.getPos();

            world.playSound(null, pos, AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f);

            // Play failure sound at the Tardis console position if the interior is not empty
            FlightUtil.playSoundAtEveryConsole(this.tardis.getDesktop(), AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f);

            // Create materialization delay and return
            FlightUtil.createMaterialiseDelay(this.tardis);
            return;
        }

        this.forceMaterialize();
    }

    public void forceMaterialize() {
        Tardis tardis = this.tardis();

        if (this.getState() != TravelHandler.State.FLIGHT)
            return;

        DirectedGlobalPos.Cached destination = FlightUtil.getPositionFromPercentage(
                this.position.get(), this.destination(), tardis.travel2().getDurationAsPercentage()
        );

        this.destination.set(destination, true);

        // Set the Tardis state to materialise
        this.state.set(TravelHandler.State.MAT);

        SequenceHandler sequences = tardis.handler(Id.SEQUENCE);

        if (sequences.hasActiveSequence()) {
            sequences.setActiveSequence(null, true);
        }

        // Get the server world of the destination
        ServerWorld destWorld = destination.getWorld();
        BlockPos destPos = destination.getPos();

        // Play materialize sound at the destination
        SoundEvent sound = this.getState().effect().sound();
        destWorld.playSound(null, destPos, sound, SoundCategory.BLOCKS, 1f, 1f);

        FlightUtil.playSoundAtEveryConsole(tardis.getDesktop(), sound, SoundCategory.BLOCKS, 1f, 1f);

        // Set the position of the Tardis to the destination
        this.position.set(destination);
        this.placeExterior();

        if (tardis.isGrowth()) {
            TardisExterior exterior = tardis.getExterior();

            exterior.setType(CategoryRegistry.CAPSULE);
            tardis.door().closeDoors();
        }
    }

    public void finishDemat() {
        this.crashing.set(false);
        this.previousPosition.set(this.position);
        this.state.set(State.FLIGHT);

        this.deleteExterior();

        if (PropertiesHandler.getBool(this.tardis().properties(), SecurityControl.SECURITY_KEY))
            SecurityControl.runSecurityProtocols(this.tardis());
    }

    public void finishLanding() {
        if (this.tardis.travel2().autopilot().get() && this.speed.get() > 0)
            this.speed.set(0);

        this.state.set(State.LANDED);

        ServerWorld world = this.position.get().getWorld();
        ExteriorBlockEntity exteriorBlockEntity;

        // If there is already a matching exterior at this position
        if (world.getBlockEntity(this.position().getPos()) instanceof ExteriorBlockEntity exterior
                && this.tardis == exterior.tardis().get()) {
            exteriorBlockEntity = exterior;
        } else {
            exteriorBlockEntity = this.placeExterior();
        }

        this.finishLanding(exteriorBlockEntity);
    }

    public void finishLanding(ExteriorBlockEntity blockEntity) {
        this.runAnimations(blockEntity);

        DoorData.lockTardis(PropertiesHandler.getBool(this.tardis().properties(), PropertiesHandler.PREVIOUSLY_LOCKED), this.tardis(), null, false);
        TardisEvents.LANDED.invoker().onLanded(this.tardis);
    }

    /**
     * Sets the position of the tardis based off the flight's progress to the destination.
     */
    public void setPosFromProgress() {
        if (this.getState() != State.FLIGHT)
            return;

        DirectedGlobalPos.Cached pos = FlightUtil.getPositionFromPercentage(
                this.position(), this.destination(),
                this.tardis().travel2().getDurationAsPercentage()
        );

        this.position.set(pos);
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

    public boolean inFlight() {
        return this.getState() != State.LANDED;
    }

    @Override
    public boolean checkDestination(int limit, boolean fullCheck) {
        if (this.isClient())
            return true;

        DirectedGlobalPos.Cached destination = this.destination();
        ServerWorld world = destination.getWorld();
        BlockPos pos = destination.getPos();

        destination = destination.pos(pos.getX(), MathHelper.clamp(
                pos.getY(), world.getBottomY(), world.getTopY() - 1
        ), pos.getZ());

        BlockState current;
        BlockState top;
        BlockState ground;

        if (fullCheck) {
            BlockPos temp = destination.getPos();

            for (int i = 0; i < limit; i++) {
                current = world.getBlockState(temp);
                top = world.getBlockState(temp.up());
                ground = world.getBlockState(temp.down());

                if (isReplaceable(current, top) && !isReplaceable(ground)) { // check two blocks cus tardis is two blocks tall yk and check for ground
                    this.destination.set(destination.world(world).pos(temp));
                    return true;
                }

                temp = temp.down();
            }
        }

        BlockPos temp2 = destination.getPos();

        current = world.getBlockState(temp2);
        top = world.getBlockState(temp2.up());

        this.destination.set(destination);
        return isReplaceable(current, top);
    }

    public void crash() {
        if (this.getState() != State.FLIGHT || this.isCrashing())
            return;

        int intensity = this.speed.get() + tardis.tardisHammerAnnoyance + 1;
        List<Explosion> explosions = new ArrayList<>();

        tardis.getDesktop().getConsolePos().forEach(console -> {
            FlightUtil.playSoundAtConsole(console,
                    SoundEvents.ENTITY_GENERIC_EXPLODE,
                    SoundCategory.BLOCKS, 3f, 1f
            );

            Explosion explosion = TardisUtil.getTardisDimension().createExplosion(
                    null, null, null,
                    console.toCenterPos(), 3f * intensity,
                    false, World.ExplosionSourceType.BLOCK
            );

            explosions.add(explosion);
        });

        Random random = TardisUtil.random();
        SequenceHandler sequence = this.tardis.sequence();

        if (sequence.hasActiveSequence())
            sequence.setActiveSequence(null, true);

        for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(tardis)) {
            float xVel = random.nextFloat(-2f, 3f);
            float yVel = random.nextFloat(-1f, 2f);
            float zVel = random.nextFloat(-2f, 3f);

            player.setVelocity(xVel * intensity, yVel * intensity, zVel * intensity);

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20 * intensity, 1, true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * intensity, (int) Math.round(0.25 * intensity), true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 20 * intensity, (int) Math.round(0.25 * intensity), true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * intensity, (int) Math.round(0.25 * intensity), true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20 * intensity, (int) Math.round(0.75 * intensity), true, false, false));

            if (explosions.isEmpty())
                player.damage(player.getDamageSources().generic(), Math.round(intensity * 0.5));
        }

        tardis.door().setLocked(true);

        PropertiesHandler.set(tardis, PropertiesHandler.ALARM_ENABLED, true);
        PropertiesHandler.set(tardis, PropertiesHandler.ANTIGRAVS_ENABLED, false);

        this.speed.set(0);

        tardis.removeFuel(500 * intensity);
        tardis.tardisHammerAnnoyance = 0;

        int multiplier = random.nextInt(0, 2) == 0 ? 1 : -1;
        int random_change = random.nextInt(10, 100) * intensity * multiplier;
        
        DirectedGlobalPos.Cached median = FlightUtil.getPositionFromPercentage(
                this.position(), this.destination(), tardis.travel2().getDurationAsPercentage()
        );

        this.setCrashing(true);
        this.destination.set(median.offset(random_change, 0, random_change));

        this.crashAndMaterialise();
        int repairTicks = 1000 * intensity;
        tardis.crash().setRepairTicks(repairTicks);

        if (repairTicks > TardisCrashData.UNSTABLE_TICK_START_THRESHOLD) {
            tardis.crash().setState(TardisCrashData.State.TOXIC);
        } else {
            tardis.crash().setState(TardisCrashData.State.UNSTABLE);
        }

        TardisEvents.CRASH.invoker().onCrash(tardis);
    }

    public void crashAndMaterialise() {
        if (this.getState() != State.FLIGHT)
            return;

        this.state.set(State.MAT);

        ServerWorld destWorld = this.destination().getWorld();
        BlockPos pos = this.destination().getPos();

        ForcedChunkUtil.keepChunkLoaded(destWorld, pos);
        WorldOps.updateIfOnServer(destWorld, pos);

        // Set the position of the Tardis to the destination
        this.position.set(this.destination());
        this.placeExterior(false);
    }

    public void immediatelyLandAt(DirectedGlobalPos.Cached globalPos) {
        this.state.set(State.LANDED);
        this.deleteExterior();

        ServerWorld destWorld = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        ForcedChunkUtil.keepChunkLoaded(destWorld, pos);
        WorldOps.updateIfOnServer(destWorld, pos);

        this.destination.set(globalPos);

        this.position.set(globalPos);
        this.placeExterior(false);
    }
}
