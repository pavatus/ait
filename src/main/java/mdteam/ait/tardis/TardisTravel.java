package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.door.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.tardis.travel.TravelContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TardisTravel extends AbstractTardisComponent {

    private State state;
    private AbsoluteBlockPos.Directed position;
    private AbsoluteBlockPos.Directed destination;
    private boolean shouldRemat = false;

    public TardisTravel(ITardis tardis, AbsoluteBlockPos.Directed pos) {
        super(tardis, "travel");

        this.position = pos;
        this.destination = null;
        this.state = State.LANDED;
    }

    protected TardisTravel(ITardis tardis, AbsoluteBlockPos.Directed pos, AbsoluteBlockPos.Directed dest, State state) {
        super(tardis, "travel", false);

        this.position = pos;
        this.destination = dest;
        this.state = state;
    }

    @Override
    public void init() {
        this.runAnimations();
        this.placeExterior();
    }

    public void setPosition(AbsoluteBlockPos.Directed pos) {
        this.position = pos;
    }

    public AbsoluteBlockPos.Directed getPosition() {
        return position;
    }

    public void materialise() {
        if (this.getDestination() == null)
            return;

        if (this.getDestination().getWorld().isClient())
            return;

        this.shouldRemat = false;
        this.setState(State.MAT);

        ServerWorld destWorld = (ServerWorld) this.getDestination().getWorld();
        destWorld.getChunk(this.getDestination());

        this.getDestination().getWorld().playSound(null, this.getDestination(), AITSounds.MAT, SoundCategory.BLOCKS,1f,1f);

        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING,this.getDestination().getDirection());
        destWorld.setBlockState(this.getDestination(), state);
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.getDestination(), state);
        destWorld.addBlockEntity(blockEntity);
        this.setPosition(this.getDestination());

        this.runAnimations(blockEntity);
    }

    public void dematerialise(boolean withRemat) {
        if (this.getPosition().getWorld().isClient())
            return;

        this.shouldRemat = withRemat;

        ServerWorld world = (ServerWorld) this.getPosition().getWorld();
        world.getChunk(this.getPosition());

        this.setState(State.DEMAT);

        world.playSound(null, this.getPosition(), AITSounds.DEMAT, SoundCategory.BLOCKS);

        this.runAnimations();

        // A definite thing just in case the animation isnt run

        Timer animTimer = new Timer();
        TardisTravel travel = this;

        animTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (travel.getState() == State.FLIGHT)
                    return;

                travel.toFlight();
            }
        }, (long) 10 * 1000L);
    }

    public void toFlight() {
        this.setState(TardisTravel.State.FLIGHT);
        this.deleteExterior();
        this.checkShouldRemat();
    }
    public void runAnimations() {
        ServerWorld level = (ServerWorld) this.position.getWorld();
        level.getChunk(this.getPosition());
        BlockEntity entity = level.getBlockEntity(this.getPosition());
        if (entity instanceof ExteriorBlockEntity) {
            ((ExteriorBlockEntity) entity).getAnimation().setupAnimation(this.state);
        }
    }

    public void runAnimations(ExteriorBlockEntity exterior) {
        exterior.getAnimation().setupAnimation(this.state);
    }

    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        this.destination = pos;
    }

    public AbsoluteBlockPos.Directed getDestination() {
        return destination;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void toggleHandbrake() {
        this.state.next(new TravelContext(this, this.position, this.destination));
    }

    public void placeExterior() {
        this.position.setBlockState(AITBlocks.EXTERIOR_BLOCK.getDefaultState());

        ExteriorBlockEntity exterior = new ExteriorBlockEntity(
                this.position, this.position.getBlockState()
        );

        // this is needed for the initialization. when we call #setTardis(ITardis) the travel field is still null.
        exterior.setTardis(this.tardis);
        this.position.addBlockEntity(exterior);
    }

    public void deleteExterior() {
        this.destination.getWorld().getChunk(this.getDestination());
        this.destination.getWorld().removeBlock(this.getPosition(),false);
    }

    public void checkShouldRemat() {
        if (!this.shouldRemat)
            return;

        this.materialise();
    }

    public enum State {
        LANDED(true) {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: LANDED");
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public State getNext() {
                return DEMAT;
            }
        },
        DEMAT {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: DEMAT");

                context.travel().dematerialise(false);
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: DEMAT");
            }

            @Override
            public long schedule(TravelContext context) {
                return 2000;
            }

            @Override
            public State getNext() {
                return FLIGHT;
            }
        },
        FLIGHT(true) {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: FLIGHT");
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public State getNext() {
                return MAT;
            }
        },
        MAT {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: MAT");

                // context.travel().materialise();
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public long schedule(TravelContext context) {
                return 2000;
            }

            @Override
            public State getNext() {
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

        public boolean isStatic() {
            return isStatic;
        }

        public ScheduledExecutorService getService() {
            return service;
        }

        public abstract void onEnable(TravelContext context);
        public abstract void onDisable(TravelContext context);
        public abstract State getNext();

        public void next(TravelContext context) {
            this.service.shutdown();
            this.onDisable(context);

            State next = this.getNext();
            next.schedule(context);

            next.onEnable(context);
            context.travel().setState(next);
        }

        public long schedule(TravelContext context) {
            return -1;
        }

        public void scheduleAndRun(TravelContext context) {
            long duration = this.schedule(context);

            if (duration < 0)
                throw new IllegalArgumentException("Schedule method was not implemented for non-static state " + this);

            this.service.schedule(() -> {
                if (this.isStatic)
                    return;

                this.next(context);
            }, duration, TimeUnit.MILLISECONDS);
        }
    }
}
