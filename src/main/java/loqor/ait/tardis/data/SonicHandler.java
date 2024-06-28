package loqor.ait.tardis.data;

import loqor.ait.api.tardis.ArtronHolderItem;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.item.SonicItem;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class SonicHandler extends TardisComponent implements ArtronHolderItem, TardisTickable {

	public static final String HAS_CONSOLE_SONIC = "has_console_sonic";
	public static final String HAS_EXTERIOR_SONIC = "has_exterior_sonic";
	private ItemStack console; // The current sonic in the console
	private ItemStack exterior; // The current sonic in the exterior's keyhole (or any hole)

	@Exclude
	private int sfxTicks = 0; // Sonic use sound tick for exterior

	public SonicHandler() {
		super(Id.SONIC);
	}

	public boolean hasSonic(String sonic) {
		return PropertiesHandler.getBool(this.tardis().properties(), sonic);
	}

	public boolean hasConsoleSonic() {
		return hasSonic(HAS_CONSOLE_SONIC);
	}

	public boolean hasExteriorSonic() {
		return hasSonic(HAS_EXTERIOR_SONIC);
	}

	public void markHasSonic(String sonic) {
		PropertiesHandler.set(this.tardis(), sonic, true);
		this.sync();
	}

	public void clearSonicMark(String sonic) {
		PropertiesHandler.set(this.tardis(), sonic, false);
		this.sync();
	}

	/**
	 * Sets the new sonic
	 *
	 * @return The optional of the previous sonic
	 */
	public Optional<ItemStack> set(ItemStack var, boolean spawnItem, String sonic, BlockPos console) {
		Optional<ItemStack> prev = Optional.ofNullable(this.get(sonic));

		if (Objects.equals(sonic, HAS_CONSOLE_SONIC)) {
			this.console = var;
		} else {
			this.exterior = var;
		}

		if (spawnItem && prev.isPresent() && console != null)
			this.spawnItem(console, prev.get(), sonic);

		this.sync();
		return prev;
	}

	public ItemStack get(String sonic) {
		return Objects.equals(sonic, HAS_CONSOLE_SONIC) ? this.console : this.exterior;
	}

	public boolean isSonicNull(String sonic) {
		return Objects.equals(sonic, HAS_CONSOLE_SONIC) ? this.console == null : this.exterior == null;
	}

	public void clear(boolean spawnItem, String sonic, BlockPos console) {
		this.set(null, spawnItem, sonic, console);
	}

	public void spawnItem(BlockPos console, String sonic) {
		if (this.isSonicNull(sonic))
			return;

		this.spawnItem(console, this.get(sonic), sonic);
		this.clear(false, sonic, console);
	}

	public void spawnItem(BlockPos console, ItemStack sonic, String sonicWhere) {
		if (!this.hasSonic(sonicWhere))
			return;

		SonicHandler.spawnItem(sonic, Objects.equals(sonicWhere, HAS_CONSOLE_SONIC)
				? console : this.tardis.travel2().position().getPos());

		this.clearSonicMark(sonicWhere);
	}

	private static ItemEntity spawnItem(ItemStack sonic, BlockPos pos) {
		World world = TardisUtil.getTardisDimension();
		ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), sonic);

		world.spawnEntity(entity);
		return entity;
	}

	@Override
	public double getMaxFuel(ItemStack stack) {
		return SonicItem.MAX_FUEL;
	}

	@Override
	public void tick(MinecraftServer server) {
		if (this.hasSonic(HAS_CONSOLE_SONIC)) {
			ItemStack sonic = this.get(HAS_CONSOLE_SONIC);

			if (this.hasMaxFuel(sonic))
				return;

			// Safe to get as ^ that method runs the check for us
			ServerTardis tardis = (ServerTardis) this.tardis();

			if (!tardis.engine().hasPower())
				return;

			this.addFuel(1, sonic);
			tardis.fuel().removeFuel(1);
		}

		if (this.hasExteriorSonic()) {
			sfxTicks++;

			ItemStack sonic = this.get(HAS_EXTERIOR_SONIC);
			ServerTardis tardis = (ServerTardis) this.tardis();

			TardisCrashData crash = tardis.crash();
			boolean isToxic = crash.isToxic();
			boolean isUnstable = crash.isUnstable();
			int repairTicks = crash.getRepairTicks();

			if (!isToxic && !isUnstable)
				return;

			crash.setRepairTicks(repairTicks <= 0 ? 0 : repairTicks - 5);

			if (sfxTicks % SonicItem.SONIC_SFX_LENGTH == 0) {
				tardis.travel2().position().getWorld().playSound(null, tardis.travel2().position().getPos(),
						AITSounds.SONIC_USE, SoundCategory.BLOCKS, 0.5f, 1f);
			}

			this.removeFuel(1, sonic);
		} else if (sfxTicks > 0) {
			sfxTicks = 0;
		}
	}
}
