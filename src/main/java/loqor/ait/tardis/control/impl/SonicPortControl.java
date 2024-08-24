package loqor.ait.tardis.control.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import loqor.ait.core.item.SonicItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.SonicHandler;

public class SonicPortControl extends Control {

    public SonicPortControl() {
        super("sonic_port");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console,
            boolean leftClick) {
        SonicHandler handler = tardis.sonic();

        if (handler.getConsoleSonic() != null && (leftClick || player.isSneaking())) {
            SonicHandler.spawnItem(world, console, handler.takeConsoleSonic());
            return true;
        }

        if (player.getMainHandStack().getItem() instanceof SonicItem linker
                && (!player.getMainHandStack().getOrCreateNbt().contains("tardis") || player.isSneaking())) {

            linker.link(player.getMainHandStack(), tardis);
            world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS,
                    1.0F, 1.0F);

            Vec3d vec3d = Vec3d.ofBottomCenter(console).add(0.0, 1.2f, 0.0);

            world.spawnParticles(ParticleTypes.GLOW, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 12, 0.4F, 1F, 0.4F,
                    5.0F);
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 12, 0.4F,
                    1F, 0.4F, 5.0F);
        }

        ItemStack stack = player.getMainHandStack();

        if (!(stack.getItem() instanceof SonicItem))
            return false;

        handler.insertConsoleSonic(stack, console);
        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

        TardisDesktop.playSoundAtConsole(console, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 6f,
                1);
        return true;
    }

    @Override
    public boolean requiresPower() {
        return false;
    }
}
