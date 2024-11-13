package loqor.ait.mixin.server;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.spongepowered.asm.mixin.Mixin;
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
    private static int soundDelayCounter = 0;
    private static PlayerEntity delayedPlayer;
    private static World delayedWorld;
    private static SoundEvent delayedSound;

    @Inject(at = @At("HEAD"), method = "onUse")
    private void ait$useOn(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                           BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient()) {
            Tardis tardis = ClientTardisUtil.getCurrentTardis();
            if (tardis != null) {
                Loyalty loyalty = tardis.loyalty().get(player);

                // Show particles and message immediately
                if (loyalty != null && loyalty.isOf(Loyalty.Type.PILOT)) {
                    if (!AITMod.AIT_CONFIG.DISABLE_LOYALTY_SLEEPING_ACTIONBAR()) {
                        player.sendMessage(Text.literal("The TARDIS hums gently, showing trust..."), true);
                    }
                    delayedSound = AITSounds.GHOST_MAT;
                } else if (loyalty != null && loyalty.isOf(Loyalty.Type.REJECT)) {
                    if (!AITMod.AIT_CONFIG.DISABLE_LOYALTY_SLEEPING_ACTIONBAR()) {
                        player.sendMessage(Text.literal("The TARDIS groans in frustration..."), true);
                    }
                    delayedSound = AITSounds.GROAN;
                } else if (loyalty != null && loyalty.isOf(Loyalty.Type.COMPANION)) {
                    if (!AITMod.AIT_CONFIG.DISABLE_LOYALTY_SLEEPING_ACTIONBAR()) {
                        player.sendMessage(Text.literal("The TARDIS glows warmly, as if glad to have you along for the journey..."), true);
                    }
                    delayedSound = AITSounds.GROAN;
                } else if (loyalty != null && loyalty.isOf(Loyalty.Type.OWNER)) {
                    if (!AITMod.AIT_CONFIG.DISABLE_LOYALTY_SLEEPING_ACTIONBAR()) {
                        player.sendMessage(Text.literal("The TARDIS vibrates gently, a sound of reassurance that it will always be here for you..."), true);
                    }
                    delayedSound = AITSounds.GROAN;
                } else if (loyalty != null && loyalty.isOf(Loyalty.Type.NEUTRAL)) {
                    if (!AITMod.AIT_CONFIG.DISABLE_LOYALTY_SLEEPING_ACTIONBAR()) {
                        player.sendMessage(Text.literal("The TARDIS hums softly, neither welcoming nor dismissing your presence..."), true);
                    }
                    delayedSound = AITSounds.GROAN;
                } else {
                   if (!AITMod.AIT_CONFIG.DISABLE_LOYALTY_SLEEPING_ACTIONBAR()) {
                       player.sendMessage(Text.literal("The TARDIS hums in uncertainty..."), true);
                   }
                   delayedSound = AITSounds.FAIL_DEMAT;
                }

                // Start sound delay counter
                soundDelayCounter = 20; // 20 ticks for 1-second delay
                delayedPlayer = player;
                delayedWorld = world;
            }
        }
    }

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (soundDelayCounter > 0) {
                soundDelayCounter--;
                if (soundDelayCounter == 0 && delayedPlayer != null && delayedWorld != null) {
                    // Play the delayed sound
                    delayedPlayer.playSound(delayedSound, 1.0F, 1.0F);

                    // Reset delay variables
                    delayedPlayer = null;
                    delayedWorld = null;
                    delayedSound = null;
                }
            }
        });
    }
}