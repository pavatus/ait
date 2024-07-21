package loqor.ait.tardis.control.impl.pos;

import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class IncrementControl extends Control {

	public IncrementControl() {
		super("increment");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console, boolean leftClick) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		if (!leftClick) {
			IncrementManager.nextIncrement(tardis);
		} else {
			IncrementManager.prevIncrement(tardis);
		}

		messagePlayerIncrement(player, tardis);
		return true;
	}


	@Override
	public SoundEvent getSound() {
		return AITSounds.CRANK;
	}

	private void messagePlayerIncrement(ServerPlayerEntity player, Tardis tardis) {
		Text text = Text.translatable("tardis.message.control.increment.info").append(Text.literal("" + IncrementManager.increment(tardis)));
		player.sendMessage(text, true);
	}

	@Override
	public boolean shouldHaveDelay() {
		return false;
	}
}
