package mdteam.ait.tardis;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.util.data.AbsoluteBlockPos;

import java.util.UUID;
import java.util.function.Function;

public class Tardis {

    private final TardisTravel travel;
    private final UUID uuid;
    private TardisDesktop desktop;
    private final TardisExterior exterior;
    private final TardisDoor door;

    public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType) {
        this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema),
                tardis -> new TardisExterior(tardis, exteriorType), TardisDoor::new);
    }

    protected Tardis(UUID uuid, Function<Tardis, TardisTravel> travel, Function<Tardis, TardisDesktop> desktop,
                     Function<Tardis, TardisExterior> exterior, Function<Tardis, TardisDoor> door) {
        this.uuid = uuid;
        this.travel = travel.apply(this);
        this.desktop = desktop.apply(this);
        this.exterior = exterior.apply(this);
        this.door = door.apply(this);

        this.init();
    }

    private void init() {
        this.init(this.travel);
        this.init(this.desktop);
        this.init(this.exterior);
        this.init(this.door);
    }

    private void init(AbstractTardisComponent component) {
        if (component.shouldInit())
            component.init();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setDesktop(TardisDesktop desktop) {
        this.desktop = desktop;
    }

    public TardisDesktop getDesktop() {
        return desktop;
    }

    public TardisExterior getExterior() {
        return exterior;
    }

    public TardisTravel getTravel() {
        return travel;
    }

    public TardisDoor getDoor() {
        return door;
    }
}
