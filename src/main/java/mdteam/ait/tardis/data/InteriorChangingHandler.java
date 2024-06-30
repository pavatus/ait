package mdteam.ait.tardis.data;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.core.util.DeltaTimeManager;
import mdteam.ait.registry.CategoryRegistry;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.data.properties.PropertiesHolder;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Random;

public class InteriorChangingHandler extends TardisLink {
	private static final int WARN_TIME = 10 * 40;
	public static final String IS_REGENERATING = "is_regenerating";
	public static final String GENERATING_TICKS = "generating_ticks";
	public static final String QUEUED_INTERIOR = "queued_interior";
	public static final Identifier CHANGE_DESKTOP = new Identifier(AITMod.MOD_ID, "change_desktop");
	private static Random random;
	private int ticks; // this shouldnt rly be stored in propertieshandler, will cause packet spam

	public InteriorChangingHandler(Tardis tardis) {
		super(tardis, "interior-changing");
	}

	private void setGenerating(boolean var) {
		if (this.findTardis().isEmpty()) return;
		PropertiesHandler.set(this.findTardis().get(), IS_REGENERATING, var);
	}

	public boolean isGenerating() {
		if (this.findTardis().isEmpty()) return false;
		return PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), IS_REGENERATING);
	}

	private void setTicks(int var) {
		this.ticks = var;
	}

	private void addTick() {
		setTicks(getTicks() + 1);
	}

	public int getTicks() {
		return this.ticks;
	}

	public boolean hasReachedMax() {
		return getTicks() >= WARN_TIME;
	}

	private void setQueuedInterior(TardisDesktopSchema schema) {
		if (this.findTardis().isEmpty()) return;
		PropertiesHandler.set(this.findTardis().get(), QUEUED_INTERIOR, schema.id());
	}

	public TardisDesktopSchema getQueuedInterior() {
		if (this.findTardis().isEmpty()) return null;
		return DesktopRegistry.getInstance().get(PropertiesHandler.getIdentifier(this.findTardis().get().getHandlers().getProperties(), QUEUED_INTERIOR));
	}

	public void queueInteriorChange(TardisDesktopSchema schema) {
		if (findTardis().isEmpty()) return;

		Tardis tardis = this.findTardis().get();

		if (!tardis.isGrowth() && !tardis.hasPower() && !tardis.getHandlers().getCrashData().isToxic()) return;

		if (tardis.getHandlers().getFuel().getCurrentFuel() < 5000 && !(tardis.isGrowth() && tardis.hasGrowthDesktop())) {
			for (PlayerEntity player : TardisUtil.getPlayersInInterior(tardis)) {
				player.sendMessage(Text.translatable("tardis.message.interiorchange.not_enough_fuel").formatted(Formatting.RED), true);
				return;
			}
		}

		setQueuedInterior(schema);
		setTicks(0);
		setGenerating(true);
		DeltaTimeManager.createDelay("interior_change-" + tardis.getUuid().toString(), 100L);
		tardis.getHandlers().getAlarms().enable();

		tardis.getDesktop().clearConsoles();

		if (!(tardis.hasGrowthDesktop()))
			tardis.removeFuel(5000 * (tardis.tardisHammerAnnoyance + 1));
	}

	private void onCompletion() {
		if (this.findTardis().isEmpty()) return;

		Tardis tardis = this.findTardis().get();
		PropertiesHolder properties = tardis.getHandlers().getProperties();

		setGenerating(false);
		clearedOldInterior = false;

		tardis.getHandlers().getAlarms().disable();

		boolean previouslyLocked = PropertiesHandler.getBool(properties, PropertiesHandler.PREVIOUSLY_LOCKED);
		DoorData.lockTardis(previouslyLocked, tardis, null, false);

		if (tardis.hasGrowthExterior()) {
			PropertiesHandler.set(tardis, PropertiesHandler.HANDBRAKE, false);
			PropertiesHandler.set(tardis, PropertiesHandler.AUTO_LAND, true);

			// exterior.getExteriorPos().getWorld().playSound(null, exterior.getExteriorPos(), AITSounds.MAT, SoundCategory.BLOCKS, 5f, 1f);
			// tardis.getTravel().setState(TardisTravel.State.MAT);

			tardis.getTravel().dematerialise(true, true);
		}
	}

	private void warnPlayers() {
		if (findTardis().isEmpty()) return;
		for (PlayerEntity player : TardisUtil.getPlayersInInterior(findTardis().get())) {
			player.sendMessage(Text.translatable("tardis.message.interiorchange.warning").formatted(Formatting.RED), true);
		}
	}

	private boolean isInteriorEmpty() {
		if (findTardis().isEmpty()) return false;
		return TardisUtil.getPlayersInInterior(findTardis().get()).isEmpty();
	}

	public static Random random() {
		if (random == null)
			random = new Random();

		return random;
	}

	private boolean clearedOldInterior = false;

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);
		if (findTardis().isEmpty()) return;
		if (TardisUtil.isClient()) return;
		if (!isGenerating()) return;
		if (DeltaTimeManager.isStillWaitingOnDelay("interior_change-" + findTardis().get().getUuid().toString()))
			return;
		if (findTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT) {
			findTardis().get().getTravel().crash();
		}
		if (isGenerating()) {
			if (!findTardis().get().getHandlers().getAlarms().isEnabled())
				findTardis().get().getHandlers().getAlarms().enable();
		}


		if (!findTardis().get().hasPower()) {
			setGenerating(false);
			findTardis().get().getHandlers().getAlarms().disable();
			return;
		}

		if (!isInteriorEmpty()) {
			warnPlayers();
			return;
		}

		if (isInteriorEmpty() && !findTardis().get().getDoor().locked()) {
			DoorData.lockTardis(true, findTardis().get(), null, true);
		}
		if (isInteriorEmpty() && !clearedOldInterior) {
			findTardis().get().getDesktop().clearOldInterior(getQueuedInterior());
			DeltaTimeManager.createDelay("interior_change-" + findTardis().get().getUuid().toString(), 15000L);
			clearedOldInterior = true;
			return;
		}
		if (isInteriorEmpty() && clearedOldInterior) {
			findTardis().get().getDesktop().changeInterior(getQueuedInterior());
			onCompletion();
		}
	}
}
