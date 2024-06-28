package loqor.ait.core.item;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.link.LinkableItem;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static loqor.ait.tardis.data.travel.TravelHandlerBase.State.LANDED;

public class RemoteItem extends LinkableItem {

	public RemoteItem(Settings settings) {
		super(settings, true);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		PlayerEntity player = context.getPlayer();
		ItemStack itemStack = context.getStack();

		if (player == null)
			return ActionResult.PASS;

		if (!(world instanceof ServerWorld serverWorld))
			return ActionResult.PASS;

		NbtCompound nbt = itemStack.getOrCreateNbt();

		// Move tardis to the clicked pos
		if (!nbt.contains("tardis"))
			return ActionResult.FAIL;

		ServerTardisManager.getInstance().getTardis(context.getWorld().getServer(), UUID.fromString(nbt.getString("tardis")), tardis -> {
			if (tardis == null)
				return;

            if (tardis.getFuel() <= 0)
                player.sendMessage(Text.translatable("message.ait.remoteitem.warning1"));

            if (tardis.isRefueling())
                player.sendMessage(Text.translatable("message.ait.remoteitem.warning2"));

            // Check if the Tardis is already present at this location before moving it there
            DirectedGlobalPos.Cached currentPosition = tardis.travel2().position();

			if (currentPosition.getPos().equals(pos))
				return;

			if (world != TardisUtil.getTardisDimension()) {
				world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS);

				BlockPos temp = pos.up();

				if (world.getBlockState(pos).isReplaceable())
					temp = pos;

				tardis.travel2().speed().set(tardis.travel2().maxSpeed());

				FlightUtil.travelTo(tardis, DirectedGlobalPos.Cached.create(serverWorld, temp,
						DirectionControl.getGeneralizedRotation(RotationPropertyHelper.fromYaw(player.getBodyYaw())))
				);
			} else {
				world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
				player.sendMessage(Text.translatable("message.ait.remoteitem.warning3"), true);
			}
        });

		return ActionResult.PASS;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		NbtCompound tag = stack.getOrCreateNbt();

		if (tag.contains("tardis")) {
			Tardis tardis = ClientTardisManager.getInstance().demandTardis(UUID.fromString(tag.getString("tardis")));

			if (tardis == null)
				return;

			if (tardis.travel2().getState() != LANDED)
				tooltip.add(Text.literal("→ " + tardis.travel2().getDurationAsPercentage() + "%").formatted(Formatting.GOLD));
		}
	}
}
