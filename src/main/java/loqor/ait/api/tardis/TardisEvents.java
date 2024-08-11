package loqor.ait.api.tardis;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.tardis.Tardis;

public final class TardisEvents {

    // Flight

    public static final Event<Demat> DEMAT = EventFactory.createArrayBacked(Demat.class, callbacks -> tardis -> {
        for (Demat callback : callbacks) {
            Interaction value = callback.onDemat(tardis);

            if (value != Interaction.PASS)
                return value;
        }

        return Interaction.SUCCESS;
    });

    public static final Event<Mat> MAT = EventFactory.createArrayBacked(Mat.class, callbacks -> tardis -> {
        for (Mat callback : callbacks) {
            Interaction value = callback.onMat(tardis);

            if (value != Interaction.PASS)
                return value;
        }

        return Interaction.SUCCESS;
    });

    public static final Event<Landed> LANDED = EventFactory.createArrayBacked(Landed.class, callbacks -> tardis -> {
        for (Landed callback : callbacks) {
            callback.onLanded(tardis);
        }
    });

    public static final Event<Crash> CRASH = EventFactory.createArrayBacked(Crash.class, callbacks -> tardis -> {
        for (Crash callback : callbacks) {
            callback.onCrash(tardis);
        }
    });

    // Power / Fuel
    public static final Event<NoFuel> OUT_OF_FUEL = EventFactory.createArrayBacked(NoFuel.class,
            callbacks -> tardis -> {
                for (NoFuel callback : callbacks) {
                    callback.onNoFuel(tardis);
                }
            });

    public static final Event<LosePower> LOSE_POWER = EventFactory.createArrayBacked(LosePower.class,
            callbacks -> tardis -> {
                for (LosePower callback : callbacks) {
                    callback.onLosePower(tardis);
                }
            });

    public static final Event<RegainPower> REGAIN_POWER = EventFactory.createArrayBacked(RegainPower.class,
            callbacks -> tardis -> {
                for (RegainPower callback : callbacks) {
                    callback.onRegainPower(tardis);
                }
            });

    // Door
    public static final Event<OpenDoor> DOOR_OPEN = EventFactory.createArrayBacked(OpenDoor.class,
            callbacks -> tardis -> {
                for (OpenDoor callback : callbacks) {
                    callback.onOpen(tardis);
                }
            });

    public static final Event<CloseDoor> DOOR_CLOSE = EventFactory.createArrayBacked(CloseDoor.class,
            callbacks -> (tardis) -> {
                for (CloseDoor callback : callbacks) {
                    callback.onClose(tardis);
                }
            });

    public static final Event<MoveDoor> DOOR_MOVE = EventFactory.createArrayBacked(MoveDoor.class,
            callbacks -> (tardis, prev) -> {
                for (MoveDoor callback : callbacks) {
                    callback.onMove(tardis, prev);
                }
            });

    public static final Event<EnterTardis> ENTER_TARDIS = EventFactory.createArrayBacked(EnterTardis.class,
            callbacks -> (tardis, entity) -> {
                for (EnterTardis callback : callbacks) {
                    callback.onEnter(tardis, entity);
                }
            });

    public static final Event<LeaveTardis> LEAVE_TARDIS = EventFactory.createArrayBacked(LeaveTardis.class,
            callbacks -> (tardis, entity) -> {
                for (LeaveTardis callback : callbacks) {
                    callback.onLeave(tardis, entity);
                }
            });

    public static final Event<Shields> TOGGLE_SHIELDS = EventFactory.createArrayBacked(Shields.class,
            callbacks -> (tardis, active, visual) -> {
                for (Shields callback : callbacks) {
                    callback.onShields(tardis, active, visual);
                }
            });

    public static final Event<SyncTardis> SYNC_TARDIS = EventFactory.createArrayBacked(SyncTardis.class,
            callbacks -> (tardis, chunk) -> {
                for (SyncTardis callback : callbacks) {
                    callback.sync(tardis, chunk);
                }
            });

    public static final Event<UnloadTardis> UNLOAD_TARDIS = EventFactory.createArrayBacked(UnloadTardis.class,
            callbacks -> (tardis, chunk) -> {
                for (UnloadTardis callback : callbacks) {
                    callback.unload(tardis, chunk);
                }
            });

    public static final Event<ReconfigureDesktop> RECONFIGURE_DESKTOP = EventFactory
            .createArrayBacked(ReconfigureDesktop.class, callbacks -> tardis -> {
                for (ReconfigureDesktop callback : callbacks) {
                    callback.reconfigure(tardis);
                }
            });

    public static final Event<OnExteriorChange> EXTERIOR_CHANGE = EventFactory.createArrayBacked(OnExteriorChange.class,
            callbacks -> tardis -> {
                for (OnExteriorChange callback : callbacks) {
                    callback.onChange(tardis);
                }
            });

    /**
     * Called when a TARDIS successfully ( passed all checks ) starts to take off,
     * before anything else is ran
     */
    @FunctionalInterface
    public interface Demat {
        /**
         * Called when a TARDIS successfully ( passed all checks ) starts to take off,
         * before anything else is ran.
         *
         * @param tardis
         *            the tardis taking off
         * @return event's result
         */
        Interaction onDemat(Tardis tardis);
    }

    /**
     * Called when a TARDIS successfully ( passed all checks ) starts to land,
     * before anything else is ran
     */
    @FunctionalInterface
    public interface Mat {
        /**
         * Called when a TARDIS successfully ( passed all checks ) starts to land,
         * before anything else is ran.
         *
         * @param tardis
         *            the tardis landing
         * @return event's result
         */
        Interaction onMat(Tardis tardis);
    }

    /**
     * Called when a TARDIS has finished landing
     */
    @FunctionalInterface
    public interface Landed {
        /**
         * Called when a TARDIS has finished landing
         *
         * @param tardis
         *            the landed tardis
         */
        void onLanded(Tardis tardis);
    }

    /**
     * Called when a TARDIS starts crashing
     */
    @FunctionalInterface
    public interface Crash {
        void onCrash(Tardis tardis);
    }

    /**
     * Called whenever the fuel is set to 0
     */
    @FunctionalInterface
    public interface NoFuel {
        void onNoFuel(Tardis tardis);
    }

    @FunctionalInterface
    public interface LosePower {
        /**
         * Called when a tardis' loses power
         */
        void onLosePower(Tardis tardis);
    }

    @FunctionalInterface
    public interface RegainPower {
        /**
         * Called when a tardis regains power
         */
        void onRegainPower(Tardis tardis);
    }

    /**
     * Called when a TARDIS Door opens ( called when its state is set to any of the
     * "open" states, but only if it was closed before )
     */
    @FunctionalInterface
    public interface OpenDoor {
        void onOpen(Tardis tardis);
    }

    /**
     * Called when a TARDIS Door closes
     */
    @FunctionalInterface
    public interface CloseDoor {
        void onClose(Tardis tardis);
    }

    /**
     * Called when the interior door position is changed, meaning it was probably
     * moved
     */
    @FunctionalInterface
    public interface MoveDoor {
        void onMove(Tardis tardis, DirectedBlockPos previous);
    }

    @FunctionalInterface
    public interface EnterTardis {
        void onEnter(Tardis tardis, Entity entity);
    }

    @FunctionalInterface
    public interface LeaveTardis {
        void onLeave(Tardis tardis, Entity entity);
    }

    @FunctionalInterface
    public interface Shields {
        void onShields(Tardis tardis, boolean active, boolean visual);
    }

    @FunctionalInterface
    public interface SyncTardis {
        void sync(ServerPlayerEntity player, WorldChunk chunk);
    }

    @FunctionalInterface
    public interface UnloadTardis {
        void unload(ServerPlayerEntity player, ChunkPos chunk);
    }

    @FunctionalInterface
    public interface ReconfigureDesktop {
        void reconfigure(Tardis tardis);
    }

    @FunctionalInterface
    public interface OnExteriorChange {
        void onChange(Tardis tardis);
    }

    public enum Interaction {
        SUCCESS, FAIL, PASS
    }
}
