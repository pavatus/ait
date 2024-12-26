package loqor.ait.registry.impl;

import java.util.Calendar;

import dev.pavatus.register.datapack.SimpleDatapackRegistry;

import net.minecraft.network.PacketByteBuf;

import loqor.ait.AITMod;
import loqor.ait.client.sounds.ClientSoundManager;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;
import loqor.ait.data.hum.DatapackHum;
import loqor.ait.data.hum.Hum;

public class HumRegistry extends SimpleDatapackRegistry<Hum> {
    private static final HumRegistry instance = new HumRegistry();

    protected HumRegistry() {
        super(DatapackHum::fromInputStream, DatapackHum.CODEC, "hum", true);
    }

    public static HumRegistry getInstance() {
        return instance;
    }

    public static Hum CORAL;
    public static Hum CHRISTMAS;

    @Override
    protected void defaults() {
        CORAL = register(Hum.create(AITMod.MOD_ID, "coral", AITSounds.CORAL_HUM));
        if (AITItems.isUnlockedOnThisDay(Calendar.JANUARY,3)) {
            CHRISTMAS = register(Hum.create(AITMod.MOD_ID, "christmas", AITSounds.CHRISTMAS_HUM));
        }
    }

    @Override
    public Hum fallback() {
        return CORAL;
    }

    @Override
    public void readFromServer(PacketByteBuf buf) {
        super.readFromServer(buf);

        ClientSoundManager.getHum().onSynced();
    }
}
