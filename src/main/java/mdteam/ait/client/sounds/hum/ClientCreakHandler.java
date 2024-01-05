package mdteam.ait.client.sounds.hum;

import mdteam.ait.client.sounds.LoopingSound;
import mdteam.ait.client.sounds.PlayerFollowingLoopingSound;
import mdteam.ait.client.sounds.PlayerFollowingSound;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.registry.CreakRegistry;
import mdteam.ait.registry.HumsRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.ServerHumHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.sound.CreakSound;
import mdteam.ait.tardis.sound.HumSound;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.SoundHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mdteam.ait.AITMod.AIT_CONFIG;

public class ClientCreakHandler extends SoundHandler {
    private static final Random random = new Random();

    protected ClientCreakHandler() {}
    public static ClientCreakHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientCreakHandler handler = new ClientCreakHandler();
        handler.generateCreaks();
        return handler;
    }

    private void generateCreaks() {
        this.sounds = new ArrayList<>();
        this.sounds.addAll(registryToList());
    }

    /**
     * Converts all the {@link CreakSound}'s in the {@link CreakRegistry} to {@link LoopingSound} so they are usable
     *
     * @return A list of {@link LoopingSound} from the {@link CreakRegistry}
     */
    private List<LoopingSound> registryToList() {
        List<LoopingSound> list = new ArrayList<>();

        for (CreakSound sound : CreakRegistry.REGISTRY) {
            list.add(new PlayerFollowingLoopingSound(sound.sound(), SoundCategory.AMBIENT, AIT_CONFIG.INTERIOR_HUM_VOLUME()));
        }

        return list;
    }

    public boolean isPlayerInATardis() {
        if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            return false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos());

        return found != null;
    }

    public BlockPos randomNearConsolePos(AbsoluteBlockPos.Directed consolePos) {
        return consolePos.add(random.nextInt(8) - 1, 0, random.nextInt(8) - 1);
    }

    public Tardis tardis() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return null;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos());
        return found;
    }

    public void playRandomCreak() {
        CreakSound chosen = CreakRegistry.getRandomCreak();
        if(chosen.equals(CreakRegistry.WHISPER) && ClientTardisUtil.getCurrentTardis().getDesktop().getConsolePos() != null) {
            startIfNotPlaying(new PositionedSoundInstance(chosen.sound(), SoundCategory.HOSTILE, 0.5f, 1.0f, net.minecraft.util.math.random.Random.create(), randomNearConsolePos(ClientTardisUtil.getCurrentTardis().getDesktop().getConsolePos())));
            return;
        }
        PlayerFollowingSound following = new PlayerFollowingSound(chosen.sound(), SoundCategory.AMBIENT, AIT_CONFIG.INTERIOR_HUM_VOLUME());
        startIfNotPlaying(following);
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null) this.generateCreaks();

        if (client.player == null) return;

        if (!isPlayerInATardis() || tardis().hasPower()) { // todo should they play even with power? just make them more rare??
            this.stopSounds();
            return;
        }

        // theyre in a tardis and theres no power so play creaks boi
        if (random.nextInt(256) == 32) {
            this.playRandomCreak();
        }
    }
}