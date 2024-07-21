package loqor.ait.tardis.exterior.variant.bookshelf;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.door.BookshelfDoorVariant;
import loqor.ait.tardis.exterior.category.BookshelfCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class BookshelfVariant extends ExteriorVariantSchema {
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/bookshelf/bookshelf_";

	protected BookshelfVariant(String name) { // idk why i added the modid bit i dont use it later lol
		super(BookshelfCategory.REFERENCE, new Identifier(AITMod.MOD_ID, "exterior/bookshelf/" + name), new Loyalty(Loyalty.Type.COMPANION));
	}

	@Override
	public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
		return new PulsatingAnimation(exterior);
	}

	@Override
	public DoorSchema door() {
		return DoorRegistry.REGISTRY.get(BookshelfDoorVariant.REFERENCE);
	}

	@Override
	public boolean hasPortals() {
		return true;
	}

	@Override
	public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
		return switch (direction) {
			case DOWN, UP -> pos;
			case NORTH -> pos.add(0, 0.207, -0.628);
			case SOUTH -> pos.add(0, 0.207, 0.628);
			case WEST -> pos.add(-0.628, 0.207, 0);
			case EAST -> pos.add(0.628, 0.207, 0);
		};
	}

	@Override
	public double portalHeight() {
		return 2.3d;
	}

	@Override
	public double portalWidth() {
		return 1.145d;
	}
}