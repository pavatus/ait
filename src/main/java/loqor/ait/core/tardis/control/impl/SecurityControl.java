package loqor.ait.core.tardis.control.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;
import loqor.ait.core.item.KeyItem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.Loyalty;

public class SecurityControl extends Control {

    public SecurityControl() {
        // ⨷ ?
        super("protocol_19");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        if (!hasMatchingKey(player, tardis))
            return false;

        boolean security = tardis.stats().security().get();
        tardis.stats().security().set(!security);
        return true;
    }

    public static void runSecurityProtocols(Tardis tardis) {
        boolean security = tardis.stats().security().get();
        boolean leaveBehind = tardis.travel().leaveBehind().get();

        if (!security)
            return;

        List<ServerPlayerEntity> forRemoval = new ArrayList<>();

        if (leaveBehind) {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis.asServer())) {
                if (!hasMatchingKey(player, tardis)) {
                    forRemoval.add(player);
                }
            }

            for (ServerPlayerEntity player : forRemoval) {
                TardisUtil.teleportOutside(tardis, player);
            }
        }
    }

    public static boolean hasMatchingKey(ServerPlayerEntity player, Tardis tardis) {
        if (player.hasPermissionLevel(2))
            return true;

        boolean companion = tardis.loyalty().get(player).isOf(Loyalty.Type.COMPANION);

        if (!KeyItem.isKeyInInventory(player))
            return false;

        Collection<ItemStack> keys = KeyItem.getKeysInInventory(player);

        for (ItemStack stack : keys) {
            Tardis found = KeyItem.getTardis(player.getWorld(), stack);

            if (stack.getItem() == AITItems.SKELETON_KEY) {
                return true;
            }

            if (found == tardis)
                return companion;
        }

        return false;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.PROTOCOL_19;
    }

    @Override
    public long getDelayLength() {
        return 50;
    }
}
