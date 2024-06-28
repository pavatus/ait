package loqor.ait.tardis.data.travel;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.TravelHandlerV2;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.data.properties.v2.integer.IntProperty;
import loqor.ait.tardis.data.properties.v2.integer.IntValue;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.border.WorldBorder;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("removal")
public abstract class TravelHandlerBase extends KeyedTardisComponent {

    private static final Property<State> STATE = Property.forEnum("state", State.class, State.LANDED);

    private static final Property<DirectedGlobalPos.Cached> POSITION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "position", (DirectedGlobalPos.Cached) Property.warnCompat("previous_position", null));
    private static final Property<DirectedGlobalPos.Cached> DESTINATION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "destination", (DirectedGlobalPos.Cached) Property.warnCompat("previous_position", null));
    private static final Property<DirectedGlobalPos.Cached> PREVIOUS_POSITION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "previous_position", (DirectedGlobalPos.Cached) Property.warnCompat("previous_position", null));

    private static final BoolProperty CRASHING = new BoolProperty("crashing", Property.warnCompat("crashing", false));

    private static final IntProperty SPEED = new IntProperty("speed", Property.warnCompat("speed", 0));
    private static final IntProperty MAX_SPEED = new IntProperty("max_speed", Property.warnCompat("max_speed", 7));

    protected final Value<State> state = STATE.create(this);
    protected final Value<DirectedGlobalPos.Cached> position = POSITION.create(this);
    protected final Value<DirectedGlobalPos.Cached> destination = DESTINATION.create(this);
    protected final Value<DirectedGlobalPos.Cached> previousPosition = PREVIOUS_POSITION.create(this);

    protected final BoolValue crashing = CRASHING.create(this);

    protected final IntValue speed = SPEED.create(this);
    protected final IntValue maxSpeed = MAX_SPEED.create(this);

    public TravelHandlerBase(Id id) {
        super(id);
    }

    @Override
    public void onLoaded() {
        state.of(this, STATE);

        position.of(this, POSITION);
        destination.of(this, DESTINATION);
        previousPosition.of(this, PREVIOUS_POSITION);

        speed.of(this, SPEED);
        maxSpeed.of(this, MAX_SPEED);

        crashing.of(this, CRASHING);

        MinecraftServer current = TravelHandlerBase.server();

        this.position.ifPresent(cached -> cached.init(current), false);
        this.destination.ifPresent(cached -> cached.init(current), false);
        this.previousPosition.ifPresent(cached -> cached.init(current), false);
    }

    public IntValue speed() {
        return speed;
    }

    protected void speed(int value) {
        this.speed.set(this.clampSpeed(value));
    }

    protected int clampSpeed(int value) {
        int max = this.autopilot() ? 1 : this.maxSpeed.get();
        return MathHelper.clamp(value, 0, max);
    }

    public IntValue maxSpeed() {
        return maxSpeed;
    }

    public State getState() {
        return state.get();
    }

    public DirectedGlobalPos.Cached position() {
        return this.position.get();
    }

    public boolean isCrashing() {
        return crashing.get();
    }

    public void setCrashing(boolean crashing) {
        this.crashing.set(crashing);
    }

    public void forcePosition(DirectedGlobalPos.Cached cached) {
        cached.init(TravelHandlerBase.server());
        this.position.set(cached);
    }

    public void forcePosition(Function<DirectedGlobalPos.Cached, DirectedGlobalPos.Cached> position) {
        this.forcePosition(position.apply(this.position()));
    }

    public DirectedGlobalPos.Cached destination() {
        return destination.get();
    }

    public void destination(DirectedGlobalPos.Cached cached) {
        cached.init(TravelHandlerBase.server());

        WorldBorder border = cached.getWorld().getWorldBorder();
        BlockPos pos = cached.getPos();

        cached = border.contains(pos) ? cached : cached.pos(
                border.clamp(pos.getX(), pos.getY(), pos.getZ())
        );

        // TODO: how about, instead of doing the checks at demat, do them at remat?
        //  it makes much more sense, because the target could be obstructed by the time the tardis is there
        cached = this.checkDestination(cached, AITMod.AIT_CONFIG.SEARCH_HEIGHT(), PropertiesHandler.getBool(
                this.tardis().properties(), PropertiesHandler.FIND_GROUND)
        );

        this.tardis().travel2().recalculate();
        this.forceDestination(cached);
    }

    public void forceDestination(DirectedGlobalPos.Cached cached) {
        cached.init(TravelHandlerBase.server());
        this.destination.set(cached);
    }

    public void destination(Function<DirectedGlobalPos.Cached, DirectedGlobalPos.Cached> position) {
        this.destination(position.apply(this.destination()));
    }

    public DirectedGlobalPos.Cached previousPosition() {
        return previousPosition.get();
    }

    protected static MinecraftServer server() {
        return TardisUtil.getOverworld().getServer();
    }

    protected static boolean isReplaceable(BlockState... states) {
        for (BlockState state1 : states) {
            if (!state1.isReplaceable()) {
                return false;
            }
        }

        return true;
    }

    protected DirectedGlobalPos.Cached checkDestination(DirectedGlobalPos.Cached destination, int limit, boolean fullCheck) {
        return destination;
    }

    public enum State {
        LANDED,
        DEMAT(AITSounds.DEMAT_ANIM, TravelHandlerV2::finishDemat),
        FLIGHT(AITSounds.FLIGHT_ANIM),
        MAT(AITSounds.MAT_ANIM, TravelHandlerV2::finishRemat),
        CRASH(AITSounds.GHOST_MAT_ANIM, travelHandlerV2 -> {});

        private final MatSound sound;
        private final boolean animated;

        private final Consumer<TravelHandlerV2> finish;

        State() {
            this(null);
        }

        State(MatSound sound) {
            this(sound, null, false);
        }

        State(MatSound sound, Consumer<TravelHandlerV2> finish) {
            this(sound, finish, true);
        }

        State(MatSound sound, Consumer<TravelHandlerV2> finish, boolean animated) {
            this.sound = sound;
            this.animated = animated;

            this.finish = finish;
        }

        public MatSound effect() {
            return this.sound;
        }

        public boolean animated() {
            return animated;
        }

        public void finish(TravelHandlerV2 handler) {
            this.finish.accept(handler);
        }
    }
}
