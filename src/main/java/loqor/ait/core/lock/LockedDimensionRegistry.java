package loqor.ait.core.lock;

import loqor.ait.AITMod;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.config.AITConfig;
import loqor.ait.registry.datapack.SimpleDatapackRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.ArrayList;
import java.util.List;

public class LockedDimensionRegistry extends SimpleDatapackRegistry<LockedDimension> {
	private static final LockedDimensionRegistry instance = new LockedDimensionRegistry();

	// it can go here idc
	static {
		TardisEvents.MAT.register((tardis -> {
			if (!AITMod.AIT_CONFIG.LOCK_DIMENSIONS()) return TardisEvents.Interaction.PASS;

			LockedDimension dim = getInstance().get(tardis.travel().destination().getWorld());
			boolean success = dim == null || tardis.isUnlocked(dim);

			if (!success) return TardisEvents.Interaction.FAIL;

			return TardisEvents.Interaction.PASS;
		}));
	}

	public LockedDimensionRegistry() {
		super(LockedDimension::fromInputStream, LockedDimension.CODEC, "locked_dimension", true);
	}

	public static LockedDimension NETHER;
	@Override
	protected void defaults() {
		NETHER = register(new LockedDimension(DimensionTypes.THE_NETHER_ID, new ItemStack(Items.NETHERITE_SCRAP)));
		// all others should be in datapack
	}

	@Override
	public LockedDimension fallback() {
		return NETHER;
	}

	public LockedDimension get(World world) {
		return this.get(world.getRegistryKey().getValue());
	}
	public List<LockedDimension> forStack(ItemStack stack) {
		// ow :(
		List<LockedDimension> copy = new ArrayList<>(this.REGISTRY.values());

		copy.removeIf((dim) -> !(dim.stack().getItem().equals(stack.getItem())));

		return copy;
	}

	public static LockedDimensionRegistry getInstance() {
		return instance;
	}
}
