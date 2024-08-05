package loqor.ait.tardis.data;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
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

public class HadsHandler extends KeyedTardisComponent implements TardisTickable {

	private static final BoolProperty HADS_ENABLED = new BoolProperty("enabled", true);
	private static final BoolProperty IS_IN_ACTIVE_DANGER = new BoolProperty("is_in_active_danger", false);

	private final BoolValue enabled = HADS_ENABLED.create(this);
	private final BoolValue inDanger = IS_IN_ACTIVE_DANGER.create(this);

	public HadsHandler() {
		super(Id.HADS);
	}

	public boolean isActive() {
		return enabled.get();
	}

	public void setIsInDanger(boolean bool) {
		inDanger.set(bool);
	}

	public boolean isInDanger() {
		return inDanger.get();
	}

	@Override
	public void onLoaded() {
		enabled.of(this, HADS_ENABLED);
		inDanger.of(this, IS_IN_ACTIVE_DANGER);
	}

	@Override
	public void tick(MinecraftServer server) {
		if (isActive())
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

			alarm.enabled().set(true);
			return;
		}

		if (state == TravelHandlerBase.State.FLIGHT)
			travel.rematerialize();

		if (state == TravelHandlerBase.State.MAT)
			alarm.enabled().set(false);
	}

    public BoolValue enabled() {
		return enabled;
    }
}
