package loqor.ait.mixin.server;

import dev.drtheo.blockqueue.data.TimeUnit;
import dev.drtheo.scheduler.ClientScheduler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.data.Loyalty;

@Mixin(BedBlock.class)
public class BedInTardisMixin {
    @Inject(at = @At("HEAD"), method = "onUse")
    private void ait$useOn(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                           BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient()) { this.onClientSleep(player); }
    }

    @Unique @Environment(EnvType.CLIENT)
    private void onClientSleep(PlayerEntity player) {
        Tardis tardis = ClientTardisUtil.getCurrentTardis();
        if (tardis == null || AITMod.CONFIG.CLIENT.DISABLE_LOYALTY_SLEEPING_ACTIONBAR) return;

        Loyalty loyalty = tardis.loyalty().get(player);

        Text message = switch (loyalty.type()) {
            case REJECT -> Text.literal("The TARDIS groans in frustration...");
            case NEUTRAL ->
                    Text.literal("The TARDIS hums softly, neither welcoming nor dismissing your presence...");
            case COMPANION ->
                    Text.literal("The TARDIS glows warmly, as if glad to have you along for the journey...");
            case PILOT -> Text.literal("The TARDIS hums gently, showing trust...");
            case OWNER ->
                    Text.literal("The TARDIS vibrates gently, a sound of reassurance that it will always be here for you...");
        };
        player.sendMessage(message, true);

        SoundEvent sound = switch(loyalty.type()) {
            case PILOT -> AITSounds.WHISPER;
            case REJECT -> AITSounds.TARDIS_REJECTION_SFX;
            default -> AITSounds.GROAN;
        };
        ClientScheduler.get().runTaskLater(() -> player.playSound(sound, 1f, 1f), TimeUnit.TICKS, 20);
    }
}