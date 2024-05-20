package loqor.ait.core.item;

import loqor.ait.tardis.link.LinkableItem;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeyItem extends LinkableItem {
	private final List<Protocols> protocols;

	public KeyItem(Settings settings, Protocols... abs) {
		super(settings.maxCount(1), true);
		this.protocols = List.of(abs);
	}

	public enum Protocols {
		SNAP,
		HAIL,
		PERCEPTION
	}

	public boolean hasProtocol(Protocols var) {
		return this.protocols.contains(var);
	}

	public static boolean isKeyInInventory(PlayerEntity player) {
		return getFirstKeyStackInInventory(player) != null;
	}

	public static ItemStack getFirstKeyStackInInventory(PlayerEntity player) {
		for (ItemStack itemStack : player.getInventory().main) {
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof KeyItem) {
				return itemStack;
			}
		}

		return null;
	}

	public static Collection<ItemStack> getKeysInInventory(PlayerEntity player) {
		List<ItemStack> items = new ArrayList<>();

		for (ItemStack stack : player.getInventory().main) {
			if (stack != null && stack.getItem() instanceof KeyItem) {
				items.add(stack);
			}
		}

		return items;
	}

	public static boolean hasMatchingKeyInInventory(PlayerEntity player, Tardis tardis) {
		Collection<ItemStack> keys = getKeysInInventory(player);

		for (ItemStack stack : keys) {
			Tardis found = KeyItem.getTardis(stack, TardisManager.getInstance(tardis));

			if (found == null)
				continue;

			if (found == tardis)
				return true;
		}

		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);

		if (!(entity instanceof ServerPlayerEntity player))
			return;

		Tardis tardis = KeyItem.getTardis(stack, ServerTardisManager.getInstance());

		if (tardis == null)
			return;

		KeyItem.hailMary(tardis, stack, player);
	}

	private static void hailMary(Tardis tardis, ItemStack stack, PlayerEntity player) {
		if (player.getItemCooldownManager().isCoolingDown(stack.getItem()))
			return;

		if (!PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.HAIL_MARY))
			return;

		KeyItem keyType = (KeyItem) stack.getItem().asItem();

		if (PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.HANDBRAKE))
			return;

		if (!keyType.hasProtocol(Protocols.HAIL))
			return;

		if (player.getHealth() > 4 || player.getWorld() == TardisUtil.getTardisDimension())
			return;

		tardis.getTravel().setDestination(TardisUtil.createFromPlayer(player), true);

		if (tardis.getTravel().getState() == TardisTravel.State.LANDED) {
			tardis.getTravel().dematerialise(true);
		} else if (tardis.getTravel().getState() == TardisTravel.State.FLIGHT) {
			tardis.getTravel().materialise();
		}

		player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 80, 3));
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 6 * 20, 3));

		player.getItemCooldownManager().set(stack.getItem(), 60 * 20);

		PropertiesHandler.set(tardis.properties(), PropertiesHandler.HAIL_MARY, false);
		PropertiesHandler.set(tardis.properties(), PropertiesHandler.PREVIOUSLY_LOCKED, false);

		player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 5f, 0.1f); // like a sound to show its been called
		player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.BLOCKS, 5f, 0.1f);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getStack();

		if (world.isClient() || player == null)
			return ActionResult.PASS;

		if (!player.isSneaking())
			return ActionResult.PASS;

		if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock) {
			if (consoleBlock.findTardis().isEmpty())
				return ActionResult.FAIL;

			this.link(stack, consoleBlock.findTardis().get());
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}
}
