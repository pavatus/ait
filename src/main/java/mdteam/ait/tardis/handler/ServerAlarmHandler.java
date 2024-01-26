package mdteam.ait.tardis.handler;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.UUID;

// use this as reference for starting other looping sounds on the exterior
public class ServerAlarmHandler extends TardisLink {
    // fixme this is bad bad but i cant be assed with packets and thinking so hardcoding this value will be okay for now
    public static final int CLOISTER_LENGTH_TICKS = 3 * 20;
    private int soundCounter = 0; // decides when to start the next cloister sound

    public ServerAlarmHandler(UUID tardisId) {
        super(tardisId);
    }

    public void enable() {
        PropertiesHandler.setBool(tardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED, true);
        tardis().markDirty();
    }

    public void disable() {
        PropertiesHandler.setBool(tardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED, false);
        tardis().markDirty();
    }

    public boolean isEnabled() {
        return PropertiesHandler.getBool(tardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);
    }

    public void toggle() {
        if (isEnabled()) disable();
        else enable();
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        if (!isEnabled()) {

            // @TODO make a new control that makes it (by default) detect hostile entities in the interior,
            //  @TODO maybe a property for the distance from the door as well? - Loqor

            /*for (Entity entity : TardisUtil.getEntitiesInInterior(tardis(), 20)) {
                if (entity instanceof HostileEntity) {
                    this.enable();
                }
            }*/
            return;
        }

        soundCounter++;

        if (soundCounter >= CLOISTER_LENGTH_TICKS) {
            soundCounter = 0;
            getExteriorPos().getWorld().playSound(null, getExteriorPos(), AITSounds.CLOISTER, SoundCategory.AMBIENT, 0.5f, 0.5f);
        }
    }
}
