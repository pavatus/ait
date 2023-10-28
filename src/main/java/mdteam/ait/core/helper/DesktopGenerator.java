package mdteam.ait.core.helper;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.core.AITBlocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class DesktopGenerator {

    private final IDesktopSchema schema;

    public DesktopGenerator(IDesktopSchema schema) {
        this.schema = schema;
    }

    public BlockPos place(ServerWorld level, BlockPos pos) {
        Optional<StructureTemplate> optional = this.schema.findTemplate();

        if (optional.isPresent()) {
            StructureTemplate template = optional.get();

            template.place(level, pos, pos, new StructurePlacementData(), level.getRandom(), 2);
            return TardisUtil.findBlockInTemplate(template, pos, Direction.SOUTH, AITBlocks.DOOR_BLOCK);
        }

        AITMod.LOGGER.error("Couldn't find interior structure {}!", this.schema.id());
        return null;
    }
}
