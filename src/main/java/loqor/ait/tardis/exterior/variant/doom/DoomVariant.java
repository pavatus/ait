package loqor.ait.tardis.exterior.variant.doom;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.DoorRegistry;
import loqor.ait.tardis.exterior.category.DoomCategory;
import loqor.ait.AITMod;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.variant.door.DoomDoorVariant;
import loqor.ait.tardis.variant.door.DoorSchema;
import net.minecraft.util.Identifier;

public class DoomVariant extends ExteriorVariantSchema {
	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/doom");

	public DoomVariant() {
		super(DoomCategory.REFERENCE, REFERENCE);
	}

	@Override
	public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
		return new PulsatingAnimation(exterior);
	}

	@Override
	public DoorSchema door() {
		return DoorRegistry.REGISTRY.get(DoomDoorVariant.REFERENCE);
	}

	@Override
	public boolean hasPortals() {
		return false;
	}
}
