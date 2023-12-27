package mdteam.ait.tardis.variant.exterior;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.client.models.exteriors.CoobExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.core.AITDoors;
import mdteam.ait.core.AITExteriors;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.exterior.CubeExterior;
import mdteam.ait.tardis.variant.door.CapsuleDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class RedCoobVariant extends ExteriorVariantSchema {
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/cube";

    public static final VoxelShape CUBE_NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 2.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(0, 0, -3.5, 16,1, 16));
    public static final VoxelShape CUBE_EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 14.0, 16.0, 16.0),
            Block.createCuboidShape(0, 0, 0, 19.5,1, 16));
    public static final VoxelShape CUBE_SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.00, 14.0),
            Block.createCuboidShape(0, 0, 0, 16,1, 19.5));
    public static final VoxelShape CUBE_WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(2.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(-3.5, 0, 0, 16,1, 16));

    public RedCoobVariant() {
        super(CubeExterior.REFERENCE, new Identifier(AITMod.MOD_ID, "cube_red"));
    }


    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + ".png");
    }

    @Override
    public Identifier emission() {
        return null;
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
    public ExteriorModel model() {
        return new CoobExteriorModel(CoobExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return AITDoors.get(CapsuleDoorVariant.REFERENCE);
    }
}
