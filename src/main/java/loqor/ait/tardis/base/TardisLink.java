package loqor.ait.tardis.base;

import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.SerialDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

// todo move everything over to TardisComponent

/**
 * @deprecated Use {@link TardisComponent} instead. Implement {@link TardisTickable} if its functionality is needed.
 */
@Deprecated
public abstract class TardisLink extends TardisComponent implements TardisTickable {

	public TardisLink(Id id) {
		super(id);
	}

	@Override
	public void tick(ServerWorld world) {
		// Implementation of the server-side tick logic
	}

	@Override
	public void tick(MinecraftServer server) {
		// Implementation of the server-side tick logic
	}

	@Override
	public void tick(MinecraftClient client) {
		// Implementation of the client-side tick logic
	}

	@Override
	public void startTick(MinecraftServer server) {
		// Implementation of the server-side tick logic when it starts
	}

	// TODO move this to the correct handler
	@Deprecated
	public AbsoluteBlockPos.Directed getDoorPos() {
		return tardis().getDesktop() != null && tardis().getDesktop().getInteriorDoorPos() != null ?
				tardis().getDesktop().getInteriorDoorPos() :
				new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), 0);
	}

	@Deprecated
	public AbsoluteBlockPos.Directed getExteriorPos() {
		return tardis().travel() != null ?
				tardis().travel().getPosition() :
				new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), 0);
	}
}
