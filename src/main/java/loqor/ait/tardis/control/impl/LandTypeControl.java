package loqor.ait.tardis.control.impl;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.travel.TravelHandlerBase;

public class LandTypeControl extends Control {

    public LandTypeControl() {
        super("land_type");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        if (player.isSneaking()) {
            tardis.travel().horizontalSearch().flatMap(value -> {
                value = !value;
                messageXPlayer(player, value);

                return value;
            });

            return false;
        }

        tardis.travel().verticalSearch().flatMap(value -> {
            value = value.next();
            messageYPlayer(player, value);

            return value;
        });

        return false;
    }

    public void messageYPlayer(ServerPlayerEntity player, TravelHandlerBase.GroundSearch value) {
        player.sendMessage(Text.translatable("message.ait.control.ylandtype", value), true);
    }

    public void messageXPlayer(ServerPlayerEntity player, boolean var) {
        Text on = Text.translatable("message.ait.control.xlandtype.on");
        Text off = Text.translatable("message.ait.control.xlandtype.off");
        player.sendMessage(var ? on : off, true);
    }
}
