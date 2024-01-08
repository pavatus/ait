package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

public class AntiGravsControl extends Control {
    public AntiGravsControl() {
        super("antigravs");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        PropertiesHandler.set(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED));
        tardis.markDirty();

        messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED));

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.HANDBRAKE_LEVER_PULL;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean autopilot) {
        // fixme translatable
        Text active = Text.translatable("tardis.message.control.antigravs.active");
        Text inactive = Text.translatable("tardis.message.control.antigravs.inactive");
        player.sendMessage((autopilot ? active : inactive), true);
    }
}
