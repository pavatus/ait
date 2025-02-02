package loqor.ait.core.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.data.schema.console.variant.renaissance.*;

public class DoorLockControl extends Control {

    private SoundEvent soundEvent = AITSounds.DOOR_LOCK;

    public DoorLockControl() {
        super("door_lock");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        boolean isRenaissance = false;
        if (world.getBlockEntity(console) instanceof ConsoleBlockEntity consoleBlockEntity) {
            isRenaissance = isRenaissanceVariant(consoleBlockEntity);
        }

        this.soundEvent = isRenaissance ? AITSounds.RENAISSANCE_LOCK_ALT : AITSounds.DOOR_LOCK;

        tardis.door().interactToggleLock(player);
        return true;
    }

    @Override
    public SoundEvent getSound() {
        return this.soundEvent;
    }

    private boolean isRenaissanceVariant(ConsoleBlockEntity consoleBlockEntity) {
        return consoleBlockEntity.getVariant() instanceof RenaissanceTokamakVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceIndustriousVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceIdentityVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceFireVariant;
    }
}
