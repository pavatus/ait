package mdteam.ait.core.helper;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.data.Corners;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import the.mdteam.ait.TardisDesktopSchema;

import javax.swing.text.html.parser.Entity;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Optional;

public class DesktopGenerator {

    private final TardisDesktopSchema schema;

    public DesktopGenerator(TardisDesktopSchema schema) {
        this.schema = schema;
    }

    public BlockPos place(ServerWorld level, Corners corners) {
        Optional<StructureTemplate> optional = this.schema.findTemplate();

        if (optional.isPresent()) {
            StructureTemplate template = optional.get();

            template.place(level, corners.getFirst(), corners.getFirst(), new StructurePlacementData(), level.getRandom(), 2);
            return TardisUtil.findBlockInTemplate(template, corners.getFirst(), Direction.SOUTH, AITBlocks.DOOR_BLOCK);
        }

        AITMod.LOGGER.error("Couldn't find interior structure {}!", this.schema.id());
        return null;
    }

    public static BlockPos centreTemplate(StructureTemplate template, BlockPos centrePos) {
        return new BlockPos(centrePos.getX() - (template.getSize().getX() / 2), centrePos.getY() - (template.getSize().getY() / 2), centrePos.getZ() - (template.getSize().getZ() / 2));
    }

    public static void clearArea(ServerWorld level, Corners interiorCorners) {
        for (BlockPos pos : BlockPos.iterate(interiorCorners.getFirst().add(0,-64,0), interiorCorners.getSecond().add(0,256,0))) {
            level.setBlockState(pos, Blocks.AIR.getDefaultState());
        }

        List<ItemEntity> items = (List<ItemEntity>) level.getEntitiesByType(EntityType.ITEM, EntityPredicates.EXCEPT_SPECTATOR);
        // fixme the check for whether the item is inside the interior bounds doesnt work, so it just deletes all items instead
        for (ItemEntity item : items) {
            item.kill();
        }
    }
}
