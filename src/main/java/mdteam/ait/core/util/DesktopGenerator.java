package mdteam.ait.core.util;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import mdteam.ait.tardis.TardisDesktopSchema;

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
}
