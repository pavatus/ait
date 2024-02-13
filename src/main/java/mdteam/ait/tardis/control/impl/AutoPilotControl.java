package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class AutoPilotControl extends Control {
    public AutoPilotControl() {
        // ☸ ?
        super("protocol_116");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        if(tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
            if(tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
                this.addToControlSequence(tardis);
                return false;
            }
        }

        PropertiesHandler.set(tardis, PropertiesHandler.AUTO_LAND, !PropertiesHandler.willAutoPilot(tardis.getHandlers().getProperties()));

        messagePlayer(player, PropertiesHandler.willAutoPilot(tardis.getHandlers().getProperties()));

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
        // fixme translatable
        Text active = Text.translatable("tardis.message.control.protocol_116.active");
        Text inactive = Text.translatable("tardis.message.control.protocol_116.inactive");
        player.sendMessage((autopilot? active : inactive), true);
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.BLOCK_LEVER_CLICK;
    }
}
