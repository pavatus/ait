package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class InteriorChangingHandler extends KeyedTardisComponent implements TardisTickable {
	private static final BoolProperty IS_REGENERATING_PROPERTY = new BoolProperty("regenerating", false);
    private final BoolValue isRegenerating = IS_REGENERATING_PROPERTY.create(this);
	private static final Property<Identifier> QUEUED_INTERIOR_PROPERTY = new Property<>(Property.Type.IDENTIFIER, "queued_interior", new Identifier(""));
	private final Value<Identifier> queuedInterior = QUEUED_INTERIOR_PROPERTY.create(this);
	public static final Identifier CHANGE_DESKTOP = new Identifier(AITMod.MOD_ID, "change_desktop");

    public InteriorChangingHandler() {
		super(Id.INTERIOR);
	}

	@Override
	public void onLoaded() {
		queuedInterior.of(this, QUEUED_INTERIOR_PROPERTY);
		isRegenerating.of(this, IS_REGENERATING_PROPERTY);
	}

	static {
		TardisEvents.DEMAT.register(tardis -> {
			if (tardis.isGrowth() || tardis.<InteriorChangingHandler>handler(TardisComponent.Id.INTERIOR).isGenerating())
				return TardisEvents.Interaction.FAIL;

            return TardisEvents.Interaction.PASS;
        });

		TardisEvents.MAT.register(tardis -> {
			if (!tardis.isGrowth())
				return TardisEvents.Interaction.PASS;

			tardis.getExterior().setType(CategoryRegistry.CAPSULE);
            return TardisEvents.Interaction.SUCCESS; // force mat even if checks fail
        });
	}

	private void setGenerating(boolean var) {
		isRegenerating.set(var);
	}

	public boolean isGenerating() {
		return isRegenerating.get();
	}

	private void setQueuedInterior(TardisDesktopSchema schema) {
		queuedInterior.set(schema.id());
	}

	public TardisDesktopSchema getQueuedInterior() {
		return DesktopRegistry.getInstance().get(queuedInterior.get());
	}

	public void queueInteriorChange(TardisDesktopSchema schema) {
		if (!this.canQueue())
			return;

		if (tardis.fuel().getCurrentFuel() < 5000) {
			for (PlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis)) {
				player.sendMessage(Text.translatable("tardis.message.interiorchange.not_enough_fuel").formatted(Formatting.RED), true);
				return;
			}
		}

		AITMod.LOGGER.info("Queueing interior change for {} to {}", this.tardis, schema);

		setQueuedInterior(schema);
		setGenerating(true);

		DeltaTimeManager.createDelay("interior_change-" + tardis.getUuid().toString(), 100L);
		tardis.alarm().enabled().set(true);

		tardis.getDesktop().getConsolePos().clear();

		if (!tardis.hasGrowthDesktop())
			tardis.removeFuel(5000 * tardis.travel().instability());
	}

	private void onCompletion() {
        this.setGenerating(false);
		clearedOldInterior = false;

		tardis.alarm().enabled().set(false);

		boolean previouslyLocked = tardis.door().previouslyLocked().get();
		DoorHandler.lockTardis(previouslyLocked, tardis, null, false);

		tardis.engine().hasEngineCore().set(false);
		tardis.engine().disablePower();

		if (tardis.hasGrowthExterior()) {
			TravelHandler travel = tardis.travel();

			travel.autopilot(true);
			travel.forceDemat();
		}
	}

	private void warnPlayers() {
		for (PlayerEntity player : TardisUtil.getPlayersInsideInterior(this.tardis())) {
			player.sendMessage(Text.translatable("tardis.message.interiorchange.warning").formatted(Formatting.RED), true);
		}
	}

	private boolean isInteriorEmpty() {
		return TardisUtil.getPlayersInsideInterior(this.tardis()).isEmpty();
	}

	private boolean clearedOldInterior = false;

	@Override
	public void tick(MinecraftServer server) {
		if (!isGenerating())
			return;

		if (DeltaTimeManager.isStillWaitingOnDelay("interior_change-" + this.tardis().getUuid().toString()))
			return;

		TravelHandler travel = this.tardis().travel();

		if (server.getTicks() % 10 == 0 && travel.getState() == TravelHandler.State.FLIGHT && !travel.isCrashing())
			travel.crash();

		if (this.isGenerating())
			tardis.alarm().enabled().set(true);

		if (!this.canQueue()) {
			this.setGenerating(false);
			this.tardis.alarm().enabled().set(false);
			return;
		}

		if (!isInteriorEmpty()) {
			warnPlayers();
			return;
		}

		if (isInteriorEmpty() && !this.tardis().door().locked()) {
			DoorHandler.lockTardis(true, this.tardis(), null, true);
		}
		if (isInteriorEmpty() && !clearedOldInterior) {
			this.tardis().getDesktop().clearOldInterior(getQueuedInterior());
			DeltaTimeManager.createDelay("interior_change-" + this.tardis().getUuid().toString(), 15000L);
			clearedOldInterior = true;
			return;
		}

		if (isInteriorEmpty() && clearedOldInterior) {
			this.tardis().getDesktop().changeInterior(getQueuedInterior());
			onCompletion();
		}
	}

	private boolean canQueue() {
		return tardis.isGrowth() || tardis.engine().hasPower() || tardis.crash().isToxic();
	}
}
