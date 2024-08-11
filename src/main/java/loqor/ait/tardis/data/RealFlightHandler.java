package loqor.ait.tardis.data;

import net.minecraft.server.MinecraftServer;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;

public class RealFlightHandler extends KeyedTardisComponent implements TardisTickable {

    private static final BoolProperty IS_FALLING = new BoolProperty("falling", false);
    private final BoolValue falling = IS_FALLING.create(this);

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
            DoorHandler.lockTardis(true, this.tardis, null, true);
    }

    public BoolValue falling() {
        return falling;
    }
}
