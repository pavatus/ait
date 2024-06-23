package loqor.ait.tardis.util.desktop.structures;

import loqor.ait.api.Structure;
import loqor.ait.core.data.Corners;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class DesktopGenerator {

	private static final StructurePlacementData SETTINGS = new StructurePlacementData();

	private final TardisDesktopSchema schema;

	public DesktopGenerator(TardisDesktopSchema schema) {
		this.schema = schema;
	}

	public void place(Tardis tardis, ServerWorld level, Corners corners) {
		Optional <StructureTemplate> optional = this.schema.findTemplate();

		if (optional.isEmpty())
			return;

		StructureTemplate template = optional.get();

		if (template instanceof Structure structure)
			structure.ait$setTardis(tardis);

		template.place(level, BlockPos.ofFloored(corners.getBox().getCenter()),
				BlockPos.ofFloored(corners.getBox().getCenter()), SETTINGS,
				level.getRandom(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD
		);
	}

	public static void clearArea(ServerWorld level, Corners interiorCorners) {
		// @TODO: Just delete the chunks instead of doing this
		for (BlockPos pos : BlockPos.iterate(interiorCorners.getFirst().add(0, -64, 0), interiorCorners.getSecond().add(0, 256, 0))) {
			level.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NO_REDRAW);
		}
	}
}
