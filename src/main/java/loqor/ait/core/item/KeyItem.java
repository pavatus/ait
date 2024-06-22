package loqor.ait.core.item;

import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.link.LinkableItem;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
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
			Tardis found = KeyItem.getTardis(player.getWorld(), stack);

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

		Tardis tardis = KeyItem.getTardis(world, stack);

		if (tardis == null)
			return;

		KeyItem.hailMary(tardis, stack, player);
	}

	@Override
	public void onItemEntityDestroyed(ItemEntity entity) {
		Entity owner = entity.getOwner();

		if (!(owner instanceof ServerPlayerEntity player))
			return;

		Tardis tardis = KeyItem.getTardis(entity.getWorld(), entity.getStack());
		tardis.loyalty().subLevel(player, 5);

		FlightUtil.playSoundAtEveryConsole(tardis.getDesktop(), AITSounds.CLOISTER);
	}

	private static void hailMary(Tardis tardis, ItemStack stack, PlayerEntity player) {
		if (player.getItemCooldownManager().isCoolingDown(stack.getItem()))
			return;

		if (!PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.HAIL_MARY))
			return;

		KeyItem keyType = (KeyItem) stack.getItem().asItem();

		if (tardis.flight().handbrake().get())
			return;

		if (!keyType.hasProtocol(Protocols.HAIL))
			return;

		if (player.getHealth() > 4 || player.getWorld() == TardisUtil.getTardisDimension())
			return;

		tardis.travel().setDestination(TardisUtil.createFromPlayer(player), true);

		if (tardis.travel().getState() == TardisTravel.State.LANDED) {
			tardis.travel().dematerialise(true);
		} else if (tardis.travel().getState() == TardisTravel.State.FLIGHT) {
			tardis.travel().materialise();
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

		if (world.isClient())
			return ActionResult.SUCCESS;

		if (player == null || !player.isSneaking())
			return ActionResult.PASS;

		if (!(world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock))
			return ActionResult.PASS;

		if (consoleBlock.tardis().isEmpty())
			return ActionResult.FAIL;

		Tardis tardis = consoleBlock.tardis().get();

		if (tardis.loyalty().get(player).isOf(Loyalty.Type.COMPANION)) {
			this.link(stack, consoleBlock.tardis().get());
			return ActionResult.SUCCESS;
		}

		player.sendMessage(Text.translatable("message.ait.tardis.trust_issue", true));
		return ActionResult.FAIL;
	}
}
