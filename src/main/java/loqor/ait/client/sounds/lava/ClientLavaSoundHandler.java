package loqor.ait.client.sounds.lava;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ClientLavaSoundHandler extends SoundHandler {
    public static LoopingSound LAVA_SOUND;

    protected ClientLavaSoundHandler() {
    }

    public LoopingSound getRainSound() {
        if (LAVA_SOUND == null)
            LAVA_SOUND = new PositionedLoopingSound(SoundEvents.BLOCK_LAVA_AMBIENT,
                    SoundCategory.BLOCKS,
                    tardis().getDesktop().doorPos().getPos(), 0.2f);

        return LAVA_SOUND;
    }

    public static ClientLavaSoundHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientLavaSoundHandler handler = new ClientLavaSoundHandler();
        handler.generate();
        return handler;
    }

    private void generate() {

        if (tardis() == null) return;

        if (LAVA_SOUND == null && tardis().getDesktop().doorPos().getPos() != null)
            LAVA_SOUND = new PositionedLoopingSound(SoundEvents.BLOCK_LAVA_AMBIENT,
                    SoundCategory.WEATHER,
                    tardis().getDesktop().doorPos().getPos(), 0.2f);

        this.sounds = new ArrayList<>();
        this.sounds.add(
                LAVA_SOUND
        );
    }

    public boolean isPlayerInATardis() {
        if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            return false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);

        return found != null;
    }

    public Tardis tardis() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
            return null;

        return TardisUtil.findTardisByInterior(player.getBlockPos(), false);
    }

    public boolean isLanded() {
        Tardis tardis = this.tardis();
        return (tardis != null && tardis.travel().getState() == TravelHandlerBase.State.LANDED);
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null)
            this.generate();

        if (isLanded() && isPlayerInATardis() && isInLava()) {
            this.startIfNotPlaying(getRainSound());
        } else {
            this.stopSound(LAVA_SOUND);
        }
    }

    public boolean isInLava() {
        Tardis tardis = this.tardis();

        if (tardis.travel().position() != null) return false;

        World world = tardis.travel().position().getWorld();
        BlockPos tardisPos = tardis.travel().position().getPos();

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                BlockPos blockPos = tardisPos.add(xOffset, 0, yOffset);
                if (world.getBlockState(blockPos).getBlock() == Blocks.LAVA) {
                    return true;
                }
            }
        }

        return false;
    }
}