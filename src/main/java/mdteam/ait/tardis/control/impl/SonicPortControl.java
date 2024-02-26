package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.item.SonicItem;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisConsole;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.SonicHandler;
import mdteam.ait.tardis.util.FlightUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class SonicPortControl extends Control { // TODO - implement onto consoles
    public SonicPortControl() {
        super("sonic_port");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick, TardisConsole console) {
        SonicHandler handler = tardis.getHandlers().getSonic();
        boolean hasSonic = handler.hasSonic();
        boolean shouldEject = leftClick || player.isSneaking();

        if (hasSonic && shouldEject) {
            handler.spawnItem();
            return true;
        }

        ItemStack stack = player.getMainHandStack();

        if (!(stack.getItem() instanceof SonicItem)) return false;

        handler.set(stack, true);
        handler.markHasSonic();
        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

        FlightUtil.playSoundAtConsole(tardis, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 6f, 1);

        return true;
    }
}