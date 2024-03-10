package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.item.SonicItem;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisConsole;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.SonicHandler;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class SonicPortControl extends Control {
	public SonicPortControl() {
		super("sonic_port");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick, TardisConsole console) {
		SonicHandler handler = tardis.getHandlers().getSonic();
		boolean hasSonic = handler.hasSonic(SonicHandler.HAS_CONSOLE_SONIC);
		boolean shouldEject = leftClick || player.isSneaking();

		if (hasSonic && shouldEject) {
			handler.spawnItem(SonicHandler.HAS_CONSOLE_SONIC);
			return true;
		}

		if (player.getMainHandStack().getItem() instanceof SonicItem linker) {
			if(!player.getMainHandStack().getOrCreateNbt().contains("tardis") || player.isSneaking()) {
				linker.link(player.getMainHandStack(), tardis);
				world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				tardis.getDesktop().getConsoles().forEach(consolis -> {
					Vec3d vec3d = Vec3d.ofBottomCenter(consolis.position()).add(0.0, 1.2f, 0.0);
					if (TardisUtil.getTardisDimension() instanceof ServerWorld) {
						world.spawnParticles(ParticleTypes.GLOW, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 12, 0.4F, 1F, 0.4F, 5.0F);
						world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 12, 0.4F, 1F, 0.4F, 5.0F);
					}
				});
			}
		}

		ItemStack stack = player.getMainHandStack();

		if (!(stack.getItem() instanceof SonicItem)) return false;

		handler.set(stack, true, SonicHandler.HAS_CONSOLE_SONIC);
		handler.markHasSonic(SonicHandler.HAS_CONSOLE_SONIC);
		player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

		FlightUtil.playSoundAtConsole(tardis, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 6f, 1);

		return true;
	}

	@Override
	public boolean shouldFailOnNoPower() {
		return false;
	}
}