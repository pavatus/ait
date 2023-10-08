package mdteam.ait.core.helper;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.desktop.DesktopSchema;
import mdteam.ait.core.helper.structures.DesktopStructure;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


import java.util.ArrayList;
import java.util.List;

public class DesktopGenerator {
    public static class InteriorGenerator extends DesktopGenerator {
        private ExteriorBlockEntity tardis;
        private DesktopSchema interior;
        public InteriorGenerator(ExteriorBlockEntity tardis, ServerWorld level, DesktopSchema interior) {
            super(level, interior);
            this.tardis = tardis;
            this.interior = interior;
        }

        @Override
        public void placeStructure(ServerWorld level, BlockPos pos, Direction direction) {
            super.placeStructure(level, pos, direction);
            //this.interior.setDoorPosition(findTargetBlockPosInTemplate(this.template,pos,direction, TARDISInteriorDoors.INTERIOR_DOOR_BLOCK_LIST).get(0));
            //this.tardis.getInterior().setSchema(this.interior);

            //this.tardis.setInterior(this.interior);
        }
    }
    private DesktopStructure structure;
    private String structureName;
    protected StructureTemplate template;

    public DesktopGenerator(ServerWorld level, DesktopStructure structure) {
        this.structure = structure;
        this.structureName = structure.getStructureName();
        this.template = level.getStructureTemplateManager().getTemplate(this.structure.getLocation()).get();
    }

    public void placeStructure(ServerWorld level, BlockPos pos, Direction direction) {
        this.template.place(level,pos,pos,new StructurePlacementData(),level.getRandom(),2); // .setRotation(this.directionToRotation(direction))
    }

    protected BlockPos findTargetBlockPosInTemplate(StructureTemplate template, BlockPos pos, Direction direction, Block targetBlock) {
        List<StructureTemplate.StructureBlockInfo> list = template.getInfosForBlock(pos, new StructurePlacementData().setRotation(directionToRotation(direction)), targetBlock);

        if (list.size() == 0) {
            return new BlockPos(0,0,0);
        }

        return list.get(0).pos();
    }

    /**
     * Recommended usage is for finding the first door pos etc
     *
     * @param targetBlocks Blocks to search for
     * @return A list of block positions for the FIRST instance of each block
     */
    protected List<BlockPos> findTargetBlockPosInTemplate(StructureTemplate template, BlockPos pos, Direction direction, List<Block> targetBlocks) {
        List<BlockPos> posList = new ArrayList<>();
        for (Block block : targetBlocks) {
            BlockPos foundPos = this.findTargetBlockPosInTemplate(template,pos,direction,block);
            if (foundPos != null) {
                posList.add(foundPos);
            }
        }
        return posList;
    }

    private BlockRotation directionToRotation(Direction direction) {
        return switch (direction) {
            default -> BlockRotation.NONE;
            case NORTH -> BlockRotation.CLOCKWISE_180;
            case SOUTH -> BlockRotation.NONE;
            case EAST -> BlockRotation.COUNTERCLOCKWISE_90;
            case WEST -> BlockRotation.CLOCKWISE_90;
        };
    }

    public static Direction rotationToDirection(BlockRotation rotation) {
        return switch(rotation) {
            case NONE -> Direction.NORTH;
            case CLOCKWISE_180 -> Direction.SOUTH;
            case CLOCKWISE_90 -> Direction.WEST;
            case COUNTERCLOCKWISE_90 -> Direction.EAST;
        };
    }
}
