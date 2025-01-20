package loqor.ait.core.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import loqor.ait.api.TardisComponent;
import loqor.ait.core.AITSounds;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.core.tardis.handler.CloakHandler;

public class CloakControl extends Control {

    public CloakControl() {
        // ⬚ ?
        super("protocol_3");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        CloakHandler cloak = tardis.handler(TardisComponent.Id.CLOAK);

        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);

            world.playSound(null, player.getBlockPos(), AITSounds.PROTOCOL_3, SoundCategory.BLOCKS,
                    1.0F, 1.0F);
            return false;
        }

        cloak.cloaked().set(!cloak.cloaked().get());

        if (cloak.cloaked().get()) {
            world.playSound(null, player.getBlockPos(), AITSounds.PROTOCOL_3, SoundCategory.BLOCKS,
                    1.0F, 1.0F);
        } else {
            world.playSound(null, player.getBlockPos(), AITSounds.PROTOCOL_3ALT,
                    SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.INTENTIONALLY_EMPTY;
    }

    @Override
    protected SubSystem.IdLike requiredSubSystem() {
        return SubSystem.Id.CHAMELEON;
    }
}
