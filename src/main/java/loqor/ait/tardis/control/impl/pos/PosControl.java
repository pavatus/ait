package loqor.ait.tardis.control.impl.pos;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.TravelHandlerV2;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public abstract class PosControl extends Control {

	private final PosType type;

	public PosControl(PosType type, String id) {
		super(id);
		this.type = type;
	}

	public PosControl(PosType type) {
		this(type, type.asString());
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console, boolean leftClick) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		TravelHandlerV2 travel = tardis.travel2();
		DirectedGlobalPos.Cached destination = travel.destination();

		BlockPos pos = this.type.add(destination.getPos(), (leftClick) ? -IncrementManager.increment(tardis)
				: IncrementManager.increment(tardis), destination.getWorld());

		travel.forceDestination(destination.pos(pos));
		messagePlayerDestination(player, travel);
		return true;
	}

	private void messagePlayerDestination(ServerPlayerEntity player, TravelHandlerV2 travel) {
		DirectedGlobalPos.Cached globalPos = travel.destination();
		BlockPos pos = globalPos.getPos();

		Text text = Text.translatable("tardis.message.control.randomiser.poscontrol").append(Text.literal(" " + pos.getX() + " | " + pos.getY() + " | " + pos.getZ()));
		player.sendMessage(text, true);
	}

	@Override
	public boolean shouldHaveDelay() {
		return false;
	}
}
