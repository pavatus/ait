package loqor.ait.core.tardis.control.impl;

import io.wispforest.owo.ops.WorldOps;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITSounds;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.data.DirectedGlobalPos;

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

        tardis.travel().antigravs().flatMap(value -> !value);

        DirectedGlobalPos.Cached globalPos = tardis.travel().position();
        World targetWorld = globalPos.getWorld();
        BlockPos pos = globalPos.getPos();

        WorldOps.updateIfOnServer(targetWorld, pos);
        world.scheduleBlockTick(pos, targetWorld.getBlockState(pos).getBlock(), 2);

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.HANDBRAKE_LEVER_PULL;
    }

    @Override
    protected SubSystem.IdLike requiredSubSystem() {
        return SubSystem.Id.GRAVITATIONAL;
    }
}
