package loqor.ait.core.engine.impl;

import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.tardis.Tardis;

public class EngineSystem extends SubSystem {
    public EngineSystem() {
        super(Id.ENGINE);
    }

    @Override
    protected void onEnable() {
        super.onEnable();

        this.tardis().subsystems().enablePower();
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        this.tardis().subsystems().disablePower();
    }

    public static boolean hasEngine(Tardis t) {
        return t.subsystems().engine().isEnabled();
    }
}
