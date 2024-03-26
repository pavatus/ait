package loqor.ait.registry;

import loqor.ait.AITMod;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.control.impl.*;
import loqor.ait.tardis.control.impl.pos.IncrementControl;
import loqor.ait.tardis.control.impl.pos.XControl;
import loqor.ait.tardis.control.impl.pos.YControl;
import loqor.ait.tardis.control.impl.pos.ZControl;
import loqor.ait.tardis.control.impl.waypoint.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ControlRegistry {
	public static final SimpleRegistry<Control> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<Control>ofRegistry(new Identifier(AITMod.MOD_ID, "control"))).buildAndRegister();

	public static Control register(Control control) {
		return Registry.register(REGISTRY, control.id(), control);
	}

	/**
	 * Finds a control by its ID ( name ) NOT its Identifier
	 * @param id the id to look for
	 * @return the control found
	 */
	public static Optional<Control> fromId(String id) {
		// this will need changing when AIT only controls is changed
		return Optional.ofNullable(REGISTRY.get(new Identifier(AITMod.MOD_ID, id)));
	}

	public static void init() {
		// Pain.

		register(new AntiGravsControl());
		register(new AutoPilotControl());
		register(new CloakControl());
		register(new DimensionControl());
		register(new DirectionControl());
		register(new DoorControl());
		register(new DoorLockControl());
		register(new FastReturnControl());
		register(new HADSControl());
		register(new HailMaryControl());
		register(new HandBrakeControl());
		register(new LandTypeControl());
		register(new MonitorControl());
		register(new PowerControl());
		register(new RandomiserControl());
		register(new RefuelerControl());
		register(new SecurityControl());
		register(new SiegeModeControl());
		register(new SonicPortControl());
		register(new TelepathicControl());
		register(new ThrottleControl());
		register(new VisualiserControl());

		// Waypoints
		register(new EjectWaypointControl());
		register(new GotoWaypointControl());
		register(new LoadWaypointControl());
		register(new MarkWaypointControl());
		register(new SetWaypointControl());

		// Pos
		register(new IncrementControl());
		register(new XControl());
		register(new YControl());
		register(new ZControl());
	}
}
