package loqor.ait.core.blockentities;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.link.v2.interior.InteriorLinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MonitorBlockEntity extends InteriorLinkableBlockEntity {

	public MonitorBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.MONITOR_BLOCK_ENTITY_TYPE, pos, state);
	}

	public void useOn(World world, boolean sneaking, PlayerEntity player) {
		if (!(player instanceof ServerPlayerEntity serverPlayer))
			return;

		if (this.tardis().isEmpty())
			return;

		Tardis tardis = this.tardis().get();
		boolean security = PropertiesHandler.getBool(tardis.properties(), SecurityControl.SECURITY_KEY);

		if (security && !SecurityControl.hasMatchingKey(serverPlayer, tardis))
			return;

		player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
		AITMod.openScreen(serverPlayer, 0, tardis.getUuid()); // we can cast because we know its on server :p
	}
}
