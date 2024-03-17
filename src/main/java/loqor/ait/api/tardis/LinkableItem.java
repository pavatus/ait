package loqor.ait.api.tardis;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
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
	public LinkableItem(Settings settings) {
		super(settings);
	}

	public void link(ItemStack stack, Tardis tardis) {
		this.link(stack, tardis.getUuid());
	}

	public void link(ItemStack stack, UUID uuid) {
		NbtCompound nbt = stack.getOrCreateNbt();

		nbt.putString("tardis", uuid.toString());
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound nbt = stack.getOrCreateNbt();
		if (!nbt.contains("tardis")) return;
		if (nbt.contains("tardis")) {
			Tardis tardis = ClientTardisManager.getInstance().getTardis(UUID.fromString(nbt.getString("tardis")), true);
			if (tardis != null) {
				tooltip.add(Text.literal("TARDIS: ").formatted(Formatting.BLUE));
				tooltip.add(Text.literal("> " + tardis.getHandlers().getStats().getName()));
				tooltip.add(Text.literal("> " + tardis.getUuid().toString().substring(0, 8)).formatted(Formatting.DARK_GRAY));
			}
		}
		super.appendTooltip(stack, world, tooltip, context);
	}

	public static Tardis getTardis(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();

		if (!(nbt.contains("tardis"))) return null;

		UUID uuid = UUID.fromString(nbt.getString("tardis"));

		if (TardisUtil.isClient()) {
			return ClientTardisManager.getInstance().getLookup().get(uuid);
		}

		return ServerTardisManager.getInstance().getTardis(uuid);
	}
}
