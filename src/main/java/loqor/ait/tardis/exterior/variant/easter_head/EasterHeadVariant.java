package loqor.ait.tardis.exterior.variant.easter_head;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.DoorRegistry;
import loqor.ait.AITMod;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.exterior.category.EasterHeadCategory;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.variant.door.DoorSchema;
import loqor.ait.tardis.variant.door.EasterHeadDoorVariant;
import net.minecraft.util.Identifier;

// a useful class for creating easter_head variants as they all have the same filepath you know
public abstract class EasterHeadVariant extends ExteriorVariantSchema {
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/easter_head/easter_head_";

	protected EasterHeadVariant(String name) {
		super(name, EasterHeadCategory.REFERENCE, new Identifier(AITMod.MOD_ID, "exterior/easter_head/" + name));
	}

	@Override
	public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
		return new PulsatingAnimation(exterior);
	}

	@Override
	public DoorSchema door() {
		return DoorRegistry.REGISTRY.get(EasterHeadDoorVariant.REFERENCE);
	}

	@Override
	public boolean hasPortals() {
		return false;
	}
}
