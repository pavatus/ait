package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

import java.util.Random;

public class EngineHandler extends KeyedTardisComponent {

    private static final BoolProperty POWER = new BoolProperty("power", Property.warnCompat("power", false));
    private static final BoolProperty HAS_ENGINE_CORE = new BoolProperty("has_engine_core", false);

    private final BoolValue power = POWER.create(this);
    private final BoolValue hasEngineCore = HAS_ENGINE_CORE.create(this);

    public EngineHandler() {
        super(Id.ENGINE);
    }

    static {
        TardisEvents.OUT_OF_FUEL.register(tardis -> tardis.engine().disablePower());

        TardisEvents.LOSE_POWER.register(tardis -> {
            if (TardisUtil.getTardisDimension() != null) {
                tardis.getDesktop().playSoundAtEveryConsole(AITSounds.SHUTDOWN, SoundCategory.AMBIENT, 10f, 1f);
            }

            // disabling protocols
            PropertiesHandler.set(tardis, PropertiesHandler.ANTIGRAVS_ENABLED, false);
            PropertiesHandler.set(tardis, PropertiesHandler.HAIL_MARY, false);
            PropertiesHandler.set(tardis, PropertiesHandler.HADS_ENABLED, false);
        });
    }

    @Override
    public void onLoaded() {
        power.of(this, POWER);
        hasEngineCore.of(this, HAS_ENGINE_CORE);
    }

    public Value<Boolean> hasEngineCore() {
        return hasEngineCore;
    }

    public boolean hasPower() {
        return power.get();
    }

    public void togglePower() {
        if (this.power.get()) {
            this.disablePower();
        } else {
            this.enablePower();
        }
    }

    public void disablePower() {
        if (!this.power.get())
            return;

        DeltaTimeManager.createDelay(AITMod.MOD_ID + "-driftingmusicdelay",
                (long) TimeUtil.secondsToMilliseconds(new Random().nextInt(1, 360))
        );

        this.power.set(false);
        this.updateExteriorState();

        TardisEvents.LOSE_POWER.invoker().onLosePower(this.tardis);
    }

    public void enablePower() {
        if (this.power.get())
            return;

        if (this.tardis.getFuel() <= (0.01 * FuelData.TARDIS_MAX_FUEL))
            return; // cant enable power if not enough fuel

        if (this.tardis.siege().isActive())
            this.tardis.siege().setActive(false);

        if (!this.hasEngineCore.get())
            return;

        this.power.set(true);
        this.updateExteriorState();

        TardisEvents.REGAIN_POWER.invoker().onRegainPower(this.tardis);
    }

    private void updateExteriorState() {
        TravelHandler travel = this.tardis.travel2();

        if (travel.getState() != TravelHandlerBase.State.LANDED)
            return;

        DirectedGlobalPos.Cached pos = travel.position();
        World world = pos.getWorld();

        if (world == null)
            return;

        world.setBlockState(pos.getPos(), world.getBlockState(pos.getPos())
                .with(ExteriorBlock.LEVEL_9, this.power.get() ? 9 : 0)
        );
    }
}
