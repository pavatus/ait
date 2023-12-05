package mdteam.ait.core.helper;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.data.Corners;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import mdteam.ait.tardis.TardisDesktopSchema;

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

            template.place(level, BlockPos.ofFloored(corners.getBox().getCenter())/*centreTemplateAtCentre(centreTemplate(template), centreCorners(corners))*/, BlockPos.ofFloored(corners.getBox().getCenter()), new StructurePlacementData(), level.getRandom(), 2);
            return TardisUtil.findBlockInTemplate(template, BlockPos.ofFloored(corners.getBox().getCenter()), Direction.SOUTH, AITBlocks.DOOR_BLOCK);
        }

        AITMod.LOGGER.error("Couldn't find interior structure {}!", this.schema.id());
        return null;
    }

    public static BlockPos centreCorners(Corners corners) {
        int firstX = corners.getFirst().getX();
        int firstY = corners.getFirst().getY();
        int firstZ = corners.getFirst().getZ();
        int secondX = corners.getSecond().getX();
        int secondY = corners.getSecond().getY();
        int secondZ = corners.getSecond().getZ();
        return new BlockPos.Mutable((secondX + firstX) / 2, (secondY + firstY) / 2, (secondZ + firstZ) / 2);
    }

    public static BlockPos centreTemplateAtCentre(BlockPos template, BlockPos centrePos) {
        return new BlockPos(centrePos.getX() - template.getX(), centrePos.getY() - template.getY(), centrePos.getZ() - template.getZ());
    }

    public static BlockPos centreTemplate(StructureTemplate template) {
        int x = template.getSize().getX();
        int y = template.getSize().getY();
        int z = template.getSize().getZ();
        int xDiv = x / 2;
        int yDiv = y / 2; // remember, these 3 are the LENGTH of the vector in that direction divided by 2 to get the centre of the entire vectored template.
        int zDiv = z / 2;

        return new BlockPos(xDiv, yDiv, zDiv);
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
