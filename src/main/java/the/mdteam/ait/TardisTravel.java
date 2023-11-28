package the.mdteam.ait;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.profiler.SampleType;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TardisTravel {

    private State state = State.LANDED;
    private AbsoluteBlockPos.Directed position;
    private AbsoluteBlockPos.Directed destination;
    private boolean shouldRemat = false;

    @Exclude
    protected final Tardis tardis;

    public TardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        this.tardis = tardis;
        this.position = pos;
    }

    public TardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos, AbsoluteBlockPos.Directed dest, State state) {
        this.tardis = tardis;
        this.position = pos;
        this.destination = dest;
        this.state = state;
    }

    public void setPosition(AbsoluteBlockPos.Directed pos) {
        this.position = pos;
    }

    public AbsoluteBlockPos.Directed getPosition() {
        return position;
    }
    public static double getSoundEventLengthInSeconds(SoundEvent sound) {
        return 10.0d;

//        try {
//            // @TODO is no worky??
//            AudioInputStream stream =
//                    (AudioInputStream) TardisUtil.getServer().getResourceManager().getResource(sound.getId()).get().getInputStream();
//            AudioFormat format = stream.getFormat();
//            long frames = stream.getFrameLength();
//            return (frames + 0.0) / format.getFrameRate();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
    public void materialise() {
        if (this.getPosition().getWorld().isClient())
            return;

        if (this.getDestination() == null)
            return;

        this.shouldRemat = false;

        this.setState(State.MAT);

        ServerWorld world = (ServerWorld) this.getPosition().getWorld();
        TravelContext context = new TravelContext(this, this.getPosition(),this.getDestination());

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

//        Timer animTimer = new Timer();
//
//        Runnable mat = () -> {
//            this.getState().next(context);
//            this.runAnimations(blockEntity);
//
//            // blockEntity.refindTardis();
//        };
//
//        animTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mat.run();
//            }
//        }, (long) getSoundEventLengthInSeconds(AITSounds.MAT) * 1000L);
    }

    public void dematerialise(boolean withRemat) {
        if (this.getPosition().getWorld().isClient())
            return;

        this.shouldRemat = withRemat;

        ServerWorld world = (ServerWorld) this.getPosition().getWorld();
        world.getChunk(this.getPosition());

        this.setState(State.DEMAT);
        TravelContext context = new TravelContext(this, this.getPosition(),this.getDestination());

        world.playSound(null, this.getPosition(), AITSounds.DEMAT, SoundCategory.BLOCKS);

        this.runAnimations();

//        Timer animTimer = new Timer();
//        TardisTravel travel = this;

//        System.out.println(getSoundEventLengthInSeconds(AITSounds.DEMAT));
//
//        animTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                travel.getState().next(context);
//
//                world.getChunk(travel.getDestination());
//                world.removeBlock(travel.getPosition(),false);
//
//                if (withRemat) {
//                    travel.materialise();
//                }
//            }
//        }, (long) getSoundEventLengthInSeconds(AITSounds.DEMAT) * 1000L);
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
        System.out.println(this.state);
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
            public void schedule(TravelContext context) {

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

                // context.travel().dematerialise(false);
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: DEMAT");
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
            public void schedule(TravelContext context) {

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

        public void schedule(TravelContext context) {
            this.getService().schedule(() -> {
                // this.next(context);
            }, 2, TimeUnit.SECONDS);
        }
    }
}
