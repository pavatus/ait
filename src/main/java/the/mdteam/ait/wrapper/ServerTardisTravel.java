package the.mdteam.ait.wrapper;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisTravel;
import the.mdteam.ait.TravelContext;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ServerTardisTravel extends TardisTravel {

    public ServerTardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        super(tardis, pos);
    }

    @Override
    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        super.setDestination(pos, withChecks);
        ((ServerTardis) this.tardis).sync();
        ((ServerTardis) this.tardis).duzoJankSync();
    }

    @Override
    public void setPosition(AbsoluteBlockPos.Directed pos) {
        super.setPosition(pos);
        ((ServerTardis) this.tardis).sync();
        ((ServerTardis) this.tardis).duzoJankSync();
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        ((ServerTardis) this.tardis).sync();
        ((ServerTardis) this.tardis).duzoJankSync();
    }

    public static double getSoundEventLengthInSeconds(SoundEvent sound) {
        return 2.0d;

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

    @Override
    public void dematerialise(boolean withRemat) {
        super.dematerialise(withRemat);

        if (this.getPosition().getWorld().isClient())
            return;

        ServerWorld world = (ServerWorld) this.getPosition().getWorld();
        world.getChunk(this.getPosition());

        this.setState(State.DEMAT);
        TravelContext context = new TravelContext(this, this.getPosition(),this.getDestination());

        world.playSound(null, this.getPosition(), AITSounds.DEMAT, SoundCategory.BLOCKS);

        Timer animTimer = new Timer();
        TardisTravel travel = this;

        System.out.println(getSoundEventLengthInSeconds(AITSounds.DEMAT));

        animTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                travel.getState().next(context);

                world.getChunk(travel.getDestination());
                world.removeBlock(travel.getPosition(),false);

                if (withRemat) {
                    travel.materialise();
                }
            }
        }, (long) getSoundEventLengthInSeconds(AITSounds.DEMAT) * 1000L);
    }

    @Override
    public void materialise() {
        super.materialise();

        if (this.getPosition().getWorld().isClient())
            return;

        if (this.getDestination() == null)
            return;

        this.setState(State.MAT);

        ServerWorld world = (ServerWorld) this.getPosition().getWorld();
        TravelContext context = new TravelContext(this, this.getPosition(),this.getDestination());

        world.playSound(null, this.getPosition(), AITSounds.MAT, SoundCategory.BLOCKS);

        Timer animTimer = new Timer();

        Runnable mat = () -> {
            ServerWorld destWorld = (ServerWorld) this.getDestination().getWorld();
            destWorld.getChunk(this.getDestination());

            this.setPosition(this.getDestination());

            ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
            BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING,this.getDestination().getDirection());
            destWorld.setBlockState(this.getDestination(), state);
            ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.getDestination(), state);
            destWorld.addBlockEntity(blockEntity);
            this.getState().next(context);

            // blockEntity.refindTardis();
        };

        animTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mat.run();
            }
        }, (long) getSoundEventLengthInSeconds(AITSounds.MAT) * 1000L);
    }
}
