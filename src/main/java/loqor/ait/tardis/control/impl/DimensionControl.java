package loqor.ait.tardis.control.impl;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class DimensionControl extends Control {

	public DimensionControl() {
		super("dimension");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		TravelHandler travel = tardis.travel();
		DirectedGlobalPos.Cached dest = travel.destination();

		List<ServerWorld> dims = TardisUtil.getDimensions(world.getServer());
		int current = dims.indexOf(dest.getWorld() == null ? World.OVERWORLD : dest.getWorld());

		int next;
		if (!player.isSneaking()) {
			next = ((current + 1) > dims.size() - 1) ? 0 : current + 1;
		} else {
			next = ((current - 1) < 0) ? dims.size() - 1 : current - 1;
		}



		// FIXME we should make it so that once the ender dragon is defeated, the end is unlocked; also make that a config option as well for the server. - Loqor

		ServerWorld destWorld = dims.get(next);

		travel.destination(cached -> cached.world(destWorld));
		messagePlayer(player, destWorld);

		return true;
	}

	private void messagePlayer(ServerPlayerEntity player, ServerWorld world) {
		Text message = Text.translatable("message.ait.tardis.control.dimension.info").append(
				WorldUtil.worldText(world.getRegistryKey())
		);

		player.sendMessage(message, true);
	}
}
