package loqor.ait.tardis.link;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class LinkableItem extends Item {

	private final boolean showTooltip;

	public LinkableItem(Settings settings, boolean showTooltip) {
		super(settings);
		this.showTooltip = showTooltip;
	}

	public void link(ItemStack stack, Tardis tardis) {
		this.link(stack, tardis.getUuid());
	}

	public void link(ItemStack stack, UUID uuid) {
		NbtCompound nbt = stack.getOrCreateNbt();

		// FIXME why the fuck is it a string?
		nbt.putString("tardis", uuid.toString());
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		this.handleTooltip(stack, tooltip);
		super.appendTooltip(stack, world, tooltip, context);
	}

	private void handleTooltip(ItemStack stack, List<Text> tooltip) {
		if (!showTooltip)
			return;

		NbtCompound nbt = stack.getOrCreateNbt();

		if (!nbt.contains("tardis"))
			return;

		if (!Screen.hasShiftDown()) {
			tooltip.add(Text.translatable("tooltip.ait.remoteitem.holdformoreinfo").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
			return;
		}

		ClientTardisManager.getInstance().getTardis(UUID.fromString(nbt.getString("tardis")), tardis -> {
			if (tardis != null) {
				tooltip.add(Text.literal("TARDIS: ").formatted(Formatting.BLUE));
				tooltip.add(Text.literal("> " + tardis.getHandlers().getStats().getName()));
				tooltip.add(Text.literal("> " + tardis.getUuid().toString().substring(0, 8)).formatted(Formatting.DARK_GRAY));
			}
		});
	}

	public static boolean isOf(ItemStack stack, Tardis tardis) {
		return getTardis(stack, TardisManager.getInstance(tardis)) == tardis;
	}

	public static Tardis getTardis(ItemStack stack, TardisManager<?> manager) {
		NbtCompound nbt = stack.getOrCreateNbt();

		if (!(nbt.contains("tardis")))
			return null;

		UUID uuid = UUID.fromString(nbt.getString("tardis"));
		return manager.demandTardis(uuid);
	}
}
