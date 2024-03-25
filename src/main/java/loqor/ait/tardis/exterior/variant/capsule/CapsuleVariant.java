package loqor.ait.tardis.exterior.variant.capsule;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.DoorRegistry;
import loqor.ait.tardis.exterior.category.CapsuleCategory;
import loqor.ait.AITMod;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.variant.door.CapsuleDoorVariant;
import loqor.ait.tardis.variant.door.DoorSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class CapsuleVariant extends ExteriorVariantSchema {
	private final String name;
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/capsule/capsule_";

	protected CapsuleVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
		super(CapsuleCategory.REFERENCE, new Identifier(modId, "exterior/capsule/" + name));

		this.name = name;
	}

	protected CapsuleVariant(String name) {
		this(name, AITMod.MOD_ID);
	}

	@Override
	public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
		return new PulsatingAnimation(exterior);
	}

	@Override
	public DoorSchema door() {
		return DoorRegistry.REGISTRY.get(CapsuleDoorVariant.REFERENCE);
	}

	@Override
	public boolean hasPortals() {
		return true;
	}

	@Override
	public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
		return switch (direction) {
			case DOWN, UP -> pos;
			case NORTH -> pos.add(0, 0.1, -0.5);
			case SOUTH -> pos.add(0, 0.1, 0.5);
			case WEST -> pos.add(-0.5, 0.1, 0);
			case EAST -> pos.add(0.5, 0.1, 0);
		};
	}

	@Override
	public double portalHeight() {
		return 2.1d;
	}

	@Override
	public double portalWidth() {
		return 0.75d;
	}
}