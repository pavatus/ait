package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.FlightData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.Random;

public class HammerItem extends Item {
    public HammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlockEntity) {
            if (player == null) return ActionResult.PASS;
            if (consoleBlockEntity.findTardis().isEmpty()) return ActionResult.PASS;
            Tardis tardis = consoleBlockEntity.findTardis().get();
            if (!(tardis.getTravel().getState() == TardisTravel.State.FLIGHT)) {
                world.playSound(null, consoleBlockEntity.getPos(),
                        SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1f, 1.0f);
                return ActionResult.SUCCESS;
            }
            FlightData flightData = tardis.getHandlers().getFlight();
            int targetTicks = flightData.getTargetTicks();
            int current_flight_ticks = flightData.getFlightTicks();
            int added_flight_ticks = 500 * tardis.getTravel().getSpeed();
            double current_fuel = tardis.getHandlers().getFuel().getCurrentFuel();
            double max_fuel = tardis.getHandlers().getFuel().getMaxFuel();
            if (tardis.tardisHammerAnnoyance > 0) {
                added_flight_ticks -= (int) Math.round(added_flight_ticks * 0.1 * tardis.tardisHammerAnnoyance);
            }
            double estimated_fuel_cost_for_hit = added_flight_ticks / 5.0;
            if (tardis.tardisHammerAnnoyance > 0) {
                estimated_fuel_cost_for_hit += (150 * tardis.getTravel().getSpeed() * tardis.tardisHammerAnnoyance) / 7.0;
            }
            if (current_fuel + estimated_fuel_cost_for_hit > max_fuel) {
                tardis.getTravel().crash();
                tardis.getHandlers().getFuel().setCurrentFuel(0.0);
                return ActionResult.SUCCESS;
            }
            if (current_flight_ticks + added_flight_ticks < targetTicks) {
                flightData.setFlightTicks(current_flight_ticks + added_flight_ticks);
            }
            else {
                flightData.setFlightTicks(targetTicks);
            }
            tardis.getHandlers().getFuel().setCurrentFuel(current_fuel - estimated_fuel_cost_for_hit);
            tardis.tardisHammerAnnoyance ++;
            if (shouldCrashTardis(tardis.tardisHammerAnnoyance)) {
                tardis.getTravel().crash();
            } else {
                world.playSound(null, consoleBlockEntity.getPos(),
                        SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.25f * tardis.tardisHammerAnnoyance, 1.0f);
            }

            if(world.isClient()) return ActionResult.PASS;

            ((ServerWorld) world).spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX() + 0.5f, pos.getY() + 1.25,
                    pos.getZ() + 0.5f, 5 * tardis.tardisHammerAnnoyance, 0,0,0, 0.1f * tardis.tardisHammerAnnoyance);
            ((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(
                    new Vector3f(0.75f, 0.75f, 0.75f), new Vector3f(0.1f, 0.1f, 0.1f), 1), pos.getX() + 0.5f, pos.getY() + 1.25,
                    pos.getZ() + 0.5f, 5 * tardis.tardisHammerAnnoyance, 0,0,0, 0.1f * tardis.tardisHammerAnnoyance);

            world.playSound(null, consoleBlockEntity.getPos(),
                    SoundEvents.ENTITY_GLOW_ITEM_FRAME_BREAK, SoundCategory.BLOCKS, 0.25f * tardis.tardisHammerAnnoyance, 1.0f);

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public boolean shouldCrashTardis(int annoyance) {
        java.util.Random random = new Random();
        if (annoyance <= 3) {
            return false;
        }
        for (int i = 0; i < annoyance; i++) {
            if (random.nextInt(0, 10) == 1) {
                return true;
            }
        }
        return false;
    }
}
