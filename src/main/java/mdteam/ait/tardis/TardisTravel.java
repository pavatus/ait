package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.entities.control.impl.DoorControl;
import mdteam.ait.core.entities.control.impl.pos.PosManager;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TardisTravel {

    private State state = State.LANDED;
    private AbsoluteBlockPos.Directed position;
    private AbsoluteBlockPos.Directed destination;
    private boolean shouldRemat = false;
    private static final double FORCE_LAND_TIMER = 15;
    private static final double FORCE_FLIGHT_TIMER = 10;
    private PosManager posManager; // kinda useless everything in posmanager could just be done here but this class is getting bloated

    @Exclude
    protected Tardis tardis;

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

    public AbsoluteBlockPos.Client getClientPosition() {
        return new AbsoluteBlockPos.Client(position, position.getDirection(), position.getWorld());
    }

    public static int getSoundLength(MatSound sound) {
        if (sound == null)
            return (int) FORCE_LAND_TIMER;
        return sound.timeLeft() / 20;
    }

    public Tardis getTardis() {
        if (this.tardis == null && this.getPosition() != null) {
            Tardis found = TardisUtil.findTardisByPosition(this.getPosition());
            if (found != null)
                this.tardis = found;
        }

        return this.tardis;
    }

    public boolean __subCheckForPos(BlockPos.Mutable mutable, int i) {
        BlockState state = this.getDestination().getWorld().getBlockState(mutable.setY(i));
        if (state.isReplaceable() && !this.getDestination().getWorld().getBlockState(mutable.down()).isReplaceable()) {
            AbsoluteBlockPos.Directed abpd = new AbsoluteBlockPos.Directed(mutable.setY(i),
                    this.getDestination().getWorld(), this.getDestination().getDirection());
            this.setDestination(abpd, false);
            return true;
        }
        return false;
    }

    //Yeah I know, I'm so cool :) - Loqor
    public void checkPositionAndMaterialise(boolean landType) {

        if(this.getDestination() == null)
            return;

        if(this.getDestination().getWorld().isClient())
            return;

        BlockPos.Mutable mutable = new BlockPos.Mutable(this.getDestination().getX(), this.getDestination().getY(), this.getDestination().getZ());
        if(landType) {
            for (int i = this.getDestination().getY(); i > this.getDestination().getWorld().getBottomY(); i--) {
                if (__subCheckForPos(mutable, i)) {
                    materialise();
                    return;
                }
            }
        } else {
            for (int i = this.getDestination().getY(); i < this.getDestination().getWorld().getBottomY(); i++) {
                if (__subCheckForPos(mutable, i)) {
                    materialise();
                    return;
                }
            }
        }
        if (this.getTardis() != null) {
            PlayerEntity player = TardisUtil.getPlayerInsideInterior(this.getTardis());
            if (player != null) {
                player.sendMessage(Text.literal("Position not viable for translocation: " + mutable.getX() + " | " + mutable.getY() + " | " + mutable.getZ()), true);
            }
        }
    }

    public void materialise() {
        if (this.getDestination() == null)
            return;

        if (this.getDestination().getWorld().isClient())
            return;

        this.shouldRemat = false;

        DoorControl.lockTardis(this.getTardis().getLockedTardis(), this.getTardis(), TardisUtil.getTardisDimension(), null);

        this.setState(State.MAT);

        ServerWorld destWorld = (ServerWorld) this.getDestination().getWorld();
        destWorld.getChunk(this.getDestination());

        this.getDestination().getWorld().playSound(null, this.getDestination(), this.getSoundForCurrentState(), SoundCategory.BLOCKS,1f,1f);
        //TardisUtil.getTardisDimension().playSound(null, getInteriorCentre(), AITSounds.MAT, SoundCategory.BLOCKS, 10f, 1f);
        if(this.getTardis() != null)
            if(this.getTardis().getDesktop().getConsolePos() != null)
                TardisUtil.getTardisDimension().playSound(null, this.getTardis().getDesktop().getConsolePos(), this.getSoundForCurrentState(), SoundCategory.BLOCKS, 1f, 1f);

        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING,this.getDestination().getDirection());
        destWorld.setBlockState(this.getDestination(), state);
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.getDestination(), state);
        destWorld.addBlockEntity(blockEntity);
        this.setPosition(this.getDestination());

        this.runAnimations(blockEntity);

        // A definite thing just in case the animation isnt run

        Timer animTimer = new Timer();
        TardisTravel travel = this;

        animTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (travel.getState() != State.MAT)
                    return;

                travel.setState(TardisTravel.State.LANDED);
                travel.runAnimations(blockEntity);
            }
        }, (long) getSoundLength(this.getMatSoundForCurrentState()) * 1000L);
    }

    public void dematerialise(boolean withRemat) {
        if (this.getPosition().getWorld().isClient())
            return;

        this.shouldRemat = withRemat;

        ServerWorld world = (ServerWorld) this.getPosition().getWorld();
        world.getChunk(this.getPosition());

        DoorControl.lockTardis(this.getTardis().getLockedTardis(), this.getTardis(), TardisUtil.getTardisDimension(), null);

        this.setState(State.DEMAT);

        world.playSound(null, this.getPosition(), this.getSoundForCurrentState(), SoundCategory.BLOCKS);
        //TardisUtil.getTardisDimension().playSound(null, getInteriorCentre(), AITSounds.DEMAT, SoundCategory.BLOCKS, 10f, 1f);
        if(this.getTardis() != null)
            if(this.getTardis().getDesktop().getConsolePos() != null) {
                //System.out.println("FYI this should be working :p");
                TardisUtil.getTardisDimension().playSound(null, this.getTardis().getDesktop().getConsolePos(), this.getSoundForCurrentState(), SoundCategory.BLOCKS, 10f, 1f);
            }

        this.runAnimations();

        // A definite thing just in case the animation isnt run

        Timer animTimer = new Timer();
        TardisTravel travel = this;

        animTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (travel.getState() != State.DEMAT)
                    return;

                travel.toFlight();
            }
        }, (long) getSoundLength(this.getMatSoundForCurrentState()) * 1000L);
    }

    @NotNull
    private BlockPos getInteriorCentre() {
        BlockPos firstCorner = this.getTardis().getDesktop().getCorners().getFirst();
        BlockPos secondCorner = this.getTardis().getDesktop().getCorners().getSecond();
        // x^1 - x^2   z^1 - z^2
        // --------- , ---------
        //     2           2
        // firstCorner.getX() + secondCorner.getX()   firstCorner.getZ() + secondCorner.getZ()
        // ---------------------------------------- , ----------------------------------------
        //                    2                                          2
        return new BlockPos((firstCorner.getX() + secondCorner.getX()) / 2, firstCorner.getY(), (firstCorner.getZ() + secondCorner.getZ()) / 2);
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
        if (exterior.getAnimation() == null) return;

        exterior.getAnimation().setupAnimation(this.state);
        //System.out.println(this.state);
    }

    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        this.destination = pos;
    }

    public AbsoluteBlockPos.Directed getDestination() {
        if (this.destination == null) {
            if (this.getPosition() != null)
                this.destination = this.getPosition();
            else {
                // PANIC!!
                AITMod.LOGGER.error("Destination error! resetting to 0 0 0 in overworld");
                this.destination = new AbsoluteBlockPos.Directed(0,0,0, TardisUtil.findWorld(World.OVERWORLD), Direction.NORTH);
            }
        }

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

        exterior.setTardis(this.getTardis());
        this.position.addBlockEntity(exterior);
    }

    public void deleteExterior() {
        this.getPosition().getWorld().getChunk(this.getPosition());
        this.getPosition().getWorld().removeBlock(this.getPosition(),false);
    }

    public void checkShouldRemat() {
        if (!this.shouldRemat)
            return;

        this.materialise();
    }

    @NotNull
    public SoundEvent getSoundForCurrentState() {
        if(this.getTardis() != null)
            return this.getTardis().getExterior().getType().getSound(this.getState()).sound();
        return SoundEvents.INTENTIONALLY_EMPTY;
    }
    public MatSound getMatSoundForCurrentState() {
        if (this.getTardis() != null)
            return this.getTardis().getExterior().getType().getSound(this.getState());
        return AITSounds.LANDED_ANIM; // COUUULD be LANDED_ANIM but null is beteter
    }

    public PosManager getPosManager() {
        if (this.posManager == null)
            this.posManager = new PosManager();

        return this.posManager;
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

        public void schedule(TravelContext context) { }
    }
}
