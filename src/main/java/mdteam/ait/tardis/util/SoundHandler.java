package mdteam.ait.tardis.util;

import mdteam.ait.AITMod;
import mdteam.ait.client.sounds.LoopingSound;
import mdteam.ait.client.sounds.PlayerFollowingLoopingSound;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.handler.TardisLink;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SoundHandler {
    protected List<LoopingSound> sounds; // shouldnt really be LoopingSound

    protected SoundHandler() {}

    public static SoundHandler create(LoopingSound... list) {
        SoundHandler handler = new SoundHandler();

        handler.sounds = new ArrayList<>();
        handler.sounds.addAll(java.util.List.of(list));

        return handler;
    }

    public void startIfNotPlaying(SoundEvent event) {
        if (!isPlaying(event)) startSound(event);
    }
    public void startIfNotPlaying(SoundInstance sound) {
        if (!isPlaying(sound)) startSound(sound);
    }

    public boolean isPlaying(SoundEvent event) {
        return MinecraftClient.getInstance().getSoundManager().isPlaying(findSoundByEvent(event));
    }
    public boolean isPlaying(SoundInstance sound) {
        return MinecraftClient.getInstance().getSoundManager().isPlaying(sound);
    }

    /**
     * Searches through the sounds in this handler and starts playing whichever one matches the SoundEvent given
     * @param event the event to search for
     */
    public void startSound(SoundEvent event) {
        SoundInstance sound = findSoundByEvent(event);

        MinecraftClient.getInstance().getSoundManager().play(sound);
    }
    public void startSound(SoundInstance sound) {
        MinecraftClient.getInstance().getSoundManager().play(sound);
    }
    public void stopSound(SoundEvent event) {
        MinecraftClient.getInstance().getSoundManager().stop(findSoundByEvent(event));
    }
    public void stopSound(SoundInstance sound) {
        MinecraftClient.getInstance().getSoundManager().stop(sound);
    }
    public void stopSounds() {
        if (this.sounds == null) return;

        for (LoopingSound sound : this.sounds) {
            this.stopSound(sound);
        }
    }

    /**
     * Finds the first sound instance that matches the event given fixme i hate this sm and it doesnt work for sounds which are randomised
     * @return
     */
    public SoundInstance findSoundByEvent(SoundEvent event) {
        return findSoundById(event.getId());
    }
    public SoundInstance findSoundById(Identifier id) {
        Identifier temp;

        for (SoundInstance sound : this.sounds) {
            temp = sound.getId();

            if (temp.equals(id)) return sound;
        }

        AITMod.LOGGER.error("Could not find sound " + id + " in list, returning empty sound!");
        return new PlayerFollowingLoopingSound(SoundEvents.INTENTIONALLY_EMPTY, SoundCategory.NEUTRAL);
    }
}
