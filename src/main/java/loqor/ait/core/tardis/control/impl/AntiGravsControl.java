package loqor.ait.core.tardis.control.impl;

import dev.pavatus.lib.data.CachedDirectedGlobalPos;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;

public class AntiGravsControl extends Control {

    public AntiGravsControl() {
        super("antigravs");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        tardis.travel().antigravs().toggle();

        CachedDirectedGlobalPos globalPos = tardis.travel().position();
        ServerWorld targetWorld = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        targetWorld.getChunkManager().markForUpdate(pos);
        world.scheduleBlockTick(pos, AITBlocks.EXTERIOR_BLOCK, 2);
        return true;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.ANTI_GRAVS;
    }

    @Override
    protected SubSystem.IdLike requiredSubSystem() {
        return SubSystem.Id.GRAVITATIONAL;
    }
}
