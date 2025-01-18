package loqor.ait.core.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;

public class FoodCreationControl extends Control {

    public FoodCreationControl() {
        super("food_creation");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console,
                             boolean leftClick) {
       player.sendMessage(Text.literal("Ya wana coppa tea??"));
        return true;
    }

    @Override
    public boolean requiresPower() {
        return true; // todo remember to change this back when this becomes a HADS control again!!
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.ALARM;
    }
}
