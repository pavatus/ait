package loqor.ait.tardis;

import com.google.gson.*;
import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import loqor.ait.core.data.Corners;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.util.LegacyUtil;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.util.desktop.structures.DesktopGenerator;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class TardisDesktop extends TardisComponent implements TardisTickable {

	public static final Identifier CACHE_CONSOLE = new Identifier(AITMod.MOD_ID, "cache_console");

	private TardisDesktopSchema schema;
	private DirectedBlockPos doorPos;

	private final Corners corners;
	private final Set<BlockPos> consolePos;

	public TardisDesktop(TardisDesktopSchema schema) {
		super(Id.DESKTOP);
		this.schema = schema;

		this.corners = TardisUtil.findInteriorSpot();
		this.consolePos = new HashSet<>();
	}

	private TardisDesktop(TardisDesktopSchema schema, DirectedBlockPos doorPos, Corners corners, Set<BlockPos> consolePos) {
		super(Id.DESKTOP);

		this.schema = schema;
		this.doorPos = doorPos;

		this.corners = corners;
		this.consolePos = consolePos;
	}

	@Override
	public void onCreate() {
		this.changeInterior(schema);
	}

	public TardisDesktopSchema getSchema() {
		return schema;
	}

	public DirectedBlockPos doorPos() {
		return doorPos;
	}

	public void setInteriorDoorPos(DirectedBlockPos pos) {
		TardisEvents.DOOR_MOVE.invoker().onMove(tardis(), pos);
		this.doorPos = pos;
	}

	public Corners getCorners() {
		return corners;
	}

	public void changeInterior(TardisDesktopSchema schema) {
		long currentTime = System.currentTimeMillis();
		this.schema = schema;

		DesktopGenerator generator = new DesktopGenerator(this.schema);
		generator.place(this.tardis, (ServerWorld) TardisUtil.getTardisDimension(), this.corners);

		AITMod.LOGGER.warn("Time taken to generate interior: " + (System.currentTimeMillis() - currentTime));
	}

	public void clearOldInterior(TardisDesktopSchema schema) {
		this.schema = schema;
		DesktopGenerator.clearArea((ServerWorld) TardisUtil.getTardisDimension(), this.corners);
	}

	public void cacheConsole(BlockPos consolePos) {
		World dim = TardisUtil.getTardisDimension();
		dim.playSound(null, consolePos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 0.5f, 1.0f);

		ConsoleGeneratorBlockEntity generator = new ConsoleGeneratorBlockEntity(consolePos, AITBlocks.CONSOLE_GENERATOR.getDefaultState());

		if (dim.getBlockEntity(consolePos) instanceof ConsoleBlockEntity entity)
			entity.killControls();

		dim.removeBlock(consolePos, false);
		dim.removeBlockEntity(consolePos);

		dim.setBlockState(consolePos, AITBlocks.CONSOLE_GENERATOR.getDefaultState(), Block.NOTIFY_ALL);
		dim.addBlockEntity(generator);
	}

	public Set<BlockPos> getConsolePos() {
		return consolePos;
	}

	public static Object updater() {
		return new Updater();
	}

	private static class Updater implements JsonDeserializer<TardisDesktop>, JsonSerializer<TardisDesktop> {

		@Override
		public TardisDesktop deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject obj = json.getAsJsonObject();

			TardisDesktopSchema schema = context.deserialize(obj.get("schema"), TardisDesktopSchema.class);
			DirectedBlockPos doorPos = context.deserialize(obj.get("doorPos"), DirectedBlockPos.class);
			Corners corners = context.deserialize(obj.get("corners"), Corners.class);
			Set<BlockPos> consoles;

			JsonArray jsonConsolePos = obj.getAsJsonArray("consolePos");

			if (jsonConsolePos == null) {
				JsonArray jsonConsoles = obj.getAsJsonArray("consoles");
				consoles = LegacyUtil.flatConsoles(jsonConsoles, context);
			} else {
				consoles = context.<HashSet<BlockPos>>deserialize(jsonConsolePos, HashSet.class);
			}

			return new TardisDesktop(schema, doorPos, corners, consoles);
		}

		@Override
		public JsonElement serialize(TardisDesktop src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject object = new JsonObject();
			object.add("schema", context.serialize(src.schema, TardisDesktopSchema.class));
			object.add("doorPos", context.serialize(src.doorPos, DirectedBlockPos.class));
			object.add("corners", context.serialize(src.corners, Corners.class));

			JsonArray arr = context.serialize(src.consolePos, HashSet.class).getAsJsonArray();
			object.add("consolePos", arr);

			return object;
		}
	}
}
