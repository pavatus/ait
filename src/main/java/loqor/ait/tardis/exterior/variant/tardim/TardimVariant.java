package loqor.ait.tardis.exterior.variant.tardim;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.door.TardimDoorVariant;
import loqor.ait.tardis.exterior.category.TardimCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class TardimVariant extends ExteriorVariantSchema {
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/tardim/tardim_";

	protected TardimVariant(String name) {
		super(TardimCategory.REFERENCE, new Identifier(AITMod.MOD_ID, "exterior/tardim/" + name), new Loyalty(Loyalty.Type.OWNER));
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
	public Vec3d adjustPortalPos(Vec3d pos, byte direction) {
		return switch (direction) {
			case 0 -> pos.add(0, 0, -0.25f); // NORTH
			case 1, 2, 3 -> pos; // NORTH EAST
			case 4 -> pos.add(0.25f, 0, 0); // EAST
			case 5, 6, 7 -> pos; // SOUTH EAST
			case 8 -> pos.add(0, 0, 0.25f); // SOUTH
			case 9, 10, 11 -> pos; // SOUTH WEST
			case 12 -> pos.add(-0.25f, 0, 0); // WEST
			case 13, 14, 15 -> pos; // NORTH WEST
			default -> pos;
		};
	}
}