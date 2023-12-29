package mdteam.ait.tardis.control.impl;

import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class HandBrakeControl extends Control {
    public HandBrakeControl() {
        super("handbrake");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.setBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE));

        tardis.markDirty();

        messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE));

        if (tardis.getTravel().getState() == TardisTravel.State.FLIGHT) {
            // randomise and force land @todo something better ive got no ideas at 1am loqor
            // fixme tardis.getTravel().setState(TardisTravel.State.CRASH);
            //@TODO make sure this can't be used like a friggin' carpet bomb - Loqor

            // fixme if (tardis.getTravel().getState() == TardisTravel.State.CRASH) {
            tardis.getTravel().getPosManager().increment = 1000; //1000
            RandomiserControl.randomiseDestination(tardis, 10); //10
            TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 3f, 1f);
            tardis.getTravel().getDestination().getWorld().getChunk(tardis.getTravel().getDestination());
                    PropertiesHandler.set(tardis.getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED, true);
                    PropertiesHandler.set(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED, false);

                    // fixme everything below this line to the markdirty() is a WIP, what i want this to do is set the destination to the height of the current dimension,
                    // fixme so that when you crash land, you crash land out of the sky towards this position like in the show; for now, what i want it to do is fall onto the crashed position,
                    // fixme and explode once it touches the ground in the CRASHED state. - Loqor
                    tardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(tardis.getTravel().getDestination().getX(), tardis.getTravel().getDestination().getWorld().getRegistryKey().equals(World.NETHER) ? 128 : 256, tardis.getTravel().getDestination().getZ(), tardis.getTravel().getDestination().getWorld(), tardis.getTravel().getDestination().getDirection()), true);
                    /*tardis.getTravel().getDestination().getWorld().createExplosion(
                    null, tardis.getTravel().getDestination().getX(),
                    tardis.getTravel().getDestination().getY(),
                    tardis.getTravel().getDestination().getZ(), 4f, true, World.ExplosionSourceType.MOB);*/

                    tardis.markDirty();
            tardis.getTravel().materialise();
            TardisEvents.CRASH.invoker().onCrash(tardis);
            // fixme }
        }

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean var) {
        // fixme translatable
        String s = var ? "ON" : "OFF";

        player.sendMessage(Text.literal("Handbrake: " + s), true);
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.HANDBRAKE_LEVER_PULL;
    }
}
