package mdteam.ait.tardis.data;

import mdteam.ait.AITMod;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
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
    private boolean isGenerating;
    private int ticks;
    private TardisDesktopSchema queuedInterior;

    public InteriorChangingHandler(Tardis tardis) {
        super(tardis, "interior-changing");
    }

    private void setGenerating(boolean var) {
        this.isGenerating = var;
    }
    public boolean isGenerating() {
        return this.isGenerating;
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
        this.queuedInterior = schema;
    }
    public TardisDesktopSchema getQueuedInterior() {
        return this.queuedInterior;
    }

    public void queueInteriorChange(TardisDesktopSchema schema) {
        if(getTardis().isEmpty()) return;
        if (!getTardis().get().hasPower()) return;
        if (getTardis().get().isGrowth() && getTardis().get().hasGrowthExterior())
            getTardis().get().getExterior().setType(TardisItemBuilder.findRandomExterior());
        if (getTardis().get().getHandlers().getFuel().getFuel() < 5000 && !(getTardis().get().isGrowth() && getTardis().get().hasGrowthDesktop())) {
            for (PlayerEntity player : TardisUtil.getPlayersInInterior(getTardis().get())) {
                player.sendMessage(Text.translatable("tardis.message.interiorchange.not_enough_fuel").formatted(Formatting.RED), true);
                return;
            }
        }
        setQueuedInterior(schema);
        setTicks(0);
        setGenerating(true);
        DeltaTimeManager.createDelay("interior_change-" + getTardis().get().getUuid().toString(), 100L);
        getTardis().get().getHandlers().getAlarms().enable();
        getTardis().get().getDesktop().setConsolePos(null);
        if (!(getTardis().get().hasGrowthDesktop()))
            getTardis().get().removeFuel(5000);
    }

    private void onCompletion() {
        if(getTardis().isEmpty()) return;
        setGenerating(false);
        clearedOldInterior = false;
        getTardis().get().getHandlers().getAlarms().disable();
        DoorData.lockTardis(PropertiesHandler.getBool(getTardis().get().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED), getTardis().get(), null, false);
    }

    private void warnPlayers() {
        if(getTardis().isEmpty()) return;
        for (PlayerEntity player : TardisUtil.getPlayersInInterior(getTardis().get())) {
            player.sendMessage(Text.translatable("tardis.message.interiorchange.warning").formatted(Formatting.RED), true);
        }
    }
    private boolean isInteriorEmpty() {
        if(getTardis().isEmpty()) return false;
        return TardisUtil.getPlayersInInterior(getTardis().get()).isEmpty();
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
        if(getTardis().isEmpty()) return;
        if (TardisUtil.isClient()) return;
        if (!isGenerating()) return;
        if (DeltaTimeManager.isStillWaitingOnDelay("interior_change-" + getTardis().get().getUuid().toString())) return;
        if (getTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT) {
            getTardis().get().getTravel().crash();
        }
        if (isGenerating()) {
            if (!getTardis().get().getHandlers().getAlarms().isEnabled()) getTardis().get().getHandlers().getAlarms().enable();
        }


        if (!getTardis().get().hasPower()) {
            setGenerating(false);
            getTardis().get().getHandlers().getAlarms().disable();
            return;
        }

        if (!isInteriorEmpty()) {
            warnPlayers();
            return;
        }

        if (isInteriorEmpty() && !getTardis().get().getDoor().locked()) {
            DoorData.lockTardis(true, getTardis().get(), null, true);
        }
        if (isInteriorEmpty() && !clearedOldInterior) {
            getTardis().get().getDesktop().clearOldInterior(getQueuedInterior());
            DeltaTimeManager.createDelay("interior_change-" + getTardis().get().getUuid().toString(), 15000L);
            clearedOldInterior = true;
            return;
        }
        if (isInteriorEmpty() && clearedOldInterior) {
            getTardis().get().getDesktop().changeInterior(getQueuedInterior());
            onCompletion();
        }

    }
}
