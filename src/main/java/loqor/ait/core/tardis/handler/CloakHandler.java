package loqor.ait.core.tardis.handler;

import net.minecraft.server.MinecraftServer;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;

public class CloakHandler extends KeyedTardisComponent implements TardisTickable {
    private static final BoolProperty IS_CLOAKED = new BoolProperty("is_cloaked", false);
    private final BoolValue isCloaked = IS_CLOAKED.create(this);

    @Override
    public void onLoaded() {
        isCloaked.of(this, IS_CLOAKED);
    }

    public CloakHandler() {
        super(Id.CLOAK);
    }

    public BoolValue cloaked() {
        return isCloaked;
    }

    @Override
    public void tick(MinecraftServer server) {
        if (!this.cloaked().get())
            return;

        if (!this.tardis.fuel().hasPower())
            this.cloaked().set(false);

        TravelHandler travel = this.tardis.travel();

        if (travel.inFlight())
            return;

        this.tardis.removeFuel(2 * travel.instability()); // idle drain of 2 fuel per tick
    }
}
