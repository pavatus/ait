package loqor.ait.core.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.data.schema.console.variant.renaissance.*;

public class SiegeModeControl extends Control {

    private static final Text enabled = Text.translatable("tardis.message.control.siege.enabled");
    private static final Text disabled = Text.translatable("tardis.message.control.siege.disabled");

    private SoundEvent soundEvent = AITSounds.SIEGE;

    public SiegeModeControl() {
        super("protocol_1913");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        if (tardis.travel().isCrashing() || tardis.travel().getState() != TravelHandlerBase.State.LANDED)
            return true;

        tardis.siege().setActive(!tardis.siege().isActive());
        tardis.alarm().enabled().set(false);
        player.sendMessage(tardis.siege().isActive() ? enabled : disabled, true);

        if (world.getBlockEntity(console) instanceof ConsoleBlockEntity consoleBlockEntity) {
            if (isRenaissanceVariant(consoleBlockEntity)) {
                this.soundEvent = AITSounds.RENAISSANCE_POWER_SIEGE_ALT;
            }
        }

        return false;
    }

    @Override
    public SoundEvent getSound() {
        return this.soundEvent;
    }

    @Override
    public boolean requiresPower() {
        return false;
    }

    @Override
    public long getDelayLength() {
        return 200;
    }

    @Override
    protected SubSystem.IdLike requiredSubSystem() {
        return SubSystem.Id.DESPERATION;
    }

    private boolean isRenaissanceVariant(ConsoleBlockEntity consoleBlockEntity) {
        return consoleBlockEntity.getVariant() instanceof RenaissanceTokamakVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceIdentityVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceIndustriousVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceFireVariant;
    }
}
