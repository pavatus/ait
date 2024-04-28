package loqor.ait.tardis.exterior.variant.renegade;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.AITMod;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.exterior.category.RenegadeCategory;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.tardis.door.RenegadeDoorVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class RenegadeVariant extends ExteriorVariantSchema {
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/renegade/renegade_";

	protected RenegadeVariant(String name) {
		super(name, RenegadeCategory.REFERENCE, new Identifier(AITMod.MOD_ID, "exterior/renegade/" + name), new Loyalty(Loyalty.Type.PILOT));
	}

	@Override
	public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
		return new PulsatingAnimation(exterior);
	}

	@Override
	public DoorSchema door() {
		return DoorRegistry.REGISTRY.get(RenegadeDoorVariant.REFERENCE);
	}

	@Override
	public boolean hasPortals() {
		return true;
	}

	@Override
	public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
		return switch (direction) {
			case DOWN, UP -> pos;
			case NORTH -> pos.add(0, 0.4875, -0.4);
			case SOUTH -> pos.add(0, 0.4875, 0.4);
			case WEST -> pos.add(-0.4, 0.4875, 0);
			case EAST -> pos.add(0.4, 0.4875, 0);
		};
	}

	@Override
	public double portalHeight() {
		return 2.7d;
	}

	@Override
	public double portalWidth() {
		return 1d;
	}
}