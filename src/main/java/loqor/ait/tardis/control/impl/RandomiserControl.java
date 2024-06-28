package loqor.ait.tardis.control.impl;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.control.impl.pos.IncrementManager;
import loqor.ait.tardis.data.TravelHandlerV2;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class RandomiserControl extends Control {

	public RandomiserControl() {
		super("randomiser");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		TravelHandlerV2 travel = tardis.travel2();

		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		tardis.travel2().destination(randomiseDestination(tardis, 10));
		tardis.removeFuel(0.1d * IncrementManager.increment(tardis) * (tardis.tardisHammerAnnoyance + 1));

		messagePlayer(player, travel);
		return true;
	}

	// TODO(travel): improve the performance of this
	public static DirectedGlobalPos.Cached randomiseDestination(Tardis tardis, int limit) {
		TravelHandlerV2 travel = tardis.travel2();
		int increment = IncrementManager.increment(tardis);

		DirectedGlobalPos.Cached dest = travel.destination();
		ServerWorld world = dest.getWorld();

		for (int i = 0; i <= limit; i++) {
			dest = dest.pos(
					world.random.nextBoolean() ? world.random.nextInt(increment) : -world.random.nextInt(increment), 0,
					world.random.nextBoolean() ? world.random.nextInt(increment) : -world.random.nextInt(increment)
			);

			if (i >= limit)
				return dest;
		}

		return dest;
	}

	@Override
	public long getDelayLength() {
		return 2000L;
	}

	private void messagePlayer(ServerPlayerEntity player, TravelHandlerV2 travel) {
		DirectedGlobalPos.Cached dest = travel.destination();
		BlockPos pos = dest.getPos();

		Text text = Text.translatable("tardis.message.control.randomiser.destination").append(Text.literal(pos.getX() + " | " + pos.getY() + " | " + pos.getZ()));
		player.sendMessage(text, true);
	}
}
