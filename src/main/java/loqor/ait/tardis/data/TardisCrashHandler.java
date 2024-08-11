package loqor.ait.tardis.data;

import org.joml.Vector3f;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.util.AITModTags;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.properties.integer.IntProperty;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;

public class TardisCrashHandler extends KeyedTardisComponent implements TardisTickable {
    private static final Property<State> STATE_PROPERTY = Property.forEnum("state", State.class, State.NORMAL);
    private final Value<State> state = STATE_PROPERTY.create(this);
    private static final IntProperty TARDIS_REPAIR_TICKS_PROPERTY = new IntProperty("repair_ticks", 0);
    private final Value<Integer> tardisRepairTicks = TARDIS_REPAIR_TICKS_PROPERTY.create(this);

    @Exclude
    private static final String DELAY_ID_START = AITMod.MOD_ID + "-tardiscrashrecoverydelay-";

    @Exclude
    public static final Integer UNSTABLE_TICK_START_THRESHOLD = 2_400;

    @Exclude
    public static final Integer MAX_REPAIR_TICKS = 7_000;

    public boolean isToxic() {
        return this.getState() == State.TOXIC;
    }

    public boolean isUnstable() {
        return this.getState() == State.UNSTABLE;
    }

    public boolean isNormal() {
        return this.getState() == State.NORMAL;
    }

    @Override
    public void onLoaded() {
        state.of(this, STATE_PROPERTY);
        tardisRepairTicks.of(this, TARDIS_REPAIR_TICKS_PROPERTY);
    }

    @Override
    public void tick(MinecraftServer server) {

        if (state.get() == null) {
            state.set(State.NORMAL);
        }

        if (getRepairTicks() > 0) {
            setRepairTicks(this.tardis().isRefueling() ? getRepairTicks() - 10 : getRepairTicks() - 1);
        }
        if (getRepairTicks() <= 0 && State.NORMAL == getState())
            return;
        ServerTardis tardis = (ServerTardis) this.tardis();
        ServerAlarmHandler alarms = tardis.handler(Id.ALARMS);

        if (getRepairTicks() <= 0) {
            setState(State.NORMAL);
            alarms.enabled().set(false);
            return;
        }

        if (getState() != State.NORMAL)
            alarms.enabled().set(true);

        if (getRepairTicks() < UNSTABLE_TICK_START_THRESHOLD && State.UNSTABLE != getState() && getRepairTicks() > 0) {
            setState(State.UNSTABLE);
            alarms.enabled().set(false);
        }

        DirectedGlobalPos.Cached exteriorPosition = tardis.travel().position();
        ServerWorld exteriorWorld = exteriorPosition.getWorld();
        if (tardis.door().isOpen() && this.getState() != State.NORMAL) {
            exteriorWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, exteriorPosition.getPos().toCenterPos().x,
                    exteriorPosition.getPos().getY() + 2f, exteriorPosition.getPos().toCenterPos().z, 1, 0.05D, 0.05D,
                    0.05D, 0.01D);
        }
        if (getState() != State.TOXIC)
            return;
        if (DeltaTimeManager.isStillWaitingOnDelay(DELAY_ID_START + tardis.getUuid().toString()))
            return;
        exteriorWorld.spawnParticles(
                new DustColorTransitionParticleEffect(new Vector3f(0.75f, 0.85f, 0.75f),
                        new Vector3f(0.15f, 0.25f, 0.15f), 3),
                exteriorPosition.getPos().toCenterPos().x, exteriorPosition.getPos().getY() + 0.1f,
                exteriorPosition.getPos().toCenterPos().z, 1, 0.05D, 0.75D, 0.05D, 0.01D);
        if (DeltaTimeManager.isStillWaitingOnDelay(DELAY_ID_START + tardis.getUuid().toString()))
            return;
        if (!TardisUtil.isInteriorNotEmpty(tardis))
            return;
        int loyaltySubAmount = AITMod.RANDOM.nextInt(10, 25);
        for (ServerPlayerEntity serverPlayerEntity : TardisUtil.getPlayersInsideInterior(tardis)) {
            ItemStack stack = serverPlayerEntity.getEquippedStack(EquipmentSlot.HEAD);

            if (stack.isIn(AITModTags.Items.FULL_RESPIRATORS) || stack.isIn(AITModTags.Items.HALF_RESPIRATORS))
                continue;

            serverPlayerEntity.playSound(AITSounds.CLOISTER, 1f, 1f);
            serverPlayerEntity.damage(exteriorWorld.getDamageSources().magic(), 3f);
            serverPlayerEntity
                    .addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 100, 3, true, false, false));
            serverPlayerEntity
                    .addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 5, true, false, false));

            tardis.<LoyaltyHandler>handler(Id.LOYALTY).get(serverPlayerEntity).subtract(loyaltySubAmount);
        }
        DeltaTimeManager.createDelay(DELAY_ID_START + tardis.getUuid().toString(),
                (long) TimeUtil.secondsToMilliseconds(2));
    }

    public TardisCrashHandler() {
        super(Id.CRASH_DATA);
    }

    public State getState() {
        if (state.get() == null)
            return State.NORMAL;
        return state.get();
    }

    public void setState(State tardisState) {
        state.set(tardisState);
    }

    public Integer getRepairTicks() {
        return tardisRepairTicks.get();
    }

    public int getRepairTicksAsSeconds() {
        return (this.getRepairTicks() / 20) / 10;
    }

    public void setRepairTicks(Integer ticks) {
        if (ticks > MAX_REPAIR_TICKS) {
            setRepairTicks(MAX_REPAIR_TICKS);
            return;
        }
        tardisRepairTicks.set(ticks);
    }

    public void addRepairTicks(Integer ticks) {
        tardisRepairTicks.set(getRepairTicks() + ticks);
    }

    public enum State {
        NORMAL, UNSTABLE, TOXIC
    }
}
