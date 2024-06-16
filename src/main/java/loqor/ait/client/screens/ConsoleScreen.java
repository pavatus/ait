package loqor.ait.client.screens;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public abstract class ConsoleScreen extends TardisScreen {

	protected final BlockPos console;

	protected ConsoleScreen(Text title, UUID tardis, BlockPos console) {
		super(title, tardis);

		this.console = console;
	}

	public BlockPos getConsole() {
		return console;
	}
}
