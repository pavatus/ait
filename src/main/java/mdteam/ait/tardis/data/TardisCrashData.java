package mdteam.ait.tardis.data;

import com.neptunedevelopmentteam.neptunelib.utils.DeltaTimeManager;
import com.neptunedevelopmentteam.neptunelib.utils.TimeUtil;
import mdteam.ait.AITMod;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;


public class TardisCrashData extends TardisLink{
    public static final String TARDIS_RECOVERY_STATE = "tardis_recovery_state";
    public static final String TARDIS_REPAIR_TICKS = "tardis_recovery_ticks";

    private static final String DELAY_ID_START = AITMod.MOD_ID + "-tardiscrashrecoverydelay-";
    public static final Integer UNSTABLE_TICK_START_THRESHOLD = 2_400;
    public static final Integer MAX_REPAIR_TICKS = 12_000;

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
        if (this.findTardis().isEmpty()) return;
        if (getRepairTicks() > 0) {
            setRepairTicks(getRepairTicks() - 1);
        }
        if (getRepairTicks() == 0 && State.NORMAL == getState()) return;
        ServerTardis tardis = (ServerTardis) this.findTardis().get();
        if (getRepairTicks() < UNSTABLE_TICK_START_THRESHOLD && State.UNSTABLE != getState()) {
            setState(State.UNSTABLE);
            tardis.getHandlers().getAlarms().disable();
        }
        if (getRepairTicks() == 0) {
            setState(State.NORMAL);
            tardis.getHandlers().getAlarms().disable();
        }
        if (!(getState() == State.TOXIC)) return;
        if (DeltaTimeManager.isStillWaitingOnDelay(DELAY_ID_START + tardis.getUuid().toString())) return;
        AbsoluteBlockPos exteriorPosition = tardis.getTravel().getExteriorPos();
        ServerWorld exteriorWorld = (ServerWorld) exteriorPosition.getWorld();
        exteriorWorld.spawnParticles(ParticleTypes.CLOUD,
                exteriorPosition.getX(), exteriorPosition.getY(), exteriorPosition.getZ(),
                10,
                1.0D, 0.0D, 1.0D, 0.5D
                );
        ServerWorld tardisWorld = (ServerWorld) TardisUtil.getTardisDimension();
        tardis.getDesktop().getConsoles().forEach(console -> {
            tardisWorld.spawnParticles(ParticleTypes.CLOUD,
                    console.position().getX(), console.position().getY(), console.position().getZ(),
                    10,
                    1.0D, 0.0D, 1.0D, 0.5D
            );
        });
        if (!TardisUtil.isInteriorNotEmpty(tardis)) return;
        for (ServerPlayerEntity serverPlayerEntity : TardisUtil.getPlayersInInterior(tardis)) {
            serverPlayerEntity.playSound(AITSounds.CLOISTER, 1f, 1f);
            serverPlayerEntity.damage(exteriorWorld.getDamageSources().magic(), 2f);
            serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 5, true, false, false));
            serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 100, 3, true, false, false));
            serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 5, true, false, false));
        }
        DeltaTimeManager.createDelay(DELAY_ID_START + tardis.getUuid().toString(), (long) TimeUtil.secondsToMilliseconds(3));
    }

    public TardisCrashData(Tardis tardis) {
        super(tardis, "crash");
    }

    public State getState() {
        if (findTardis().isEmpty()) return State.NORMAL;
        return State.valueOf((String) PropertiesHandler.get(findTardis().get().getHandlers().getProperties(), TARDIS_RECOVERY_STATE));
    }

    public void setState(State state) {
        if (findTardis().isEmpty()) return;
        PropertiesHandler.set(findTardis().get().getHandlers().getProperties(), TARDIS_RECOVERY_STATE, state);
    }

    public Integer getRepairTicks() {
        if (findTardis().isEmpty()) return 0;
        return PropertiesHandler.getInt(findTardis().get().getHandlers().getProperties(), TARDIS_REPAIR_TICKS);
    }

    public void setRepairTicks(Integer ticks) {
        if (findTardis().isEmpty()) return;
        if (ticks > MAX_REPAIR_TICKS) {
            setRepairTicks(MAX_REPAIR_TICKS);
            return;
        }
        PropertiesHandler.set(findTardis().get().getHandlers().getProperties(), TARDIS_REPAIR_TICKS, ticks);
    }

    public void addRepairTicks(Integer ticks) {
        if (findTardis().isEmpty()) return;
        PropertiesHandler.set(findTardis().get().getHandlers().getProperties(), TARDIS_REPAIR_TICKS, getRepairTicks() + ticks);
    }

    public enum State {
        NORMAL,
        UNSTABLE,
        TOXIC
    }
}
