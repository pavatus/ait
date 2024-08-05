package loqor.ait.tardis.data.travel;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.TardisCrashHandler;
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.properties.integer.IntProperty;
import loqor.ait.tardis.data.properties.integer.IntValue;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.border.WorldBorder;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class TravelHandlerBase extends KeyedTardisComponent implements TardisTickable {

    private static final Property<State> STATE = Property.forEnum("state", State.class, State.LANDED);
    private static final BoolProperty LEAVE_BEHIND = new BoolProperty("leave_behind", false);

    private static final Property<DirectedGlobalPos.Cached> POSITION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "position", (DirectedGlobalPos.Cached) null);
    private static final Property<DirectedGlobalPos.Cached> DESTINATION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "destination", (DirectedGlobalPos.Cached) null);
    private static final Property<DirectedGlobalPos.Cached> PREVIOUS_POSITION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "previous_position", (DirectedGlobalPos.Cached) null);

    private static final BoolProperty CRASHING = new BoolProperty("crashing", false);
    private static final BoolProperty ANTIGRAVS = new BoolProperty("antigravs",false);

    private static final IntProperty SPEED = new IntProperty("speed", 0);
    private static final IntProperty MAX_SPEED = new IntProperty("max_speed", 7);

    private static final Property<GroundSearch> VGROUND_SEARCH = Property.forEnum("yground_search", GroundSearch.class, GroundSearch.CEILING);
    private static final BoolProperty HGROUND_SEARCH = new BoolProperty("hground_search", true);

    protected final Value<State> state = STATE.create(this);
    protected final Value<DirectedGlobalPos.Cached> position = POSITION.create(this);
    protected final Value<DirectedGlobalPos.Cached> destination = DESTINATION.create(this);
    protected final Value<DirectedGlobalPos.Cached> previousPosition = PREVIOUS_POSITION.create(this);

    private final BoolValue leaveBehind = LEAVE_BEHIND.create(this);
    protected final BoolValue crashing = CRASHING.create(this);
    protected final BoolValue antigravs = ANTIGRAVS.create(this);

    protected final IntValue speed = SPEED.create(this);
    protected final IntValue maxSpeed = MAX_SPEED.create(this);

    protected final Value<GroundSearch> vGroundSearch = VGROUND_SEARCH.create(this);
    protected final BoolValue hGroundSearch = HGROUND_SEARCH.create(this);

    @Exclude(strategy = Exclude.Strategy.NETWORK)
    protected int hammerUses = 0;

    public TravelHandlerBase(Id id) {
        super(id);
    }

    @Override
    public void onLoaded() {
        state.of(this, STATE);

        position.of(this, POSITION);
        destination.of(this, DESTINATION);
        previousPosition.of(this, PREVIOUS_POSITION);
        leaveBehind.of(this, LEAVE_BEHIND);

        speed.of(this, SPEED);
        maxSpeed.of(this, MAX_SPEED);

        crashing.of(this, CRASHING);
        antigravs.of(this, ANTIGRAVS);

        vGroundSearch.of(this, VGROUND_SEARCH);
        hGroundSearch.of(this, HGROUND_SEARCH);

        if (this.isClient())
            return;

        @SuppressWarnings("resource")
        MinecraftServer current = TravelHandlerBase.server();

        this.position.ifPresent(cached -> cached.init(current), false);
        this.destination.ifPresent(cached -> cached.init(current), false);
        this.previousPosition.ifPresent(cached -> cached.init(current), false);
    }

    @Override
    public void tick(MinecraftServer server) {
        TardisCrashHandler crash = tardis.crash();

        if (crash.getState() != TardisCrashHandler.State.NORMAL)
            crash.addRepairTicks(2 * this.speed());

        if (this.hammerUses > 0 && !DeltaTimeManager.isStillWaitingOnDelay(AITMod.MOD_ID + "-tardisHammerAnnoyanceDecay")) {
            this.hammerUses--;

            DeltaTimeManager.createDelay(AITMod.MOD_ID + "-tardisHammerAnnoyanceDecay", (long) TimeUtil.secondsToMilliseconds(10));
        }
    }

    public BoolValue leaveBehind() {
        return leaveBehind;
    }

    public void speed(int value) {
        this.speed.set(this.clampSpeed(value));
    }

    public int speed() {
        return this.speed.get();
    }

    protected int clampSpeed(int value) {
        return MathHelper.clamp(value, 0, this.maxSpeed.get());
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

    public BoolValue antigravs() {
        return antigravs;
    }

    public boolean isCrashing() {
        return crashing.get();
    }

    public void setCrashing(boolean crashing) {
        this.crashing.set(crashing);
    }

    public int getHammerUses() {
        return hammerUses;
    }

    public int instability() {
        return this.getHammerUses() + 1;
    }

    public void useHammer() {
        this.hammerUses++;
    }

    public void resetHammerUses() {
        this.hammerUses = 0;
    }

    public void forcePosition(DirectedGlobalPos.Cached cached) {
        cached.init(TravelHandlerBase.server());
        this.previousPosition.set(this.position);
        this.position.set(cached);
    }

    public void forcePosition(Function<DirectedGlobalPos.Cached, DirectedGlobalPos.Cached> position) {
        this.forcePosition(position.apply(this.position()));
    }

    public DirectedGlobalPos.Cached destination() {
        return destination.get();
    }

    public void destination(DirectedGlobalPos.Cached cached) {
        if (this.destination().equals(cached))
            return;

        cached.init(TravelHandlerBase.server());

        WorldBorder border = cached.getWorld().getWorldBorder();
        BlockPos pos = cached.getPos();

        cached = border.contains(pos) ? cached : cached.pos(
                border.clamp(pos.getX(), pos.getY(), pos.getZ())
        );

        this.forceDestination(WorldUtil.locateSafe(
                cached, this.vGroundSearch.get(), this.hGroundSearch.get()
        ));
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

    public BoolValue horizontalSearch() {
        return hGroundSearch;
    }

    public Value<GroundSearch> verticalSearch() {
        return vGroundSearch;
    }

    protected static MinecraftServer server() {
        return TardisUtil.getOverworld().getServer();
    }

    public enum State {
        LANDED,
        DEMAT(AITSounds.DEMAT_ANIM, TravelHandler::finishDemat),
        FLIGHT(AITSounds.FLIGHT_ANIM),
        MAT(AITSounds.MAT_ANIM, TravelHandler::finishRemat);

        private final MatSound sound;
        private final boolean animated;

        private final Consumer<TravelHandler> finish;

        State() {
            this(null);
        }

        State(MatSound sound) {
            this(sound, null, false);
        }

        State(MatSound sound, Consumer<TravelHandler> finish) {
            this(sound, finish, true);
        }

        State(MatSound sound, Consumer<TravelHandler> finish, boolean animated) {
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

        public void finish(TravelHandler handler) {
            this.finish.accept(handler);
        }
    }

    public enum GroundSearch {
        NONE {
            @Override
            public GroundSearch next() {
                return FLOOR;
            }
        },
        FLOOR {
            @Override
            public GroundSearch next() {
                return CEILING;
            }
        },
        CEILING {
            @Override
            public GroundSearch next() {
                return MEDIAN;
            }
        },
        MEDIAN {
            @Override
            public GroundSearch next() {
                return FLOOR;
            }
        };

        public abstract GroundSearch next();
    }
}
