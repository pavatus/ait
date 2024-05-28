package loqor.ait.tardis.data;

import loqor.ait.api.tardis.ArtronHolderItem;
import loqor.ait.core.AITSounds;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisLink;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;

import java.util.Objects;
import java.util.Optional;

public class SonicHandler extends TardisLink implements ArtronHolderItem {
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
	 * @param var
	 * @return The optional of the previous sonic
	 */
	public Optional<ItemStack> set(ItemStack var, boolean spawnItem, String sonic) {
		Optional<ItemStack> prev = Optional.ofNullable(this.get(sonic));
		if (Objects.equals(sonic, HAS_CONSOLE_SONIC)) {
			this.console = var;
		} else {
			this.exterior = var;
		}

		if (spawnItem && prev.isPresent()) {
			this.spawnItem(prev.get(), sonic);
		}

		this.sync();

		return prev;
	}

	public ItemStack get(String sonic) {
		return Objects.equals(sonic, HAS_CONSOLE_SONIC) ? this.console : this.exterior;
	}

	public boolean isSonicNull(String sonic) {
		return Objects.equals(sonic, HAS_CONSOLE_SONIC) ? this.console == null : this.exterior == null;
	}

	public void clear(boolean spawnItem, String sonic) {
		this.set(null, spawnItem, sonic);
	}

	public void spawnItem(String sonic) {
		if (this.isSonicNull(sonic)) return;

		spawnItem(this.get(sonic), sonic);
		this.clear(false, sonic);
	}

	public void spawnItem(ItemStack sonic, String sonicWhere) {
		if (!this.hasSonic(sonicWhere)) return;

		Tardis tardis = this.tardis();

		if (Objects.equals(sonicWhere, HAS_CONSOLE_SONIC) && tardis.getDesktop().findCurrentConsole().isEmpty()) return;

		spawnItem(sonic, Objects.equals(sonicWhere, HAS_CONSOLE_SONIC) ? tardis.getDesktop().findCurrentConsole().get().position() : tardis.getExterior().getExteriorPos());
		this.clearSonicMark(sonicWhere);
	}

	public static ItemEntity spawnItem(ItemStack sonic, AbsoluteBlockPos pos) {
		ItemEntity entity = new ItemEntity(pos.getWorld(), pos.getX(), pos.getY(), pos.getZ(), sonic);
		pos.getWorld().spawnEntity(entity);
		return entity;
	}

	@Override
	public double getMaxFuel(ItemStack stack) {
		return SonicItem.MAX_FUEL;
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);

		if (this.hasSonic(HAS_CONSOLE_SONIC)) {
			ItemStack sonic = this.get(HAS_CONSOLE_SONIC);
			if (this.hasMaxFuel(sonic)) return;
			// Safe to get as ^ that method runs the check for us
			ServerTardis tardis = (ServerTardis) this.tardis();
			if (!tardis.engine().hasPower()) return;
			this.addFuel(1, sonic);
			tardis.fuel().removeFuel(1);
		}
		if (this.hasExteriorSonic()) {
			sfxTicks++;

			ItemStack sonic = this.get(HAS_EXTERIOR_SONIC);
			ServerTardis tardis = (ServerTardis) this.tardis();
			if (tardis.crash().getRepairTicks() <= 0) {
				this.spawnItem(this.get(HAS_EXTERIOR_SONIC), HAS_EXTERIOR_SONIC);
				return;
			}
			TardisCrashData crash = tardis.crash();
			boolean isToxic = crash.isToxic();
			boolean isUnstable = crash.isUnstable();
			int repairTicks = crash.getRepairTicks();

			if (!isToxic && !isUnstable) return;

			crash.setRepairTicks(repairTicks <= 0 ? 0 : repairTicks - 5);

			if (sfxTicks % SonicItem.SONIC_SFX_LENGTH == 0) {
				tardis.getExterior().getExteriorPos().getWorld().playSound(null, tardis.getExterior().getExteriorPos(),
						AITSounds.SONIC_USE, SoundCategory.BLOCKS, 0.5f, 1f);
			}

			this.removeFuel(1, sonic);
		}
		else if (sfxTicks > 0) {
			sfxTicks = 0;
		}
	}
}
