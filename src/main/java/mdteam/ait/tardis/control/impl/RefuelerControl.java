package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.FlightUtil;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.Random;

public class RefuelerControl extends Control {
    public RefuelerControl() {
        super("refueler");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        if(tardis.isGrowth()) return false;
        if(tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
            this.addToControlSequence(tardis);
            if(tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) return false;
        }
        if(PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE)) {
            //if (TardisUtil.isRiftChunk((ServerWorld) tardis.getTravel().getPosition().getWorld(), tardis.getTravel().getExteriorPos())) {
            Random random = new Random();
                tardis.setRefueling(!tardis.isRefueling());
                Text enabled = Text.translatable("tardis.message.control.refueler.enabled");
                Text disabled = Text.translatable("tardis.message.control.refueler.disabled");
                player.sendMessage((tardis.isRefueling()? enabled : disabled), true);
                if (tardis.isRefueling()) {
                    FlightUtil.playSoundAtConsole(tardis, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 10, 1);
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
        return false;
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.BLOCK_LEVER_CLICK;
    }
    @Override
    public boolean shouldFailOnNoPower() {
        return false;
    }
}
