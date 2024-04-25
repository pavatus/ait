package loqor.ait.tardis.exterior.variant.booth;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.DoorRegistry;
import loqor.ait.tardis.exterior.category.BoothCategory;
import loqor.ait.AITMod;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.variant.door.BoothDoorVariant;
import loqor.ait.tardis.variant.door.DoorSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class BoothVariant extends ExteriorVariantSchema {
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/booth/booth_";

	protected BoothVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
		super(name, BoothCategory.REFERENCE, new Identifier(modId, "exterior/booth/" + name));
	}

	protected BoothVariant(String name) {
		this(name, AITMod.MOD_ID);
	}

	@Override
	public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
		return new PulsatingAnimation(exterior);
	}

	@Override
	public DoorSchema door() {
		return DoorRegistry.REGISTRY.get(BoothDoorVariant.REFERENCE);
	}

	@Override
	public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
		return switch (direction) {
			case DOWN, UP -> pos;
			case NORTH -> pos.add(0, 0.25, -0.48f);
			case SOUTH -> pos.add(0, 0.25, 0.48f);
			case WEST -> pos.add(-0.48f, 0.25, 0);
			case EAST -> pos.add(0.48f, 0.25, 0);
		};
	}

	@Override
	public double portalWidth() {
		return super.portalWidth();
	}

	@Override
	public double portalHeight() {
		return 2.25d;
	}
}