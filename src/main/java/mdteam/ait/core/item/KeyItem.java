package mdteam.ait.core.item;

import mdteam.ait.api.tardis.LinkableItem;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KeyItem extends LinkableItem {
	private final List<Protocols> protocols;


	public KeyItem(Settings settings, Protocols... abs) {
		super(settings.maxCount(1));
		protocols = new ArrayList<>(List.of(abs));
	}

	public enum Protocols {
		SNAP,
		HAIL,
		PERCEPTION
	}

	public enum Permissions {
		ATTUNE, // Permission to attune sonic or key to a tardis
		CONSOLE, // Permission to use console
		TRAVEL, // Permission to travel inside the tardis
		CONTAINER, // Permission to open containers
		PLACE, // Permission to place blocks
		BREAK, // Permission to break blocks
		INTERACT, // Permission to interact with blocks
		CLOAK, // Permission to see through cloak
	}

	public boolean hasProtocol(Protocols var) {
		return this.protocols.contains(var);
	}

	public static boolean isKeyInInventory(PlayerEntity player) {
		return getFirstKeyStackInInventory(player) != null;
	}

	public static ItemStack getFirstKeyStackInInventory(PlayerEntity player) {
		// from playerinventory

		for (ItemStack itemStack : player.getInventory().main) {
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof KeyItem key) {
				return itemStack;
			}
		}
		return null;
	}

	public static ItemStack[] getKeysInInventory(PlayerEntity player) {
		List<ItemStack> items = new ArrayList<>();
		for (ItemStack itemStack : player.getInventory().main) {
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof KeyItem key) {
				items.add(itemStack);
			}
		}
		return items.toArray(new ItemStack[0]);
	}

	public static boolean hasMatchingKeyInInventory(PlayerEntity player, Tardis tardis) {
		if (player.hasPermissionLevel(2)) return true; // extra check
		ItemStack[] keys = getKeysInInventory(player);
		for (ItemStack stack : keys) {
			if (stack == null) continue;
			Tardis found = KeyItem.getTardis(stack);
			if (found == null) continue;
			if (found == tardis) return true;
		}
		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);

		if (!(entity instanceof ServerPlayerEntity player)) return;

		Tardis tardis = getTardis(stack);
		if (tardis == null) return;

		hailMary(tardis, stack, player);
	}

	private void hailMary(Tardis tardis, ItemStack stack, PlayerEntity player) {
		if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) return;
		if (!PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY)) return;

		KeyItem keyType = (KeyItem) stack.getItem().asItem();
		boolean handbrake = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE);

		if (keyType.hasProtocol(Protocols.HAIL) && !handbrake && player.getHealth() <= 4 && player.getWorld() != TardisUtil.getTardisDimension()) {
			tardis.getTravel().setDestination(TardisUtil.createFromPlayer(player), true);

			if (tardis.getTravel().getState() == TardisTravel.State.LANDED) {
				tardis.getTravel().dematerialise(true);
			} else if (tardis.getTravel().getState() == TardisTravel.State.FLIGHT) {
				tardis.getTravel().materialise();
			}

			player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 80, 3));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 6 * 20, 3));

			player.getItemCooldownManager().set(stack.getItem(), 60 * 20);

			PropertiesHandler.set(tardis.getHandlers().getProperties(), PropertiesHandler.HAIL_MARY, false);
			PropertiesHandler.set(tardis.getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED, false);

			player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 5f, 0.1f); // like a sound to show its been called
			player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.BLOCKS, 5f, 0.1f);
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		PlayerEntity player = context.getPlayer();
		ItemStack itemStack = context.getStack();

		if (world.isClient() || player == null)
			return ActionResult.PASS;

		if (player.isSneaking()) {
			if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock) {
				if (consoleBlock.findTardis().isEmpty())
					return ActionResult.FAIL;

				this.link(itemStack, consoleBlock.findTardis().get());
				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (!Screen.hasShiftDown()) {
			tooltip.add(Text.translatable("tooltip.ait.remoteitem.holdformoreinfo").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
			return;
		}
		super.appendTooltip(stack, world, tooltip, context);
	}
}
