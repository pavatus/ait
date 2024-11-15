package loqor.ait.core.engine.impl;

import org.joml.Vector3f;

import loqor.ait.core.engine.DurableSubSystem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.util.ServerLifecycleHooks;
import loqor.ait.data.Exclude;

public class EngineSystem extends DurableSubSystem {
    @Exclude(strategy = Exclude.Strategy.FILE)
    private Status status;

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

    @Override
    protected float cost() {
        return 0.25f;
    }

    @Override
    protected int changeFrequency() {
        return 200; // drain 0.25 durability every 10 seconds
    }

    @Override
    protected boolean shouldDurabilityChange() {
        return tardis.subsystems().hasPower();
    }

    @Override
    protected void onBreak() {
        super.onBreak();

        this.setEnabled(false);
    }

    @Override
    protected void onRepair() {
        super.onRepair();

        this.setEnabled(true);
    }

    @Override
    public void tick() {
        super.tick();

        this.tickForDurability();
        this.tryUpdateStatus();
    }

    public Status status() {
        return this.status;
    }
    private void tryUpdateStatus() {
        if (ServerLifecycleHooks.get() == null) return;
        if (ServerLifecycleHooks.get().getTicks() % 40 != 0) return;

        this.status = Status.from(this);
        this.sync();
    }
    private void tickForDurability() {
        if (this.durability() <= 25) {
            this.tardis.alarm().enabled().set(true);
        }
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
