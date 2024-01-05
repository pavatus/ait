package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class CloakHandler extends TardisLink {
    public CloakHandler(UUID tardisId) {
        super(tardisId);
    }

    public void enable() {
        PropertiesHandler.setBool(tardis().getHandlers().getProperties(), PropertiesHandler.IS_CLOAKED, true);
        tardis().markDirty();
    }

    public void disable() {
        PropertiesHandler.setBool(tardis().getHandlers().getProperties(), PropertiesHandler.IS_CLOAKED, false);
        tardis().markDirty();
    }

    public boolean isEnabled() {
        return PropertiesHandler.getBool(tardis().getHandlers().getProperties(), PropertiesHandler.IS_CLOAKED);
    }

    public void toggle() {
        if (isEnabled()) disable();
        else enable();
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        if (this.isEnabled() && !tardis().hasPower()) {
            this.disable();
            return;
        }

        if (!this.isEnabled()) return;

        this.tardis().removeFuel(2); // idle drain of 2 fuel per tick
    }
}
