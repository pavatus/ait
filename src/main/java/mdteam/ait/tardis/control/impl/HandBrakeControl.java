package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundCategory;
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
            tardis.getTravel().getDestination().getWorld().createExplosion(
                    null, tardis.getTravel().getDestination().getX(),
                    tardis.getTravel().getDestination().getY(),
                    tardis.getTravel().getDestination().getZ(), 4f, true, World.ExplosionSourceType.MOB);
            tardis.getTravel().materialise();
            // fixme }
        }

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean var) {
        // fixme translatable
        String s = var ? "ON" : "OFF";

        player.sendMessage(Text.literal("Handbrake: " + s), true);
    }
}
