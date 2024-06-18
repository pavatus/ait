package loqor.ait.tardis.data;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.core.util.ForcedChunkUtil;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.data.properties.v2.integer.IntProperty;
import loqor.ait.tardis.data.properties.v2.integer.IntValue;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TravelHandler extends KeyedTardisComponent implements TardisTickable {

    private static final Property<State> STATE = Property.forEnum("state", State.class, State.LANDED);

    private static final Property<DirectedGlobalPos.Cached> POSITION = new Property<>(Property.Type.DIRECTED_GLOBAL_POS, "position", Property.warnCompat("previous_position", null));
    private static final Property<DirectedGlobalPos.Cached> DESTINATION = new Property<>(Property.Type.DIRECTED_GLOBAL_POS, "destination", Property.warnCompat("previous_position", null));
    private static final Property<DirectedGlobalPos.Cached> PREVIOUS_POSITION = new Property<>(Property.Type.DIRECTED_GLOBAL_POS, "previous_position", Property.warnCompat("previous_position", null));

    private static final BoolProperty HANDBRAKE = new BoolProperty("handbrake", Property.warnCompat("handbrake", true));
    private static final BoolProperty AUTO_LAND = new BoolProperty("auto_land", Property.warnCompat("auto_land", false));

    private static final IntProperty SPEED = new IntProperty("speed", Property.warnCompat("speed", 0));
    private static final IntProperty MAX_SPEED = new IntProperty("max_speed", Property.warnCompat("max_speed", 7));

    private final Value<State> state = STATE.create(this);
    private final Value<DirectedGlobalPos.Cached> position = POSITION.create(this);
    private final Value<DirectedGlobalPos.Cached> destination = DESTINATION.create(this);
    private final Value<DirectedGlobalPos.Cached> previousPosition = PREVIOUS_POSITION.create(this);

    private final BoolValue handbrake = HANDBRAKE.create(this);
    private final BoolValue autoLand = AUTO_LAND.create(this);

    private final IntValue speed = SPEED.create(this);
    private final IntValue maxSpeed = MAX_SPEED.create(this);

    private boolean crashing;

    public TravelHandler() {
        super(Id.TRAVEL);
    }

    @Override
    public void onLoaded() {
        state.of(this, STATE);

        position.of(this, POSITION);
        destination.of(this, DESTINATION);
        previousPosition.of(this, PREVIOUS_POSITION);

        speed.of(this, SPEED);
        maxSpeed.of(this, MAX_SPEED);

        handbrake.of(this, HANDBRAKE);
        autoLand.of(this, AUTO_LAND);

        this.position.ifPresent(cached -> cached.init(server()), false);
        this.destination.ifPresent(cached -> cached.init(server()), false);
        this.previousPosition.ifPresent(cached -> cached.init(server()), false);
    }

    public ExteriorBlockEntity placeExterior() {
        DirectedGlobalPos.Cached globalPos = this.position.get();
        ServerWorld world = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        BlockState blockState = AITBlocks.EXTERIOR_BLOCK.getDefaultState().with(
                ExteriorBlock.ROTATION, DirectionControl.getGeneralizedRotation(globalPos.getRotation())
        ).with(ExteriorBlock.LEVEL_9, 0);

        world.setBlockState(pos, blockState);
        ExteriorBlockEntity exterior = new ExteriorBlockEntity(pos, blockState);

        exterior.link(this.tardis);
        world.addBlockEntity(exterior);

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
                    AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f); // fixme can be spammed

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

        if (this.autoLand.get()) {
            // fulfill all the prerequisites
            // DoorData.lockTardis(true, tardis(), null, false);
            this.handbrake.set(false);

            this.tardis.getDoor().closeDoors();
            this.tardis.setRefueling(false);

            if (this.speed.get() == 0)
                this.increaseSpeed();
        }

        DirectedGlobalPos.Cached globalPos = this.position.get();

        this.autoLand.set(withRemat);
        ServerWorld world = globalPos.getWorld();

        this.state.set(State.DEMAT);
        SoundEvent sound = this.getState().effect().sound();

        world.playSound(null, globalPos.getPos(), sound, SoundCategory.BLOCKS);
        FlightUtil.playSoundAtEveryConsole(this.tardis().getDesktop(), sound, SoundCategory.BLOCKS, 10f, 1f);

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
                this.position.get(), this.destination().get(), tardis.flight().getDurationAsPercentage()
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
        byte rotation = destination.getRotation();

        // Play materialize sound at the destination
        SoundEvent sound = this.getState().effect().sound();
        destWorld.playSound(null, destPos, sound, SoundCategory.BLOCKS, 1f, 1f);

        FlightUtil.playSoundAtEveryConsole(tardis.getDesktop(), sound, SoundCategory.BLOCKS, 1f, 1f);

        // Set the destination block to the Tardis exterior block
        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.ROTATION, Math.abs(rotation)).with(ExteriorBlock.LEVEL_9, 0);
        destWorld.setBlockState(destPos, state);

        // Create and add the exterior block entity at the destination
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(destPos, state);
        destWorld.addBlockEntity(blockEntity);

        // Set the position of the Tardis to the destination
        this.position.set(destination);

        // Run animations on the block entity
        this.runAnimations(blockEntity);

        BiomeHandler biome = tardis.handler(Id.BIOME);
        biome.update();

        if (tardis.isGrowth()) {
            TardisExterior exterior = tardis.getExterior();

            exterior.setType(CategoryRegistry.CAPSULE);
            tardis.getDoor().closeDoors();
        }
    }

    public void fly() {
        this.crashing = false;
        this.previousPosition.set(this.position);
        this.state.set(State.FLIGHT);

        this.deleteExterior();
        if (PropertiesHandler.getBool(this.tardis().properties(), SecurityControl.SECURITY_KEY))
            SecurityControl.runSecurityProtocols(this.tardis());
    }

    public void land() {
        if (this.autoLand.get() && this.speed.get() > 0)
            this.speed.set(0);

        this.state.set(State.LANDED);

        ServerWorld world = this.position.get().getWorld();
        ExteriorBlockEntity exteriorBlockEntity;

        // If there is already a matching exterior at this position
        if (world.getBlockEntity(this.getPosition().getPos()) instanceof ExteriorBlockEntity exterior
                && this.tardis == exterior.tardis().get()) {
            exteriorBlockEntity = exterior;
        } else {
            exteriorBlockEntity = this.placeExterior();
        }

        this.land(exteriorBlockEntity);
    }

    public void land(ExteriorBlockEntity blockEntity) {
        this.runAnimations(blockEntity);

        if (this.isClient())
            return;

        DoorData.lockTardis(PropertiesHandler.getBool(this.tardis().properties(), PropertiesHandler.PREVIOUSLY_LOCKED), this.tardis(), null, false);
        TardisEvents.LANDED.invoker().onLanded(this.tardis);
    }

    public void initPos(DirectedGlobalPos.Cached cached) {
        if (this.position.get() == null)
            this.position.set(cached);

        if (this.destination.get() == null)
            this.destination.set(cached);

        if (this.previousPosition.get() == null)
            this.previousPosition.set(cached);
    }

    public void increaseSpeed() {
        // Stop speed from going above 1 if autopilot is enabled, and we're in flight
        if (this.speed.get() > 0 && this.getState() == State.FLIGHT && this.autoLand.get())
            return;

        this.speed.set(MathHelper.clamp(this.speed.get() + 1, 0, this.maxSpeed.get()));
    }

    public void decreaseSpeed() {
        if (this.getState() == State.LANDED && this.speed.get() == 1)
            FlightUtil.playSoundAtEveryConsole(this.tardis().getDesktop(), AITSounds.LAND_THUD, SoundCategory.AMBIENT);

        this.speed.set(MathHelper.clamp(this.speed.get() - 1, 0, this.maxSpeed.get()));
    }

    public IntValue speed() {
        return speed;
    }

    public IntValue maxSpeed() {
        return maxSpeed;
    }

    public BoolValue handbrake() {
        return handbrake;
    }

    public BoolValue autoLand() {
        return autoLand;
    }

    public State getState() {
        return state.get();
    }

    public DirectedGlobalPos getPosition() {
        return position.get();
    }

    public Value<DirectedGlobalPos.Cached> destination() {
        return destination;
    }

    public DirectedGlobalPos getPreviousPosition() {
        return previousPosition.get();
    }

    public boolean isCrashing() {
        return crashing;
    }

    private static MinecraftServer server() {
        return TardisUtil.getOverworld().getServer();
    }

    public enum State {
        LANDED(AITSounds.LANDED_ANIM),
        DEMAT(AITSounds.DEMAT_ANIM),
        FLIGHT(AITSounds.FLIGHT_ANIM),
        MAT(AITSounds.MAT_ANIM),
        CRASH(AITSounds.LANDED_ANIM);

        private final MatSound sound;

        State(MatSound sound) {
            this.sound = sound;
        }

        public MatSound effect() {
            return this.sound;
        }
    }
}
