package mdteam.ait.core.tardis.travel;

import com.mojang.logging.LogUtils;
import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.TardisHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Mostly copied from box mod's TARDISTravel class and then ported to fabric
 * Not the best way to do things, especially due to the ongoing issues with saves (also an issue in box mod) @TODO Better implementation?
 * @TODO No sounds implemented, so all sound references are commented out.
 */
public class TardisTravel implements Serializable {
    private transient int MAT_AUDIO_LENGTH = 10;
    private transient final long SECONDS = 1000L;
    private UUID tardisUuid;
    private STATE state;
    private AbsoluteBlockPos destination;
    private boolean handbrakeOn;
    public TardisTravel(UUID tardis) {
        this.tardisUuid = tardis;
        this.handbrakeOn = false;
    }

    public static BlockPos searchForNearestAirBlock(World level, BlockPos pos, Direction direction) {
        if (direction != Direction.UP && direction != Direction.DOWN) {direction = Direction.UP;}

        BlockPos testingPos = pos;

        if (direction == Direction.UP) {
            while (level.getBlockState(testingPos).getBlock() != Blocks.AIR) {
                testingPos = testingPos.up();
            }
        } else {
            while (level.getBlockState(testingPos).getBlock() != Blocks.AIR) {
                testingPos = testingPos.down();
            }
        }

        return testingPos;
    }

    public static BlockPos checkForNearestNonAirBlock(World level, BlockPos pos, Direction direction) {
        if (direction != Direction.UP && direction != Direction.DOWN) {direction = Direction.UP;}

        BlockPos testingPos = pos;

        if (direction == Direction.UP) {
            while (level.getBlockState(testingPos).getBlock() == Blocks.AIR) {
                testingPos = testingPos.up();
            }
        } else {
            while (level.getBlockState(testingPos).getBlock() == Blocks.AIR) {
                testingPos = testingPos.down();
            }
        }

        return testingPos.up();
    }

    public static BlockPos getRandomPosInRange(BlockPos pos, int range) {
        Random random = new Random();

        int xChange = random.nextInt(-range, range);
        int yChange = random.nextInt(-range, range);
        int zChange = random.nextInt(-range, 0);

        return new BlockPos(pos.getX() + xChange, pos.getY() + yChange, pos.getZ() + zChange);
    }

    public Tardis getTardis() {
        return TardisHandler.getTardis(tardisUuid);
    }

    public STATE getState() {
        if (state == null) setState(STATE.LANDED); // assuming its landed if its null, probs not best way

        return state;
    }

    public void setDestination(AbsoluteBlockPos pos, boolean withChecks) {
        if (withChecks) {
            pos = new AbsoluteBlockPos(checkForNearestNonAirBlock(pos.getDimension(), searchForNearestAirBlock(pos.getDimension(),pos.toBlockPos(), Direction.UP), Direction.DOWN), pos.getDirection(), pos.getDimension());
        }
        this.destination = pos;
    }
    public AbsoluteBlockPos getDestination() {
        return destination;
    }
    private boolean canTakeoff() {
        return (getState() == STATE.LANDED) && !(handbrakeOn);
    }
    private boolean getHandbrakeChecks() {
        if (handbrakeOn && getState() == STATE.LANDED) {
            setState(STATE.FAIL_TAKEOFF);
            // getTardis().world().playSound(null, getTardis().getPosition(), SoundsInit.FAIL_TAKEOFF.get(), SoundSource.BLOCKS, 1f,1f);
            runAnimations();
            startHopping();

            // making sure that it does land
            TardisTravel travel = this;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    travel.setState(STATE.LANDED);
                }
            }, MAT_AUDIO_LENGTH * SECONDS);

            return true;
        }
        return false;
    }

    private void runAnimations() {
        // @TODO animations
//        World level = tardis.getLevel();
//        level.getChunkAt(tardis.getPosition());
//        BlockEntity entity = level.getBlockEntity(tardis.getPosition());
//        if (entity instanceof ExteriorBlockEntity) {
//            ((ExteriorBlockEntity) entity).getAnimation().setupAnimation(state);
//        }
    }

    public void startHopping() {
        Random random = new Random();
        TardisTravel travel = this;
        Timer timer = new Timer();
        long MINUTES = SECONDS * 60;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.__hopTakeoff();
            }
        }, random.nextInt(5,10) * MINUTES);
    }

    private void __hopTakeoff() {
        if (getTardis().world().isClient) {return;}

        if (getHandbrakeChecks()) {
            return;
        }

        if (!(canTakeoff())) {
            startHopping();
            return;
        }

        setState(STATE.HOP_TAKEOFF);

        World level = getTardis().world();

        // level.playSound(null,getTardis().getPosition(), SoundsInit.HOP_TAKEOFF.get(), SoundSource.BLOCKS, 1f,1f);

        runAnimations();

        setDestination(new AbsoluteBlockPos(getRandomPosInRange(getTardis().getPosition().toBlockPos(), 10), getTardis().getPosition().getDimension()),true);

        // Timer code for waiting for sound to finish
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Delete the block and rematerialise if needed.
//                ForgeChunkManager.forceChunk((ServerLevel) level, TARDISMod.MODID, travel.tardis.getPosition(),0, 0,true,true);
                travel.setState(STATE.FLIGHT);
                level.getChunk(travel.destination.toBlockPos());

                level.removeBlock(travel.getTardis().getPosition().toBlockPos(), false);

                travel.__hopLand();
            }
        }, 11 * SECONDS);
    }

    private void __hopLand() {
        if (getDestination() == null || getDestination().getDimension().isClient) {
            return;
        }

        setState(STATE.HOP_LAND);

        World level = getDestination().getDimension();

        level.getChunk(getDestination().toBlockPos());

        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING, getDestination().getDirection());
        level.setBlockState(getDestination().toBlockPos(), state,3);
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(getDestination().toBlockPos(), state);
        setUuid(blockEntity.getTardisUuid());
        level.addBlockEntity(blockEntity);
        // level.playSound(null, destination, SoundsInit.HOP_LAND.get(), SoundSource.BLOCKS, 1f, 1f);

        getTardis().setPosition(getDestination());
        getTardis().setUuid(tardisUuid);
        if(level.getBlockEntity(getDestination().toBlockPos()) != null) TardisUtil.updateBlockEntity(getTardis());

        runAnimations();
        startHopping();

        // making sure that it does land
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.setState(STATE.LANDED);
            }
        }, MAT_AUDIO_LENGTH * SECONDS);
    }

    public void dematerialise() {
        dematerialise(false);
    }
    public void dematerialise(boolean withRemat) {
        if (getTardis().getPosition().getDimension().isClient) {return;}
        System.out.println(getTardis().getUuid() + ": " + "DEMATERIALISING");
        System.out.println(getTardis().getUuid() + ": " + "DESTINATION: " + getDestination());
        System.out.println(getTardis().getUuid() + ": " + getState());
        System.out.println(getTardis().getUuid() + ": " + getHandbrakeChecks());
        System.out.println(getTardis().getUuid() + ": " + canTakeoff());

        World level = getTardis().getPosition().getDimension();

        if (getHandbrakeChecks()) {
            return;
        }

        if (!(canTakeoff())) {
                return;
        }

        setState(STATE.DEMAT);

        // level.playSound(null,getTardis().getPosition(), SoundsInit.DEMATERIALISE.get(), SoundSource.BLOCKS, 1f,1f);

        runAnimations();

        // Timer code for waiting for sound to finish
        TardisTravel travel = this;
        Timer timer = new Timer();
        int DEMAT_AUDIO_LENGTH = 8;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.setState(STATE.FLIGHT);
                level.getChunk(travel.getDestination().toBlockPos());

                level.removeBlock(travel.getTardis().getPosition().toBlockPos(), false);

                if (withRemat) {
                    travel.materialise();
                }
            }
        }, DEMAT_AUDIO_LENGTH * SECONDS);
    }

    public void materialise() {
        if (getDestination() == null || getDestination().getDimension().isClient) {return;}

        setState(STATE.MAT);

        World level = getDestination().getDimension();

        level.getChunk(getDestination().toBlockPos());
        if (level == TardisUtil.getTardisDimension()) {
            if (/*AITCommonConfigs.CAN_LAND_IN_TARDIS_DIM.get()*/ true) {
                // level.playSound(null, destination, SoundsInit.EMERGENCY_LAND.get(), SoundSource.BLOCKS, 1f,1f);
                MAT_AUDIO_LENGTH = 16;
            } else {
                // level.playSound(null, destination, SoundsInit.FAIL_LAND.get(), SoundSource.BLOCKS, 1f,1f);
                setDestination(getTardis().getPosition(),false);

                if (getTardis().getPosition().getDimension() == TardisUtil.getTardisDimension()) {
                    setDestination(new AbsoluteBlockPos(getTardis().getPosition().toBlockPos(), AITMod.mcServer.getOverworld()),false);
                }

                materialise();
                return;
            }
        } else {
            // level.playSound(null, destination, SoundsInit.MATERIALISE.get(), SoundSource.BLOCKS, 1f, 1f);
            MAT_AUDIO_LENGTH = 10;
        }

        // Timer code for waiting for sound to finish
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.__materialise();
            }
        }, MAT_AUDIO_LENGTH * SECONDS);
    }

    private void __materialise() {
        if (getDestination().getDimension().isClient) {return;}

        World level = getDestination().getDimension();

        level.getChunk(getDestination().toBlockPos());

        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING, getDestination().getDirection());
        level.setBlockState(getDestination().toBlockPos(), state, 3);
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(getDestination().toBlockPos(), state);
        blockEntity.setTardis(getTardis());
        level.addBlockEntity(blockEntity);
        setUuid(tardisUuid);

        getTardis().setPosition(getDestination());
        getTardis().setUuid(tardisUuid);
        if(level.getBlockEntity(getDestination().toBlockPos()) != null) TardisUtil.updateBlockEntity(getTardis());

        runAnimations();

        // making sure that it does land
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.setState(STATE.LANDED);
            }
        }, MAT_AUDIO_LENGTH * SECONDS);
    }

    public void moveTo(AbsoluteBlockPos destination, boolean withChecks) {
        setDestination(destination,withChecks);
        dematerialise(true);
    }
    public void moveTo(AbsoluteBlockPos destination) {
        moveTo(destination, false);
    }

    public boolean inFlight() {
        return getState() == STATE.FLIGHT;
    }
    public boolean isMaterialising() {
        return getState() == STATE.MAT;
    }
    public boolean isDematerialising() {
        return getState() == STATE.DEMAT;
    }
    public void setState(STATE state) {
        this.state = state;
    }
    public void setUuid(UUID uuid) {
        this.tardisUuid = uuid;
    }
    public void setToggleHandbrake() {
        this.handbrakeOn = !handbrakeOn;
    }

    public enum STATE {
        FAIL_TAKEOFF,
        HOP_TAKEOFF,
        HOP_LAND,
        DEMAT,
        MAT,
        LANDED,
        FLIGHT
    }
}
