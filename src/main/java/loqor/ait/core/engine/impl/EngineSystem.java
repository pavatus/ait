package loqor.ait.core.engine.impl;

import org.joml.Vector3f;

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

    public Status status() {
        return Status.from(this);
    }

    public static boolean hasEngine(Tardis t) {
        return t.subsystems().engine().isEnabled();
    }

    public enum Status {
        OKAY(132, 195, 240) {
            @Override
            public boolean isViable(EngineSystem system) {
                return true;
            }
        },
        OFF(0, 0, 0) {
            @Override
            public boolean isViable(EngineSystem system) {
                return !system.tardis.subsystems().hasPower();
            }
        },
        ERROR(250, 242, 22) {
            @Override
            public boolean isViable(EngineSystem system) {
                return system.tardis.alarm().enabled().get() || system.tardis.sequence().hasActiveSequence();
            }
        },
        CRITICAL(250, 33, 22) {
            @Override
            public boolean isViable(EngineSystem system) {
                return false; // todo
            }
        },
        LEAKAGE(114, 255, 33) {
            @Override
            public boolean isViable(EngineSystem system) {
                return false; // todo
            }
        };
        public abstract boolean isViable(EngineSystem system);

        public final Vector3f colour;

        Status(int red, int green, int blue) {
            this.colour = new Vector3f(red / 255f, green / 255f, blue / 255f);
        }

        public static Status from(EngineSystem system) {
            for (Status status : values()) {
                if (status.isViable(system) && !status.equals(OKAY)) {
                    return status;
                }
            }

            return OKAY;
        }
    }
}
