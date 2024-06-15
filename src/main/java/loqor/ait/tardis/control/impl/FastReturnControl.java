package loqor.ait.tardis.control.impl;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class FastReturnControl extends Control {

	public FastReturnControl() {
		super("fast_return");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
		TardisTravel travel = tardis.travel();

		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player);
			return false;
		}

		boolean bl = travel.getDestination() == travel.getLastPosition(); // fixme move this to be saved in the PropertiesHandler instead as TardisTravel is too bloated rn and will be getting a rewrite

		if (travel.getLastPosition() != null) {
			travel.setDestination(bl ? travel.getPosition() : travel.getLastPosition(), tardis.flight().autoLand().get());
			messagePlayer(player, bl);

		} else {
			Text text = Text.translatable("tardis.message.control.fast_return.destination_nonexistent");
			player.sendMessage(text, true);
		}

		return true;
	}

	public void messagePlayer(ServerPlayerEntity player, boolean isLastPosition) {
		Text last_position = Text.translatable("tardis.message.control.fast_return.last_position");
		Text current_position = Text.translatable("tardis.message.control.fast_return.current_position");
		player.sendMessage((!isLastPosition ? last_position : current_position), true);
	}
}