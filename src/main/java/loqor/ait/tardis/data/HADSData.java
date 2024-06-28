package loqor.ait.tardis.data;

import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class HADSData extends TardisComponent implements TardisTickable {

	public HADSData() {
		super(Id.HADS);
	}

	public boolean isHADSActive() {
		return PropertiesHandler.getBool(tardis().getHandlers().getProperties(), PropertiesHandler.HADS_ENABLED);
	}

	public void setIsInDanger(boolean bool) {
		PropertiesHandler.set(tardis(), PropertiesHandler.IS_IN_ACTIVE_DANGER, bool);
	}

	public boolean isInDanger() {
		return PropertiesHandler.getBool(tardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_ACTIVE_DANGER);
	}

	@Override
	public void tick(MinecraftServer server) {
		if (isHADSActive())
			tickingForDanger(tardis.travel2().position().getWorld());
	}

	// @TODO Fix hads idk why its broken. duzo did something to the demat idk what happened lol
	public void tickingForDanger(World world) {
		if (tardis.travel2().position().getPos() == null)
			return;

		List<Entity> listOfEntities = world.getOtherEntities(null,
				new Box(tardis.travel2().position().getPos()).expand(3f),
				EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);

		for (Entity entity : listOfEntities) {
			if (entity instanceof CreeperEntity creeperEntity) {
				if (creeperEntity.getFuseSpeed() > 0) {
					setIsInDanger(true);
					break;
				}
			} else if (entity instanceof TntEntity) {
				setIsInDanger(true);
				break;
			}
			setIsInDanger(false);
		}

		dematerialiseWhenInDanger();
	}

	public void dematerialiseWhenInDanger() {
		ServerTardis tardis = (ServerTardis) tardis();

		TravelHandlerV2 travel = tardis.travel2();
		TravelHandler.State state = travel.getState();

		ServerAlarmHandler alarm = tardis.getHandlers().getAlarms();

		if (this.isInDanger()) {
			if (state == TravelHandler.State.LANDED)
				travel.dematerialize(); // TODO(travel): replace with a proper method

			tardis.alarm().enable();
			return;
		}

		if (state == TravelHandler.State.FLIGHT)
			travel.rematerialize();

		if (state == TravelHandler.State.MAT)
			alarm.disable();
	}
}
