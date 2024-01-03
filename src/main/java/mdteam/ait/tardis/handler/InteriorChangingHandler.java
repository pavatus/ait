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
        PropertiesHandler.set(getLinkedTardis().getHandlers().getProperties(), IS_REGENERATING, var);
        getLinkedTardis().markDirty();
    }
    public boolean isGenerating() {
        return PropertiesHandler.getBool(getLinkedTardis().getHandlers().getProperties(), IS_REGENERATING);
    }

    private void setTicks(int var) {
        PropertiesHandler.set(getLinkedTardis().getHandlers().getProperties(), GENERATING_TICKS, var);
        getLinkedTardis().markDirty();
    }
    private void addTick() {
        setTicks(getTicks() + 1);
    }
    public int getTicks() {
        return PropertiesHandler.getInt(getLinkedTardis().getHandlers().getProperties(), GENERATING_TICKS);
    }
    public boolean hasReachedMax() {
        return getTicks() >= WARN_TIME;
    }

    private void setQueuedInterior(TardisDesktopSchema schema) {
        PropertiesHandler.setDesktop(getLinkedTardis().getHandlers().getProperties(), QUEUED_INTERIOR, schema);
        getLinkedTardis().markDirty();
    }
    public TardisDesktopSchema getQueuedInterior() {
        return PropertiesHandler.getDesktop(getLinkedTardis().getHandlers().getProperties(), QUEUED_INTERIOR);
    }

    public void queueInteriorChange(TardisDesktopSchema schema) {
        if (!getLinkedTardis().hasPower()) return;
        if (getLinkedTardis().isGrowth() && getLinkedTardis().hasGrowthExterior())
            getLinkedTardis().getExterior().setType(TardisItemBuilder.findRandomExterior());
        if (getLinkedTardis().getHandlers().getFuel().getFuel() < 5000 && !(getLinkedTardis().isGrowth() && getLinkedTardis().hasGrowthDesktop())) {
            for (PlayerEntity player : TardisUtil.getPlayersInInterior(getLinkedTardis())) {
                player.sendMessage(Text.translatable("tardis.message.interiorchange.not_enough_fuel").formatted(Formatting.RED), true);
                return;
            }
        }
        setQueuedInterior(schema);
        setTicks(0);
        setGenerating(true);
        DeltaTimeManager.createDelay("interior_change-" + getLinkedTardis().getUuid().toString(), 100L);
        getLinkedTardis().getHandlers().getAlarms().enable();
        getLinkedTardis().getDesktop().setConsolePos(null);
        if (!(getLinkedTardis().hasGrowthDesktop()))
            getLinkedTardis().removeFuel(5000);
        getLinkedTardis().markDirty();
    }

    private void onCompletion() {
        setGenerating(false);
        clearedOldInterior = false;
        getLinkedTardis().getHandlers().getAlarms().disable();
        DoorHandler.lockTardis(PropertiesHandler.getBool(getLinkedTardis().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED), getLinkedTardis(), null, false);
    }

    private void warnPlayers() {
        for (PlayerEntity player : TardisUtil.getPlayersInInterior(getLinkedTardis())) {
            player.sendMessage(Text.translatable("tardis.message.interiorchange.warning").formatted(Formatting.RED), true);
        }
    }
    private boolean isInteriorEmpty() {
        return TardisUtil.getPlayersInInterior(getLinkedTardis()).isEmpty();
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
        if (DeltaTimeManager.isStillWaitingOnDelay("interior_change-" + getLinkedTardis().getUuid().toString())) return;
        if (getLinkedTardis().getTravel().getState() == TardisTravel.State.FLIGHT) {
            getLinkedTardis().getTravel().crash();
        }
        if (isGenerating()) {
            if (!getLinkedTardis().getHandlers().getAlarms().isEnabled()) getLinkedTardis().getHandlers().getAlarms().enable();
        }


        if (!getLinkedTardis().hasPower()) {
            setGenerating(false);
            getLinkedTardis().getHandlers().getAlarms().disable();
            return;
        }
        if (!isGenerating()) return;

        if (!isInteriorEmpty()) {
            warnPlayers();
            return;
        }

        if (isInteriorEmpty() && !getLinkedTardis().getDoor().locked()) {
            DoorHandler.lockTardis(true, getLinkedTardis(), null, true);
        }
        if (isInteriorEmpty() && !clearedOldInterior) {
            getLinkedTardis().getDesktop().clearOldInterior(getQueuedInterior());
            DeltaTimeManager.createDelay("interior_change-" + getLinkedTardis().getUuid().toString(), 15000L);
            clearedOldInterior = true;
            return;
        }
        if (isInteriorEmpty() && clearedOldInterior) {
            getLinkedTardis().getDesktop().changeInterior(getQueuedInterior());
            onCompletion();
        }

    }
}
