package loqor.ait.tardis.data;

import loqor.ait.api.tardis.ArtronHolderItem;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.item.SonicItem;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class SonicHandler extends KeyedTardisComponent implements ArtronHolderItem, TardisTickable {

	private static final BoolProperty HAS_CONSOLE_SONIC_PROPERTY = new BoolProperty("has_console_sonic", false);
	private final BoolValue hasConsoleSonic = HAS_CONSOLE_SONIC_PROPERTY.create(this);
	private static final BoolProperty HAS_EXTERIOR_SONIC_PROPERTY = new BoolProperty("has_exterior_sonic", false);
	private final BoolValue hasExteriorSonic = HAS_EXTERIOR_SONIC_PROPERTY.create(this);
	private ItemStack console; // The current sonic in the console
	private ItemStack exterior; // The current sonic in the exterior's keyhole

	@Exclude
	private int sfxTicks = 0; // Sonic use sound tick for exterior

	public SonicHandler() {
		super(Id.SONIC);
	}

	@Override
	public void onLoaded() {
		hasConsoleSonic.of(this, HAS_CONSOLE_SONIC_PROPERTY);
		hasExteriorSonic.of(this, HAS_EXTERIOR_SONIC_PROPERTY);
	}

	public boolean hasConsoleSonic() {
		return hasConsoleSonic.get();
	}

	public ItemStack getConsoleSonic() {
		return console;
	}

	public boolean hasExteriorSonic() {
		return hasExteriorSonic.get();
	}

	public ItemStack getExteriorSonic() {
		return exterior;
	}

	public void markHasConsoleSonic() {
		hasConsoleSonic.set(true);
	}

	public void clearConsoleSonicMark() {
		hasConsoleSonic.set(false);
	}

	public void markHasExteriorSonic() {
		hasExteriorSonic.set(true);
	}

	public void clearExteriorSonicMark() {
		hasExteriorSonic.set(false);
	}

	/**
	 * Sets the new sonic
	 */
	public void setConsoleSonic(ItemStack var, boolean spawnItem, BlockPos consolePos) {
		Optional<ItemStack> prev = Optional.ofNullable(console);

		this.console = var;

		if (spawnItem && prev.isPresent() && console != null)
			spawnItem(consolePos, prev.get());
	}

	public void setExteriorSonic(ItemStack var, boolean spawnItem, BlockPos exteriorPos) {
		Optional<ItemStack> prev = Optional.ofNullable(exterior);

		this.exterior = var;

		if (spawnItem && prev.isPresent() && console != null)
			spawnItem(exteriorPos, prev.get());
	}

	public void clearExterior(boolean spawnItem, BlockPos exterior) {
		this.setExteriorSonic(null, spawnItem, exterior);
	}

	public void spawnConsoleItem(BlockPos consolePos) {

		SonicHandler.spawnItem(consolePos, console);

		this.clearConsoleSonicMark();
	}

	public static void spawnItem(BlockPos pos, ItemStack sonic) {
		World world = TardisUtil.getTardisDimension();
		ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), sonic);

		world.spawnEntity(entity);
	}

	@Override
	public double getMaxFuel(ItemStack stack) {
		return SonicItem.MAX_FUEL;
	}

	@Override
	public void tick(MinecraftServer server) {
		if (this.hasConsoleSonic()) {
			ItemStack sonic = this.console;

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

			ItemStack sonic = this.exterior;
			ServerTardis tardis = (ServerTardis) this.tardis();

			TardisCrashHandler crash = tardis.crash();
			boolean isToxic = crash.isToxic();
			boolean isUnstable = crash.isUnstable();
			int repairTicks = crash.getRepairTicks();

			if (!isToxic && !isUnstable)
				return;

			crash.setRepairTicks(repairTicks <= 0 ? 0 : repairTicks - 5);

			if (sfxTicks % SonicItem.SONIC_SFX_LENGTH == 0) {
				tardis.travel().position().getWorld().playSound(null, tardis.travel().position().getPos(),
						AITSounds.SONIC_USE, SoundCategory.BLOCKS, 0.5f, 1f);
			}

			this.removeFuel(1, sonic);
		} else if (sfxTicks > 0) {
			sfxTicks = 0;
		}
	}
}
