package loqor.ait.tardis.control.sequences;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

// should this be an interface?
public class Sequence {
	public Identifier id() {
		return new Identifier(AITMod.MOD_ID, "sequence");
	}

	/**
	 * A list of controls needed to execute the sequence, in the order they should be executed
	 */
	public List<Control> getControls() {
		return new ArrayList<>();
	}

	public Long timeToFail() {
		return 0L;
	}

	/**
	 * @param recent Compares the recent controls to this sequence, if everything matches then it is finished
	 */
	public boolean isFinished(RecentControls recent) {
        /*if(recent.size() > this.getControls().size())
            return false;*/
		return recent.equals(this.getControls());
	}

	public void execute(Tardis tardis) {
	}

	public void executeMissed(Tardis tardis) {
	}

	public Text sequenceStartMessage() {
		return Text.of("");
	}

	public boolean wasMissed(RecentControls recent, int ticks) {
		if (recent.size() >= this.getControls().size() && recent != this.getControls()) {
			recent.clear();
		}
		return ticks >= this.timeToFail()/* || (recent.size() > this.getControls().size())*/;
	}

	public boolean controlPartOfSequence(Control control) {
		return this.getControls().contains(control);
	}

	public void sendMessageToInteriorPlayers(List<ServerPlayerEntity> playersInInterior) {
		if (playersInInterior.isEmpty()) return;
		for (ServerPlayerEntity player : playersInInterior) {
			player.sendMessage(this.sequenceStartMessage(), true);
		}
	}

	public interface ExecuteSequence {
		void run(Tardis tardis);
	}

	public static class Builder extends Sequence {

		private final Identifier id;
		private final List<Control> controls;
		private final ExecuteSequence execute;
		private final ExecuteSequence executeMissed;
		private final Long timeToFail;
		private final Text sequenceStartMessage;

		private Builder(Identifier id, ExecuteSequence execute, ExecuteSequence executeMissed, Long timeToFail, Text sequenceStartMessage, Control... controls) {
			this.id = id;
			this.controls = List.of(controls);
			this.execute = execute;
			this.executeMissed = executeMissed;
			this.timeToFail = timeToFail;
			this.sequenceStartMessage = sequenceStartMessage;
		}

		public static Sequence create(Identifier id, ExecuteSequence execute, ExecuteSequence executeMissed, Long timeToFail, Text sequenceStartMessage, Control... controls) {
			return new Builder(id, execute, executeMissed, timeToFail, sequenceStartMessage, controls);
		}

		@Override
		public Identifier id() {
			return this.id;
		}

		@Override
		public List<Control> getControls() {
			return this.controls;
		}

		@Override
		public void execute(Tardis tardis) {
			this.execute.run(tardis);
		}

		@Override
		public void executeMissed(Tardis tardis) {
			this.executeMissed.run(tardis);
		}

		@Override
		public Long timeToFail() {
			return this.timeToFail;
		}

		@Override
		public Text sequenceStartMessage() {
			return this.sequenceStartMessage;
		}
	}
}
