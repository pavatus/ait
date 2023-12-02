package mdteam.ait.tardis;

import java.util.UUID;

public interface ITardis {

    UUID getUuid();
    TardisExterior getExterior();
    TardisTravel getTravel();
    TardisDoor getDoor();

    TardisDesktop getDesktop();
    void setDesktop(TardisDesktop desktop);

    default void init() {
        ITardis.init(this.getTravel());
        ITardis.init(this.getDesktop());
        ITardis.init(this.getExterior());
        ITardis.init(this.getDoor());
    }

    private static void init(AbstractTardisComponent component) {
        if (component.shouldInit())
            component.init();
    }
}
