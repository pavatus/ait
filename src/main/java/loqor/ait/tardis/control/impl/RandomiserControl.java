package loqor.ait.tardis.control.impl;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.control.impl.pos.IncrementManager;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelUtil;
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
		TravelHandler travel = tardis.travel2();

		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		world.getServer().execute(() -> {
			tardis.travel2().destination(TravelUtil.randomPos(tardis, 10, IncrementManager.increment(tardis)));
			tardis.removeFuel(0.1d * IncrementManager.increment(tardis) * (tardis.tardisHammerAnnoyance + 1));

			messagePlayer(player, travel);
		});

		return true;
	}

	@Override
	public long getDelayLength() {
		return 2000L;
	}

	private void messagePlayer(ServerPlayerEntity player, TravelHandler travel) {
		DirectedGlobalPos.Cached dest = travel.destination();
		BlockPos pos = dest.getPos();

		Text text = Text.translatable("tardis.message.control.randomiser.destination").append(Text.literal(pos.getX() + " | " + pos.getY() + " | " + pos.getZ()));
		player.sendMessage(text, true);
	}
}
