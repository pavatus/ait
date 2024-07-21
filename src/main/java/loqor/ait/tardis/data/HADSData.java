package loqor.ait.tardis.data;

import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
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
			tickingForDanger(tardis.travel().position().getWorld());
	}

	// @TODO Fix hads idk why its broken. duzo did something to the demat idk what happened lol
	public void tickingForDanger(World world) {
		List<Entity> listOfEntities = world.getOtherEntities(null,
				new Box(tardis.travel().position().getPos()).expand(3f),
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

		TravelHandler travel = tardis.travel();
		TravelHandlerBase.State state = travel.getState();

		ServerAlarmHandler alarm = tardis.alarm();

		if (this.isInDanger()) {
			if (state == TravelHandlerBase.State.LANDED)
				travel.dematerialize();

			tardis.alarm().enable();
			return;
		}

		if (state == TravelHandlerBase.State.FLIGHT)
			travel.rematerialize();

		if (state == TravelHandlerBase.State.MAT)
			alarm.disable();
	}
}
