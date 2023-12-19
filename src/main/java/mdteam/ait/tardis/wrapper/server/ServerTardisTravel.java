package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

//TODO: istg duzo :))))
public class ServerTardisTravel extends TardisTravel implements TardisTickable {
    private boolean dirty = false;

    public ServerTardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        super(tardis, pos);
    }

    @Override
    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        super.setDestination(pos, withChecks);
        markDirty();
    }

    @Override
    public void setPosition(AbsoluteBlockPos.Directed pos) {
        super.setPosition(pos);
        markDirty();
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        markDirty();
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
        markDirty();
    }

    @Override
    public void materialise() {
        super.materialise();
        markDirty();
    }

    public void sync() {
        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }

    public boolean isDirty() {
        return dirty;
    }
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void startTick(MinecraftServer server) {
        if (isDirty()) this.sync();
    }

    @Override
    public void tick(MinecraftServer server) {}

    @Override
    public void tick(ServerWorld world) {}

    @Override
    public void tick(MinecraftClient client) {}
}
