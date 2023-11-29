package the.mdteam.ait.wrapper.server;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.datagen.datagen_providers.AITLanguageProvider;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisTravel;
import the.mdteam.ait.TravelContext;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.swing.plaf.synth.SynthUI;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

//TODO: istg duzo :))))
public class ServerTardisTravel extends TardisTravel {

    public ServerTardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        super(tardis, pos);
    }

    @Override
    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        super.setDestination(pos, withChecks);
        this.sync();
    }

    @Override
    public void setPosition(AbsoluteBlockPos.Directed pos) {
        super.setPosition(pos);
        this.sync();
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        this.sync();
    }

    public static double getSoundEventLengthInSeconds(SoundEvent sound) {
        try {
            // @TODO is no worky??
            AudioInputStream stream =
                    (AudioInputStream) TardisUtil.getServer().getResourceManager().getResource(sound.getId().withPrefixedPath("assets/ait/sounds/")).get().getInputStream();
            AudioFormat format = stream.getFormat();
            long frames = stream.getFrameLength();
            return (frames + 0.0) / format.getFrameRate();
        } catch (Exception e) {
            e.printStackTrace();
            AITMod.LOGGER.error("Could not get sound length for " + sound + "! Returning 5.0");
            return 5.0D;
        }
    }

    @Override
    public void dematerialise(boolean withRemat) {
        super.dematerialise(withRemat);
    }

    @Override
    public void materialise() {
        super.materialise();
    }

    public void sync() {
        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
