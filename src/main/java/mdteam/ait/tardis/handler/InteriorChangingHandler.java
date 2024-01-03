package mdteam.ait.tardis.handler;

import mdteam.ait.AITMod;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Random;
import java.util.UUID;

public class InteriorChangingHandler extends TardisLink {
    private static final int WARN_TIME = 10 * 40;
    public static final String IS_REGENERATING = "is_regenerating";
    public static final String GENERATING_TICKS = "generating_ticks";
    public static final String QUEUED_INTERIOR = "queued_interior";
    public static final Identifier CHANGE_DESKTOP = new Identifier(AITMod.MOD_ID, "change_desktop");
    private static Random random;

    public InteriorChangingHandler(UUID tardisId) {
        super(tardisId);
    }

    private void setGenerating(boolean var) {
        PropertiesHandler.set(getTardis().getHandlers().getProperties(), IS_REGENERATING, var);
        getTardis().markDirty();
    }
    public boolean isGenerating() {
        return PropertiesHandler.getBool(getTardis().getHandlers().getProperties(), IS_REGENERATING);
    }

    private void setTicks(int var) {
        PropertiesHandler.set(getTardis().getHandlers().getProperties(), GENERATING_TICKS, var);
        getTardis().markDirty();
    }
    private void addTick() {
        setTicks(getTicks() + 1);
    }
    public int getTicks() {
        return PropertiesHandler.getInt(getTardis().getHandlers().getProperties(), GENERATING_TICKS);
    }
    public boolean hasReachedMax() {
        return getTicks() >= WARN_TIME;
    }

    private void setQueuedInterior(TardisDesktopSchema schema) {
        PropertiesHandler.setDesktop(getTardis().getHandlers().getProperties(), QUEUED_INTERIOR, schema);
        getTardis().markDirty();
    }
    public TardisDesktopSchema getQueuedInterior() {
        return PropertiesHandler.getDesktop(getTardis().getHandlers().getProperties(), QUEUED_INTERIOR);
    }

    public void queueInteriorChange(TardisDesktopSchema schema) {
        if (!getTardis().hasPower()) return;
        if (getTardis().isGrowth() && getTardis().hasGrowthExterior())
            getTardis().getExterior().setType(TardisItemBuilder.findRandomExterior());
        if (getTardis().getHandlers().getFuel().getFuel() < 5000 && !(getTardis().isGrowth() && getTardis().hasGrowthDesktop())) {
            for (PlayerEntity player : TardisUtil.getPlayersInInterior(getTardis())) {
                player.sendMessage(Text.translatable("tardis.message.interiorchange.not_enough_fuel").formatted(Formatting.RED), true);
                return;
            }
        }
        setQueuedInterior(schema);
        setTicks(0);
        setGenerating(true);
        DeltaTimeManager.createDelay("interior_change-" + getTardis().getUuid().toString(), 100L);
        getTardis().getHandlers().getAlarms().enable();
        getTardis().getDesktop().setConsolePos(null);
        if (!(getTardis().hasGrowthDesktop()))
            getTardis().removeFuel(5000);
        getTardis().markDirty();
    }

    private void onCompletion() {
        setGenerating(false);
        clearedOldInterior = false;
        getTardis().getHandlers().getAlarms().disable();
        DoorHandler.lockTardis(PropertiesHandler.getBool(getTardis().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED), getTardis(), null, false);
    }

    private void warnPlayers() {
        for (PlayerEntity player : TardisUtil.getPlayersInInterior(getTardis())) {
            player.sendMessage(Text.translatable("tardis.message.interiorchange.warning").formatted(Formatting.RED), true);
        }
    }
    private boolean isInteriorEmpty() {
        return TardisUtil.getPlayersInInterior(getTardis()).isEmpty();
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
        if (TardisUtil.isClient()) return;
        if (!isGenerating()) return;
        if (DeltaTimeManager.isStillWaitingOnDelay("interior_change-" + getTardis().getUuid().toString())) return;
        if (getTardis().getTravel().getState() == TardisTravel.State.FLIGHT) {
            getTardis().getTravel().crash();
        }
        if (isGenerating()) {
            if (!getTardis().getHandlers().getAlarms().isEnabled()) getTardis().getHandlers().getAlarms().enable();
        }


        if (!getTardis().hasPower()) {
            setGenerating(false);
            getTardis().getHandlers().getAlarms().disable();
            return;
        }
        if (!isGenerating()) return;

        if (!isInteriorEmpty()) {
            warnPlayers();
            return;
        }

        if (isInteriorEmpty() && !getTardis().getDoor().locked()) {
            DoorHandler.lockTardis(true, getTardis(), null, true);
        }
        if (isInteriorEmpty() && !clearedOldInterior) {
            getTardis().getDesktop().clearOldInterior(getQueuedInterior());
            DeltaTimeManager.createDelay("interior_change-" + getTardis().getUuid().toString(), 15000L);
            clearedOldInterior = true;
            return;
        }
        if (isInteriorEmpty() && clearedOldInterior) {
            getTardis().getDesktop().changeInterior(getQueuedInterior());
            onCompletion();
        }

    }
}
