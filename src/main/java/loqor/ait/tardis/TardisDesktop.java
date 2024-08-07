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
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.util.desktop.structures.DesktopGenerator;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import static loqor.ait.core.util.LegacyUtil.Consoles;

public class TardisDesktop extends TardisComponent {

	public static final Identifier CACHE_CONSOLE = new Identifier(AITMod.MOD_ID, "cache_console");

	private TardisDesktopSchema schema;
	private DirectedBlockPos doorPos;

	private final Corners corners;
	private final Consoles consolePos;

	public TardisDesktop(TardisDesktopSchema schema) {
		super(Id.DESKTOP);
		this.schema = schema;

		this.corners = TardisUtil.findInteriorSpot();
		this.consolePos = new Consoles();
	}

	private TardisDesktop(TardisDesktopSchema schema, DirectedBlockPos doorPos, Corners corners, Consoles consolePos) {
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

	@Override
	public void onLoaded() {
		if (this.isClient())
			return;

		for (BlockPos pos : this.consolePos) {
			if (TardisUtil.getTardisDimension().getBlockEntity(pos) instanceof ConsoleBlockEntity console)
				console.markNeedsControl();
		}
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

		TardisEvents.RECONFIGURE_DESKTOP.invoker().reconfigure(this.tardis);

		DesktopGenerator generator = new DesktopGenerator(this.schema);
		generator.place(this.tardis, (ServerWorld) TardisUtil.getTardisDimension(), this.corners);

        AITMod.LOGGER.warn("Time taken to generate interior: {}", System.currentTimeMillis() - currentTime);
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

	public static void playSoundAtConsole(BlockPos console, SoundEvent sound, SoundCategory category, float volume, float pitch) {
		ServerWorld dim = (ServerWorld) TardisUtil.getTardisDimension();
		dim.playSound(null, console, sound, category, volume, pitch);
	}

	public void playSoundAtEveryConsole(SoundEvent sound, SoundCategory category, float volume, float pitch) {
		this.getConsolePos().forEach(consolePos -> playSoundAtConsole(
				consolePos, sound, category, volume, pitch)
		);
	}

	public void playSoundAtEveryConsole(SoundEvent sound, SoundCategory category) {
		this.playSoundAtEveryConsole(sound, category, 1f, 1f);
	}

	public void playSoundAtEveryConsole(SoundEvent sound) {
		this.playSoundAtEveryConsole(sound, SoundCategory.BLOCKS);
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
			Consoles consoles;

			JsonArray jsonConsolePos = obj.getAsJsonArray("consolePos");

			if (jsonConsolePos == null) {
				JsonArray jsonConsoles = obj.getAsJsonArray("consoles");
				consoles = LegacyUtil.flatConsoles(jsonConsoles, context);
			} else {
				consoles = context.deserialize(jsonConsolePos, Consoles.class);
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
