package loqor.ait.tardis.data.travel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.data.TardisCrashHandler;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.util.TardisUtil;

public sealed interface CrashableTardisTravel permits TravelHandler {

    Tardis tardis();

    TravelHandlerBase.State getState();

    void resetHammerUses();

    int getHammerUses();

    boolean isCrashing();

    void setCrashing(boolean value);

    int speed();

    void speed(int speed);

    BoolValue antigravs();

    void forceRemat();

    DirectedGlobalPos.Cached position();

    DirectedGlobalPos.Cached getProgress();

    void destination(DirectedGlobalPos.Cached cached);

    /**
     * Performs a crash for the Tardis. If the Tardis is not in flight state, the
     * crash will not be executed.
     */
    default void crash() {
        if (this.getState() != TravelHandler.State.FLIGHT || this.isCrashing())
            return;

        Tardis tardis = this.tardis();

        int power = this.speed() + this.getHammerUses() + 1;
        List<Explosion> explosions = new ArrayList<>();

        tardis.getDesktop().getConsolePos().forEach(console -> {
            TardisDesktop.playSoundAtConsole(console, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 3f, 1f);

            Explosion explosion = TardisUtil.getTardisDimension().createExplosion(null, null, null,
                    console.toCenterPos(), 3f * power, false, World.ExplosionSourceType.MOB);

            explosions.add(explosion);
        });

        if (tardis.sequence().hasActiveSequence())
            tardis.sequence().setActiveSequence(null, true);

        Random random = TardisUtil.random();

        for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis)) {
            float xVel = random.nextFloat(-2f, 3f);
            float yVel = random.nextFloat(-1f, 2f);
            float zVel = random.nextFloat(-2f, 3f);

            player.setVelocity(xVel * power, yVel * power, zVel * power);
            player.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.BLINDNESS, 20 * power, 1, true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * power,
                    (int) Math.round(0.25 * power), true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 20 * power,
                    (int) Math.round(0.25 * power), true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * power,
                    (int) Math.round(0.25 * power), true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20 * power,
                    (int) Math.round(0.75 * power), true, false, false));
            int damage = (int) Math.round(0.5 * power);

            if (!explosions.isEmpty()) {
                player.damage(TardisUtil.getTardisDimension().getDamageSources().explosion(explosions.get(0)), damage);
            } else {
                player.damage(TardisUtil.getTardisDimension().getDamageSources().generic(), damage);
            }
        }

        tardis.door().setLocked(true);
        tardis.alarm().enabled().set(true);
        this.antigravs().set(false);

        this.speed(0);
        tardis.removeFuel(500 * power);

        this.resetHammerUses();

        this.setCrashing(true);
        this.destination(TravelUtil.jukePos(this.getProgress(), 10, 100, power));

        this.forceRemat();

        int repairTicks = 1000 * power;
        tardis.crash().setRepairTicks(repairTicks);

        if (repairTicks > TardisCrashHandler.UNSTABLE_TICK_START_THRESHOLD) {
            tardis.crash().setState(TardisCrashHandler.State.TOXIC);
        } else {
            tardis.crash().setState(TardisCrashHandler.State.UNSTABLE);
        }

        TardisEvents.CRASH.invoker().onCrash(tardis);
    }
}
