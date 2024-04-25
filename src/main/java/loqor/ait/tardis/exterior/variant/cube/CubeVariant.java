package loqor.ait.tardis.exterior.variant.cube;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.DoorRegistry;
import loqor.ait.AITMod;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.exterior.category.EasterHeadCategory;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.variant.door.ClassicDoorVariant;
import loqor.ait.tardis.variant.door.DoorSchema;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public abstract class CubeVariant extends ExteriorVariantSchema {
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/cube/";
	public static final VoxelShape CUBE_NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 2.0, 16.0, 16.0, 16.0),
			Block.createCuboidShape(0, 0, -3.5, 16, 1, 16));
	public static final VoxelShape CUBE_EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 14.0, 16.0, 16.0),
			Block.createCuboidShape(0, 0, 0, 19.5, 1, 16));
	public static final VoxelShape CUBE_SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.00, 14.0),
			Block.createCuboidShape(0, 0, 0, 16, 1, 19.5));
	public static final VoxelShape CUBE_WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(2.0, 0.0, 0.0, 16.0, 16.0, 16.0),
			Block.createCuboidShape(-3.5, 0, 0, 16, 1, 16));

	protected CubeVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
		super(name, EasterHeadCategory.REFERENCE, new Identifier(modId, "exterior/cube/" + name));
	}

	protected CubeVariant(String name) {
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
	public VoxelShape bounding(Direction dir) {
		return switch (dir) {
			case NORTH -> CUBE_NORTH_SHAPE;
			case EAST -> CUBE_EAST_SHAPE;
			case SOUTH -> CUBE_SOUTH_SHAPE;
			case WEST -> CUBE_WEST_SHAPE;
			default ->
					throw new RuntimeException("Invalid facing direction in " + this + ", // duzo put a funny coment here : )");
		};
	}

	@Override
	public boolean hasPortals() {
		return false;
	}
}
