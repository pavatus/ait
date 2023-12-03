package mdteam.ait.core.helper;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.data.Corners;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import the.mdteam.ait.TardisDesktopSchema;

import java.util.Optional;

public class DesktopGenerator {

    private final TardisDesktopSchema schema;

    public DesktopGenerator(TardisDesktopSchema schema) {
        this.schema = schema;
    }

    public BlockPos place(ServerWorld level, BlockPos pos) {
        BlockPos bpos = new BlockPos(pos.getX(), pos.getY() - 64, pos.getZ());
        Optional<StructureTemplate> optional = this.schema.findTemplate();

        if (optional.isPresent()) {
            StructureTemplate template = optional.get();

            template.place(level, bpos, pos, new StructurePlacementData(), level.getRandom(), 2);
            return TardisUtil.findBlockInTemplate(template, bpos, Direction.SOUTH, AITBlocks.DOOR_BLOCK);
        }

        AITMod.LOGGER.error("Couldn't find interior structure {}!", this.schema.id());
        return null;
    }

    public static void clearArea(ServerWorld level, Corners interiorCorners) {
        // fixme some interiors go outside the corners (cave interior) so it dont get cleared properly, what should we do? @loqor
        for (BlockPos pos : BlockPos.iterate(interiorCorners.getFirst(), interiorCorners.getSecond().add(0,128,0))) {
            level.removeBlock(pos, false);
        }
        //System.out.println(level);
    }
}
