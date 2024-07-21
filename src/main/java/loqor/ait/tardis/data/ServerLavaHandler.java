package loqor.ait.tardis.data;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ServerLavaHandler extends TardisComponent implements TardisTickable {

	public ServerLavaHandler() {
		super(Id.LAVA_OUTSIDE);
	}

	public void enable() {
		this.set(true);
	}

	public void disable() {
		this.set(false);
	}

	private void set(boolean var) {
		PropertiesHandler.set(this.tardis(), PropertiesHandler.LAVA_OUTSIDE, var);
	}

	public boolean isEnabled() {
		return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), PropertiesHandler.LAVA_OUTSIDE);
	}

	public void toggle() {
		if (isEnabled()) disable();
		else enable();
	}

	@Override
	public void tick(MinecraftServer server) {
		if (this.isEnabled() &&
				tardis.travel().position().getWorld() != null && !isInLava()) {
			this.disable();
			return;
		}

		if (this.isEnabled())
			return;

		if (tardis.travel().getState() != TravelHandlerBase.State.LANDED)
			return;

		if (tardis.travel().position().getWorld() != null && isInLava()) {
			this.enable();
		}
	}

	public boolean isInLava() {
		Tardis tardis = this.tardis();

		if (tardis.travel().position().getWorld() == null) return false;

		World world = tardis.travel().position().getWorld();
		BlockPos tardisPos = tardis.travel().position().getPos();

		for (int xOffset = -1; xOffset <= 1; xOffset++) {
			for (int yOffset = -1; yOffset <= 1; yOffset++) {
				BlockPos blockPos = tardisPos.add(xOffset, 0, yOffset);
				if (world.getBlockState(blockPos).getBlock() == Blocks.LAVA) {
					return true;
				}
			}
		}

		return false;
	}
}
