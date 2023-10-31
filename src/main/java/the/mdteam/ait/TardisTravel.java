package the.mdteam.ait;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITravel;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.data.AbsoluteBlockPos;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TardisTravel implements ITravel {

    private IState state = State.LANDED;
    private AbsoluteBlockPos.Directed position;
    private AbsoluteBlockPos.Directed destination;

    @Exclude private final ITardis tardis;

    public TardisTravel(ITardis tardis, AbsoluteBlockPos.Directed pos) {
        this.tardis = tardis;
        this.position = pos;
    }

    @Override
    public void setPosition(AbsoluteBlockPos.Directed pos) {
        this.position = pos;
    }

    @Override
    public AbsoluteBlockPos.Directed getPosition() {
        return position;
    }

    @Override
    public void materialise() {

    }

    @Override
    public void dematerialise(boolean withRemat) {

    }

    @Override
    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        this.destination = pos;
    }

    @Override
    public AbsoluteBlockPos.Directed getDestination() {
        return destination;
    }

    @Override
    public IState getState() {
        return state;
    }

    @Override
    public void setState(IState state) {
        this.state = state;
    }

    @Override
    public void toggleHandbrake() {
        this.state.next(new TravelContext(this, this.position, this.destination));
    }

    @Override
    public void placeExterior() {
        this.position.setBlockState(AITBlocks.EXTERIOR_BLOCK.getDefaultState());

        ExteriorBlockEntity exterior = new ExteriorBlockEntity(
                this.position, this.position.getBlockState()
        );

        exterior.setTardis(this.tardis);
        this.position.addBlockEntity(exterior);
    }

    @Override
    public void deleteExterior() {

    }

    public enum State implements IState {
        LANDED(true) {
            @Override
            public void onEnable() {
                AITMod.LOGGER.info("ON: LANDED");
            }

            @Override
            public void onDisable() {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public void schedule(TravelContext context) {

            }

            @Override
            public IState getNext() {
                return DEMAT;
            }
        },
        DEMAT {
            @Override
            public void onEnable() {
                AITMod.LOGGER.info("ON: DEMAT");
            }

            @Override
            public void onDisable() {
                AITMod.LOGGER.info("OFF: DEMAT");
            }

            @Override
            public IState getNext() {
                return FLIGHT;
            }
        },
        FLIGHT(true) {
            @Override
            public void onEnable() {
                AITMod.LOGGER.info("ON: FLIGHT");
            }

            @Override
            public void onDisable() {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public void schedule(TravelContext context) {

            }

            @Override
            public IState getNext() {
                return MAT;
            }
        },
        MAT {
            @Override
            public void onEnable() {
                AITMod.LOGGER.info("ON: MAT");
            }

            @Override
            public void onDisable() {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public IState getNext() {
                return LANDED;
            }
        };

        private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        private final boolean isStatic;

        State(boolean isStatic) {
            this.isStatic = isStatic;
        }

        State() {
            this(false);
        }

        @Override
        public boolean isStatic() {
            return isStatic;
        }

        public ScheduledExecutorService getService() {
            return service;
        }

        @Override
        public void next(TravelContext context) {
            this.service.shutdown();
            this.onDisable();

            IState next = this.getNext();
            next.schedule(context);

            next.onEnable();
            context.travel().setState(next);
        }

        @Override
        public void schedule(TravelContext context) {
            this.getService().schedule(() -> {
                this.next(context);
            }, 2, TimeUnit.SECONDS);
        }
    }
}
