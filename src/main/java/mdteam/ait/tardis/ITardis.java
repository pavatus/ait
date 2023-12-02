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
        this.init(false);
    }

    default void init(boolean dirty) {
        this.init(this.getTravel(), dirty);
        this.init(this.getDesktop(), dirty);
        this.init(this.getExterior(), dirty);
        this.init(this.getDoor(), dirty);
    }

    private void init(AbstractTardisComponent component, boolean dirty) {
        AbstractTardisComponent.Init mode = component.getInitMode();
        component.setTardis(this);

        switch (mode) {
            case NO_INIT -> {}
            case ALWAYS -> component.init();
            case FIRST -> {
                if (!dirty) component.init();
            }
            case DESERIALIZE -> {
                if (dirty) component.init();
            }
            default -> throw new IllegalArgumentException("Unimplemented init mode " + mode);
        }
    }
}
