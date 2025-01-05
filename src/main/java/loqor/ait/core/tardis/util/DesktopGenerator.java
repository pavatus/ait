package loqor.ait.core.tardis.util;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;

import loqor.ait.AITMod;
import loqor.ait.api.TardisStructure;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.data.Corners;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;

public class DesktopGenerator {

    private static final StructurePlacementData SETTINGS = new StructurePlacementData().setUpdateNeighbors(false);

    private final TardisDesktopSchema schema;

    public DesktopGenerator(TardisDesktopSchema schema) {
        this.schema = schema;
    }

    public boolean place(Tardis tardis, ServerWorld level, Corners corners) {
        Optional<StructureTemplate> optional = this.schema.findTemplate();

        if (optional.isEmpty()) {
            AITMod.LOGGER.error("Failed to find template for {}", this.schema.id());
            return false;
        }

        StructureTemplate template = optional.get();

        if (template instanceof TardisStructure structure)
            structure.ait$setTardis(tardis);

        return template.place(level, BlockPos.ofFloored(corners.getBox().getCenter()),
                BlockPos.ofFloored(corners.getBox().getCenter()), SETTINGS, level.getRandom(), Block.FORCE_STATE);
    }
}
