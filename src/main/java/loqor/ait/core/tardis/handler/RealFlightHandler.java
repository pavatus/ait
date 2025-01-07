package loqor.ait.core.tardis.handler;

import net.minecraft.server.MinecraftServer;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;

public class RealFlightHandler extends KeyedTardisComponent implements TardisTickable {

    private static final BoolProperty IS_FALLING = new BoolProperty("falling", false);
    private final BoolValue falling = IS_FALLING.create(this);

    static {
        TardisEvents.DEMAT.register(tardis -> tardis.flight().falling().get() ? TardisEvents.Interaction.FAIL : TardisEvents.Interaction.PASS);
    }

    public RealFlightHandler() {
        super(Id.FLIGHT);
    }

    @Override
    public void onLoaded() {
        falling.of(this, IS_FALLING);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (this.falling.get())
            this.tardis.door().setLocked(true);
    }

    public BoolValue falling() {
        return falling;
    }
}
