package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.sound.SoundEvent;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

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
        this.sync();
    }

    @Override
    public void materialise() {
        super.materialise();
        this.sync();
    }

    public void sync() {
        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }
}
