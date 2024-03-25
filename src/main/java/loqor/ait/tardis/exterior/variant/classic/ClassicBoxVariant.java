package loqor.ait.tardis.exterior.variant.classic;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.DoorRegistry;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.exterior.category.ClassicCategory;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.variant.door.ClassicDoorVariant;
import loqor.ait.tardis.variant.door.DoorSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClassicBoxVariant extends ExteriorVariantSchema {
	private final String name;
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/classic/classic_";

	protected ClassicBoxVariant(String name, String modId) {
		super(ClassicCategory.REFERENCE, new Identifier(modId, "exterior/classic/" + name));

		this.name = name;
	}

	protected ClassicBoxVariant(String name) {
		this(name, AITMod.MOD_ID);
	}

	@Override
	public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
		return new PulsatingAnimation(exterior);
	}

	@Override
	public DoorSchema door() {
		return DoorRegistry.REGISTRY.get(ClassicDoorVariant.REFERENCE);
	}

	@Override
	public boolean hasPortals() {
		return true;
	}

	@Override
	public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
		return switch (direction) {
			case DOWN, UP -> pos;
			case NORTH -> pos.add(0, 0.15, -0.599);
			case SOUTH -> pos.add(0, 0.15, 0.599);
			case WEST -> pos.add(-0.599, 0.15, 0);
			case EAST -> pos.add(0.599, 0.15, 0);
		};
	}

	@Override
	public double portalHeight() {
		return 2.2d;
	}

	@Override
	public double portalWidth() {
		return 1.2d;
	}
}