package loqor.ait.tardis.exterior.variant.booth;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.door.BoothDoorVariant;
import loqor.ait.tardis.exterior.category.BoothCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class BoothVariant extends ExteriorVariantSchema {
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/booth/booth_";

	protected BoothVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
		super(BoothCategory.REFERENCE, new Identifier(modId, "exterior/booth/" + name), new Loyalty(Loyalty.Type.COMPANION));
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
	public Vec3d adjustPortalPos(Vec3d pos, byte direction) {
		return switch (direction) {
			case 0 -> pos.add(0, 0.25, -0.48f); // NORTH
			case 1, 2, 3 -> pos; // NORTH EAST
			case 4 -> pos.add(0.48f, 0.25, 0); // EAST
			case 5, 6, 7 -> pos; // SOUTH EAST
			case 8 -> pos.add(0, 0.25, 0.48f); // SOUTH
			case 9, 10, 11 -> pos; // SOUTH WEST
			case 12 -> pos.add(-0.48f, 0.25, 0); // WEST
			case 13, 14, 15 -> pos; // NORTH WEST
			default -> pos;
		};
	}

	@Override
	public boolean hasPortals() {
		return true;
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