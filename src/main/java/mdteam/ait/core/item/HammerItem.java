package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.FlightData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
            if (consoleBlockEntity.getTardis().isEmpty()) return ActionResult.PASS;
            Tardis tardis = consoleBlockEntity.getTardis().get();
            if (!(tardis.getTravel().getState() == TardisTravel.State.FLIGHT)) return ActionResult.PASS;
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
            flightData.setFlightTicks(Math.min(current_flight_ticks + added_flight_ticks, targetTicks));
            tardis.getHandlers().getFuel().setCurrentFuel(current_fuel - estimated_fuel_cost_for_hit);
            tardis.tardisHammerAnnoyance ++;
            if (tardis.tardisHammerAnnoyance >= 7) {
                tardis.getTravel().crash();
            } else {
                world.playSound(null, consoleBlockEntity.getPos(),
                        SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.25f * tardis.tardisHammerAnnoyance, 1.0f);
            }

            ((ServerWorld) world).spawnParticles(ParticleTypes.ELECTRIC_SPARK, pos.getX() + 0.5f, pos.getY() + 1.25,
                    pos.getZ() + 0.5f, tardis.tardisHammerAnnoyance, 0,0,0, 0.025f);

            world.playSound(null, consoleBlockEntity.getPos(),
                    SoundEvents.ENTITY_GLOW_ITEM_FRAME_BREAK, SoundCategory.BLOCKS, 0.25f * tardis.tardisHammerAnnoyance, 1.0f);

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
