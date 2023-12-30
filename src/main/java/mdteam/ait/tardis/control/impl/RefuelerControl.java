package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class RefuelerControl extends Control {
    public RefuelerControl() {
        super("refueler");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        if(PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE)) {
            //if (TardisUtil.isRiftChunk((ServerWorld) tardis.getTravel().getPosition().getWorld(), tardis.getTravel().getExteriorPos())) {
                tardis.setRefueling(!tardis.isRefueling());
                player.sendMessage(Text.literal("Refueling: " + (tardis.isRefueling() ? "Enabled" : "Disabled")), true);
                if (tardis.getDesktop().getConsolePos() != null && tardis.isRefueling()) {
                    world.playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 10, 1);
                }
                //return true;
            //}
        //player.sendMessage(Text.literal("Not positioned within a viable rift!"), true);
        //if (tardis.getDesktop().getConsolePos() != null && !tardis.isRefueling()) {
        //    world.playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON.value(), SoundCategory.BLOCKS, 10, 1);
        //}
        //if (tardis.isRefueling()) tardis.setRefueling(false); // we shouldnt be refueling if we're not in a rift
        return true;
        }
        tardis.markDirty();
        return false;
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.BLOCK_LEVER_CLICK;
    }
    @Override
    public boolean shouldFailOnNoFuel() {
        return false;
    }
}
