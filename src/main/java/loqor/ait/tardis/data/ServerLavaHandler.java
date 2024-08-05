package loqor.ait.tardis.data;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ServerLavaHandler extends KeyedTardisComponent implements TardisTickable {

	private static final BoolProperty LAVA_OUTSIDE = new BoolProperty("lava_outside", false);
	private final BoolValue lavaOutside = LAVA_OUTSIDE.create(this);

	public ServerLavaHandler() {
		super(Id.LAVA_OUTSIDE);
	}
	@Override
	public void onLoaded() {
		lavaOutside.of(this, LAVA_OUTSIDE);
	}

	public void enable() {
		this.set(true);
	}

	public void disable() {
		this.set(false);
	}

	private void set(boolean var) {
		lavaOutside.set(var);
	}

	public boolean isEnabled() {
		return lavaOutside.get();
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
