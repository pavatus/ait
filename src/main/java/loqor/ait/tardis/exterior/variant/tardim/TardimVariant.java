package loqor.ait.tardis.exterior.variant.tardim;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.DoorRegistry;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.exterior.category.TardimCategory;
import loqor.ait.AITMod;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.variant.door.DoorSchema;
import loqor.ait.tardis.variant.door.TardimDoorVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class TardimVariant extends ExteriorVariantSchema {
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/tardim/tardim_";

	protected TardimVariant(String name) {
		super(name, TardimCategory.REFERENCE, new Identifier(AITMod.MOD_ID, "exterior/tardim/" + name), new Loyalty(Loyalty.Type.OWNER));
	}

	@Override
	public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
		return new PulsatingAnimation(exterior);
	}

	@Override
	public DoorSchema door() {
		return DoorRegistry.REGISTRY.get(TardimDoorVariant.REFERENCE);
	}

	@Override
	public boolean hasPortals() {
		return true;
	}

	@Override
	public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
		return switch (direction) {
			case DOWN, UP -> pos;
			case NORTH -> pos.add(0, 0, -0.25f);
			case SOUTH -> pos.add(0, 0, 0.25f);
			case WEST -> pos.add(-0.25f, 0, 0);
			case EAST -> pos.add(0.25f, 0, 0);
		};
	}
}