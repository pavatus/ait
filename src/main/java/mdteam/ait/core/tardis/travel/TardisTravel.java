package mdteam.ait.core.tardis.travel;

import com.mojang.logging.LogUtils;
import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.tardis.Tardis;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static mdteam.ait.core.helper.TardisUtil.getTardisComponent;

/**
 * Mostly copied from box mod's TARDISTravel class and then ported to fabric
 * Not the best way to do things, especially due to the ongoing issues with saves (also an issue in box mod) @TODO Better implementation?
 * @TODO No sounds implemented, so all sound references are commented out.
 */
public class TardisTravel {
    private final int DEMAT_AUDIO_LENGTH = 8;
    private int MAT_AUDIO_LENGTH = 10;
    private final long SECONDS = 1000L;
    private final long MINUTES = SECONDS * 60;
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
        System.out.println("WHY IS THIS SO LITERALLY MUNTED BRO" + this.tardisUuid);
        if(this.tardisUuid == null) this.tardisUuid = getTardisComponent().getUuid();
        return TardisUtil.getTardisFromUuid(this.tardisUuid); // Ensures the Tardis class is always up to date to avoid sync issues, may cause lag though @TODO
    }

    public STATE state() {
        if (this.state == null) this.state = STATE.LANDED; // assuming its landed if its null, probs not best way

        return this.state;
    }

    public void setDestination(AbsoluteBlockPos pos, boolean withChecks) {
        if (withChecks) {
            pos = new AbsoluteBlockPos(pos.getDimension(),pos.getDirection(),
                    checkForNearestNonAirBlock(pos.getDimension(),
                            searchForNearestAirBlock(pos.getDimension(),pos, Direction.UP),
                            Direction.DOWN)
            );
        }
        this.destination = pos;
    }
    public AbsoluteBlockPos getDestination() {
        return this.destination;
    }
    private boolean canTakeoff() {
        return (this.state() == STATE.LANDED) && !(this.handbrakeOn);
    }
    private boolean runHandbrakeChecks() {
        if (this.handbrakeOn && this.state() == STATE.LANDED) {
            this.state = STATE.FAIL_TAKEOFF;
            // this.getTardis().world().playSound(null, this.getTardis().getPosition(), SoundsInit.FAIL_TAKEOFF.get(), SoundSource.BLOCKS, 1f,1f);
            this.runAnimations();
            this.startHopping();

            // making sure that it does land
            TardisTravel travel = this;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    travel.state = STATE.LANDED;
                }
            }, MAT_AUDIO_LENGTH * SECONDS);

            return true;
        }
        return false;
    }

    private void runAnimations() {
        // @TODO animations
//        World level = this.tardis.getLevel();
//        level.getChunkAt(this.tardis.getPosition());
//        BlockEntity entity = level.getBlockEntity(this.tardis.getPosition());
//        if (entity instanceof ExteriorBlockEntity) {
//            ((ExteriorBlockEntity) entity).getAnimation().setupAnimation(this.state);
//        }
    }

    public void startHopping() {
        Random random = new Random();
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.__hopTakeoff();
            }
        }, random.nextInt(5,10) * MINUTES);
    }

    private void __hopTakeoff() {
        if (this.getTardis().world().isClient) {return;}

        if (this.runHandbrakeChecks()) {
            return;
        }

        if (!(this.canTakeoff())) {
            this.startHopping();
            return;
        }

        this.state = STATE.HOP_TAKEOFF;

        World level = this.getTardis().world();

        // level.playSound(null,this.getTardis().getPosition(), SoundsInit.HOP_TAKEOFF.get(), SoundSource.BLOCKS, 1f,1f);

        this.runAnimations();

        this.setDestination(new AbsoluteBlockPos(this.getTardis().getPosition().getDimension(),getRandomPosInRange(this.getTardis().getPosition(), 10)),true);

        // Timer code for waiting for sound to finish
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Delete the block and rematerialise if needed.
//                ForgeChunkManager.forceChunk((ServerLevel) level, TARDISMod.MODID, travel.tardis.getPosition(),0, 0,true,true);
                travel.state = STATE.FLIGHT;
                level.getChunk(travel.destination);

                level.removeBlock(travel.getTardis().getPosition(), false);

                travel.__hopLand();
            }
        }, 11 * SECONDS);
    }

    private void __hopLand() {
        if (this.destination == null || this.destination.getDimension().isClient) {
            return;
        }

        this.state = STATE.HOP_LAND;

        World level = this.destination.getDimension();

        level.getChunk(this.destination);

        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING, this.destination.getDirection());
        level.setBlockState(this.destination, state,3);
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.destination, state);
        this.link(blockEntity.getTardisUuid());
        level.addBlockEntity(blockEntity);
        // level.playSound(null, this.destination, SoundsInit.HOP_LAND.get(), SoundSource.BLOCKS, 1f, 1f);

        this.getTardis().setPosition(this.destination);
        this.getTardis().setUuid(this.tardisUuid);
        if(level.getBlockEntity(this.destination) != null) TardisUtil.updateBlockEntity(this.getTardis());

        this.runAnimations();
        this.startHopping();

        // making sure that it does land
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.state = STATE.LANDED;
            }
        }, MAT_AUDIO_LENGTH * SECONDS);
    }

    public void dematerialise() {
        this.dematerialise(false);
    }
    public void dematerialise(boolean withRemat) {
        if (this.getTardis().world().isClient) {return;}
        System.out.println("DEMATERIALISING");
        System.out.println(this.state());
        System.out.println(this.runHandbrakeChecks());
        System.out.println(this.canTakeoff());

        World level = this.getTardis().world();

        if (this.runHandbrakeChecks()) {
            return;
        }

        if (!(this.canTakeoff())) {
            return;
        }

        this.state = STATE.DEMAT;

        // level.playSound(null,this.getTardis().getPosition(), SoundsInit.DEMATERIALISE.get(), SoundSource.BLOCKS, 1f,1f);

        this.runAnimations();

        // Timer code for waiting for sound to finish
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Delete the block and rematerialise if needed.
//                ForgeChunkManager.forceChunk((ServerLevel) level, TARDISMod.MODID, travel.tardis.getPosition(),0, 0,true,true);
                travel.state = STATE.FLIGHT;
                level.getChunk(travel.destination);

                level.removeBlock(travel.getTardis().getPosition(), false);

                if (withRemat) {
                    travel.materialise();
                }
            }
        }, DEMAT_AUDIO_LENGTH * SECONDS);
    }

    public void materialise() {
        if (this.destination == null || this.destination.getDimension().isClient) {return;}

        this.state = STATE.MAT;

        World level = this.destination.getDimension();

        level.getChunk(this.destination);
        if (level == TardisUtil.getTardisDimension()) {
            if (/*AITCommonConfigs.CAN_LAND_IN_TARDIS_DIM.get()*/ true) {
                // level.playSound(null, this.destination, SoundsInit.EMERGENCY_LAND.get(), SoundSource.BLOCKS, 1f,1f);
                MAT_AUDIO_LENGTH = 16;
            } else {
                // level.playSound(null, this.destination, SoundsInit.FAIL_LAND.get(), SoundSource.BLOCKS, 1f,1f);
                this.setDestination(this.getTardis().getPosition(),false);

                if (this.getTardis().getPosition().getDimension() == TardisUtil.getTardisDimension()) {
                    this.setDestination(new AbsoluteBlockPos(AITMod.mcServer.getOverworld(), this.getTardis().getPosition()),false);
                }

                this.materialise();
                return;
            }
        } else {
            // level.playSound(null, this.destination, SoundsInit.MATERIALISE.get(), SoundSource.BLOCKS, 1f, 1f);
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
        if (this.destination.getDimension().isClient) {return;}

        World level = this.destination.getDimension();

        level.getChunk(this.destination);

        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING, this.destination.getDirection());
        level.setBlockState(this.destination, state, 3);
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.destination, state);
        level.addBlockEntity(blockEntity);
        this.link(blockEntity.getTardisUuid());

        this.getTardis().setPosition(this.destination);
        this.getTardis().setUuid(this.tardisUuid);
        if(level.getBlockEntity(this.destination) != null) TardisUtil.updateBlockEntity(this.getTardis());

        this.runAnimations();

        // making sure that it does land
        TardisTravel travel = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.state = STATE.LANDED;
            }
        }, MAT_AUDIO_LENGTH * SECONDS);
    }

    public void moveTo(AbsoluteBlockPos destination, boolean withChecks) {
        this.setDestination(destination,withChecks);
        this.dematerialise(true);
    }
    public void moveTo(AbsoluteBlockPos destination) {
        this.moveTo(destination, false);
    }

    public boolean inFlight() {
        return this.state() == STATE.FLIGHT;
    }
    public boolean isMaterialising() {return this.state() == STATE.MAT;}
    public boolean isDematerialising() {return this.state() == STATE.DEMAT;}
    public void setState(STATE state) {
        this.state = state;
    }
    public void link(UUID uuid) {
        this.tardisUuid = uuid;
    }
    public void changeHandbrake() {
        this.handbrakeOn = !this.handbrakeOn;
    }

    public static class Serializer {
        public NbtCompound serialize(TardisTravel travel) {
            NbtCompound tag = new NbtCompound();
            this.serialize(tag, travel);
            return tag;
        }
        public void serialize(NbtCompound nbt, TardisTravel travel) {
            if (travel.tardisUuid != null) nbt.putUuid("uuid", travel.getTardis().getUuid());
            if (travel.destination != null) nbt.put("destination", travel.destination.writeToNbt());
        }
        public TardisTravel deserialize(NbtCompound nbt) {
            if (nbt.contains("destination") && nbt.contains("uuid")) {
                TardisTravel travel = new TardisTravel(nbt.getUuid("uuid"));
                travel.setDestination(AbsoluteBlockPos.readFromNbt(nbt.getCompound("destination")), false);
                return travel;
            }
            return null;
        }
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
