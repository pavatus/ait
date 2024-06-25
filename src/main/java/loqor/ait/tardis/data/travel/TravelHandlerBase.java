package loqor.ait.tardis.data.travel;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.tardis.base.KeyedTardisComponent;
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
import net.minecraft.world.border.WorldBorder;

import java.util.function.Function;

@SuppressWarnings("removal")
public abstract class TravelHandlerBase extends KeyedTardisComponent {

    private static final Property<State> STATE = Property.forEnum("state", State.class, State.LANDED);

    private static final Property<DirectedGlobalPos.Cached> POSITION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "position", (DirectedGlobalPos.Cached) Property.warnCompat("previous_position", null));
    private static final Property<DirectedGlobalPos.Cached> DESTINATION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "destination", (DirectedGlobalPos.Cached) Property.warnCompat("previous_position", null));
    private static final Property<DirectedGlobalPos.Cached> PREVIOUS_POSITION = new Property<>(Property.Type.CDIRECTED_GLOBAL_POS, "previous_position", (DirectedGlobalPos.Cached) Property.warnCompat("previous_position", null));

    private static final BoolProperty HANDBRAKE = new BoolProperty("handbrake", Property.warnCompat("handbrake", true));
    private static final BoolProperty AUTO_LAND = new BoolProperty("auto_land", Property.warnCompat("auto_land", false));
    private static final BoolProperty CRASHING = new BoolProperty("crashing", Property.warnCompat("crashing", false));

    private static final IntProperty SPEED = new IntProperty("speed", Property.warnCompat("speed", 0));
    private static final IntProperty MAX_SPEED = new IntProperty("max_speed", Property.warnCompat("max_speed", 7));

    protected final Value<State> state = STATE.create(this);
    protected final Value<DirectedGlobalPos.Cached> position = POSITION.create(this);
    protected final Value<DirectedGlobalPos.Cached> destination = DESTINATION.create(this);
    protected final Value<DirectedGlobalPos.Cached> previousPosition = PREVIOUS_POSITION.create(this);

    protected final BoolValue handbrake = HANDBRAKE.create(this);
    protected final BoolValue autoLand = AUTO_LAND.create(this);
    protected final BoolValue crashing = CRASHING.create(this);

    protected final IntValue speed = SPEED.create(this);
    protected final IntValue maxSpeed = MAX_SPEED.create(this);

    public TravelHandlerBase() {
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
        crashing.of(this, CRASHING);

        MinecraftServer current = TravelHandlerBase.server();

        this.position.ifPresent(cached -> cached.init(current), false);
        this.destination.ifPresent(cached -> cached.init(current), false);
        this.previousPosition.ifPresent(cached -> cached.init(current), false);
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

        this.tardis().flight().recalculate();
        this.checkDestination(AITMod.AIT_CONFIG.SEARCH_HEIGHT(), PropertiesHandler.getBool(
                this.tardis().properties(), PropertiesHandler.FIND_GROUND)
        );

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

    protected abstract boolean checkDestination(int limit, boolean fullCheck);

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
