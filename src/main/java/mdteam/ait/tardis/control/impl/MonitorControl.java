package mdteam.ait.tardis.control.impl;

import mdteam.ait.AITMod;
import mdteam.ait.client.AITModClient;
import mdteam.ait.tardis.TardisConsole;
import mdteam.ait.tardis.control.Control;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundEvents;

public class MonitorControl extends Control {
    public MonitorControl() {
        super("monitor");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick, TardisConsole console) {
        if (tardis == null) return false; // Why would this be null loqor

        player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
        AITMod.openScreen(player, 0, tardis.getUuid(), console.uuid());

        return true;
    }
}
