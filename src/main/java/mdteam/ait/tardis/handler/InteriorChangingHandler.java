package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Random;
import java.util.UUID;

public class InteriorChangingHandler extends TardisLink {
    private static final int WARN_TIME = 10 * 40;
    public static final String IS_REGENERATING = "is_regenerating";
    public static final String GENERATING_TICKS = "generating_ticks";
    public static final String QUEUED_INTERIOR = "queued_interior";
    private static Random random;

    public InteriorChangingHandler(UUID tardisId) {
        super(tardisId);
    }

    private void setGenerating(boolean var) {
        PropertiesHandler.set(tardis().getHandlers().getProperties(), IS_REGENERATING, var);
    }
    public boolean isGenerating() {
        return PropertiesHandler.getBool(tardis().getHandlers().getProperties(), IS_REGENERATING);
    }

    private void setTicks(int var) {
        PropertiesHandler.set(tardis().getHandlers().getProperties(), GENERATING_TICKS, var);
    }
    private void addTick() {
        setTicks(getTicks() + 1);
    }
    public int getTicks() {
        return PropertiesHandler.getInt(tardis().getHandlers().getProperties(), GENERATING_TICKS);
    }
    public boolean hasReachedMax() {
        return getTicks() >= WARN_TIME;
    }

    private void setQueuedInterior(TardisDesktopSchema schema) {
        PropertiesHandler.set(tardis().getHandlers().getProperties(), QUEUED_INTERIOR, schema);
    }
    public TardisDesktopSchema getQueuedInterior() {
        return (TardisDesktopSchema) PropertiesHandler.get(tardis().getHandlers().getProperties(), QUEUED_INTERIOR);
    }

    public void queueInteriorChange(TardisDesktopSchema schema) {
        setQueuedInterior(schema);
        setTicks(0);
        setGenerating(true);
        tardis().getHandlers().getAlarms().enable();
        tardis().getDesktop().setConsolePos(null);
    }

    private void onCompletion() {
        setGenerating(false);
        tardis().getHandlers().getAlarms().disable();
        tardis().removeFuel(20);
        DoorHandler.lockTardis(PropertiesHandler.getBool(tardis().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED), tardis(), null, false);
    }

    private void warnPlayers() {
        for (PlayerEntity player : TardisUtil.getPlayersInInterior(tardis())) {
            player.sendMessage(Text.literal("Interior reconfiguration started! Please leave the interior.").formatted(Formatting.RED), true);
        }
    }
    private boolean isInteriorEmpty() {
        return TardisUtil.getPlayersInInterior(tardis()).isEmpty();
    }

    public static Random random() {
        if (random == null)
            random = new Random();

        return random;
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        if (!isGenerating()) {
            if (getTicks() > 0) setTicks(0);
            return;
        }

        if (!isInteriorEmpty()) {
            warnPlayers();
            return;
        }

        if (isInteriorEmpty() && !tardis().getDoor().locked()) {
            DoorHandler.lockTardis(true, tardis(), null, true);
        }

        addTick();

        if (hasReachedMax()) {
            tardis().getDesktop().changeInterior(getQueuedInterior());
            onCompletion();
        }
    }
}
