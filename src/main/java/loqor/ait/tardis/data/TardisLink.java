package loqor.ait.tardis.data;

import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.SerialDimension;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// todo move everything over to TardisComponent
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

	public AbsoluteBlockPos.Directed getDoorPos() {
		return tardis().getDesktop() != null && tardis().getDesktop().getInteriorDoorPos() != null ?
				tardis().getDesktop().getInteriorDoorPos() :
				new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), 0);
	}

	public AbsoluteBlockPos.Directed getExteriorPos() {
		return tardis().getTravel() != null ?
				tardis().getTravel().getPosition() :
				new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), 0);
	}

	public static boolean isClient() {
		return TardisUtil.isClient();
	}

	public static boolean isServer() {
		return TardisUtil.isServer();
	}
}
