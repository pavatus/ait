package loqor.ait.tardis.util.desktop.structures;

import loqor.ait.core.data.Corners;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.desktops.TardisStructureTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;

public class DesktopGenerator {

	private final TardisDesktopSchema schema;

	public DesktopGenerator(TardisDesktopSchema schema) {
		this.schema = schema;
	}

	public void place(Tardis tardis, ServerWorld level, Corners corners) {
		this.schema.findTemplate().ifPresent(parent -> {
			TardisStructureTemplate template = new TardisStructureTemplate(parent);

			template.place(tardis, level, BlockPos.ofFloored(corners.getBox().getCenter()),
					BlockPos.ofFloored(corners.getBox().getCenter()), new StructurePlacementData(),
					level.getRandom(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD
			);
		});
	}

	public static void clearArea(ServerWorld level, Corners interiorCorners) {
		// @TODO: Just delete the chunks instead of doing this
		for (BlockPos pos : BlockPos.iterate(interiorCorners.getFirst().add(0, -64, 0), interiorCorners.getSecond().add(0, 256, 0))) {
			level.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NO_REDRAW);
		}
	}
}
