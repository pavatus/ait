package loqor.ait.tardis.data;

import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;

import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.ServerTardisTravel;
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
			tickingForDanger(tardis.getExteriorPos().getWorld());
	}


	// @TODO Fix hads idk why its broken. duzo did something to the demat idk what happened lol
	public void tickingForDanger(World world) {
		if (tardis.getExteriorPos() == null) return;
		List<Entity> listOfEntities = world.getOtherEntities(null,
				new Box(tardis.getExteriorPos()).expand(3f),
				EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
		/*if(isHADSActive()) {*/
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

		ServerTardisTravel travel = (ServerTardisTravel) tardis.travel();
		TardisTravel.State state = travel.getState();

		ServerAlarmHandler alarm = tardis.getHandlers().getAlarms();

		if (isInDanger()) {
			if (state == TardisTravel.State.LANDED) {
				travel.dematerialise(false);
			}
			tardis.getHandlers().getAlarms().enable();

		} else if (alarm.isEnabled()) {
			if (state == TardisTravel.State.FLIGHT) {
				travel.materialise();
			} else if (state == TardisTravel.State.MAT)
				alarm.disable();
		}
	}

}
