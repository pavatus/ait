package loqor.ait.tardis.data;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.AbsoluteBlockPos;
import loqor.ait.tardis.util.SerialDimension;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.AbstractTardisComponent;
import loqor.ait.tardis.TardisTickable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// todo move everything over to TardisComponent
public abstract class TardisLink extends AbstractTardisComponent implements TardisTickable {
	public TardisLink(Tardis tardis, String id) {
		super(tardis, id);
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
		if (findTardis().isEmpty())
			return new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), Direction.NORTH);
		Tardis tardis = findTardis().get();
		return tardis.getDesktop() != null && tardis.getDesktop().getInteriorDoorPos() != null ?
				tardis.getDesktop().getInteriorDoorPos() :
				new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), Direction.NORTH);
	}

	public AbsoluteBlockPos.Directed getExteriorPos() {
		if (findTardis().isEmpty()) return null;
		Tardis tardis = findTardis().get();
		return tardis.getTravel() != null ?
				tardis.getTravel().getPosition() :
				new AbsoluteBlockPos.Directed(0, 0, 0, new SerialDimension(World.OVERWORLD.getValue().toString()), Direction.NORTH);
	}

	public static boolean isClient() {
		return TardisUtil.isClient();
	}

	public static boolean isServer() {
		return TardisUtil.isServer();
	}
}
