package loqor.ait.tardis.util.desktop.structures;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;

import loqor.ait.api.Structure;
import loqor.ait.core.data.Corners;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.util.TardisUtil;

public class DesktopGenerator {

    private static final StructurePlacementData SETTINGS = new StructurePlacementData();

    private final TardisDesktopSchema schema;

    public DesktopGenerator(TardisDesktopSchema schema) {
        this.schema = schema;
    }

    public void place(Tardis tardis, ServerWorld level, Corners corners) {
        Optional<StructureTemplate> optional = this.schema.findTemplate();

        if (optional.isEmpty())
            return;

        StructureTemplate template = optional.get();

        if (template instanceof Structure structure)
            structure.ait$setTardis(tardis);

        template.place(level, BlockPos.ofFloored(corners.getBox().getCenter()),
                BlockPos.ofFloored(corners.getBox().getCenter()), SETTINGS, level.getRandom(), Block.FORCE_STATE);
    }

    public static void clearArea(ServerWorld level, Corners corners) {
        for (BlockPos pos : BlockPos.iterate(corners.getFirst().add(0, -64, 0), corners.getSecond().add(0, 256, 0))) {
            level.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.FORCE_STATE);
        }

        TardisUtil.getEntitiesInBox(ItemFrameEntity.class, level, corners.getBox(), frame -> true)
                .forEach(frame -> frame.remove(Entity.RemovalReason.DISCARDED));
    }
}
