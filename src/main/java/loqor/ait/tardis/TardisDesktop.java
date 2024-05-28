package loqor.ait.tardis;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.Corners;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisLink;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.util.desktop.structures.DesktopGenerator;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TardisDesktop extends TardisLink {

	public static final Identifier CACHE_CONSOLE = new Identifier(AITMod.MOD_ID, "cache_console");

	private TardisDesktopSchema schema;
	private AbsoluteBlockPos.Directed doorPos;
	private AbsoluteBlockPos.Directed consolePos;
	private ConcurrentLinkedQueue<TardisConsole> consoles;
	private final Corners corners;

	public TardisDesktop(TardisDesktopSchema schema) {
		super(Id.DESKTOP);

		this.schema = schema;
		this.corners = TardisUtil.findInteriorSpot();
	}

	public TardisDesktop(TardisDesktopSchema schema, Corners corners, AbsoluteBlockPos.Directed door, AbsoluteBlockPos.Directed console) {
		super(Id.DESKTOP);

		this.schema = schema;
		this.corners = corners;
		this.doorPos = door;
		this.consolePos = console;
	}

	@Override
	public void onCreate() {
		this.changeInterior(schema);
	}

	@Override
	protected void onInit(InitContext ctx) {
		// in some cases, an old and fucked up save can have no consoles field.
		if (this.consoles == null)
			this.consoles = new ConcurrentLinkedQueue<>();

		for (TardisConsole console : this.consoles) {
			TardisComponent.init(console, this.tardis, ctx);
		}
	}

	public TardisDesktopSchema getSchema() {
		return schema;
	}

	public AbsoluteBlockPos.Directed getInteriorDoorPos() {
		if (this.doorPos == null)
			linkToInteriorBlocks();

		return doorPos;
	}

	// bad laggy bad bad but i cant be botehredddddd
	private void linkToInteriorBlocks() {
		if (this.tardis() instanceof ClientTardis)
			return;

		if (doorPos != null && consolePos != null)
			return; // no need to relink

		BlockEntity entity;
		for (BlockPos pos : this.iterateOverInterior()) { // FIXME this is bullshit
			entity = TardisUtil.getTardisDimension().getBlockEntity(pos);

			if (entity == null)
				continue;

			if (doorPos == null && entity instanceof DoorBlockEntity door) {
				door.setTardis(this.tardis());
				continue;
			}

			if (consolePos == null && entity instanceof ConsoleBlockEntity console) {
				console.setTardis(this.tardis());
            }
		}
	}

	@Deprecated(forRemoval = true)
	public AbsoluteBlockPos.Directed getConsolePos() {
		if (consolePos == null)
			linkToInteriorBlocks();

		return consolePos;
	}

	public ConcurrentLinkedQueue<TardisConsole> getConsoles() {
		return this.consoles;
	}

	public void addConsole(TardisConsole console) {
		this.consoles.add(console);
		this.sync();
	}

	public void removeConsole(TardisConsole console) {
		this.consoles.remove(console);
		this.sync();
	}

	public void clearConsoles() {
		this.consoles = new ConcurrentLinkedQueue<>();
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);

		for (TardisConsole console : this.getConsoles()) {
			console.tick(server);
		}
	}

	public @Nullable TardisConsole findConsole(AbsoluteBlockPos position) {
		for (TardisConsole console : this.getConsoles()) {
			if (console.position().equals(position)) {
				return console;
			}
		}
		return null;
	}

	public @Nullable TardisConsole findConsole(UUID uuid) {
		for (TardisConsole console : this.getConsoles()) {
			if (console.uuid().equals(uuid)) {
				return console;
			}
		}

		return null;
	}

	public Optional<TardisConsole> findCurrentConsole() {
		for (TardisConsole console : this.getConsoles()) {
			if (console.inUse()) {
				return Optional.of(console);
			}
		}
		return Optional.empty();
	}

	public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
		TardisEvents.DOOR_MOVE.invoker().onMove(tardis(), pos);
		this.doorPos = pos;
	}

	@Deprecated(forRemoval = true)
	public void setConsolePos(AbsoluteBlockPos.Directed pos) {
		this.consolePos = pos;
		this.sync();
	}

	public Corners getCorners() {
		return corners;
	}

	public void updateDoor() {
		if (!(TardisUtil.getTardisDimension().getBlockEntity(doorPos) instanceof DoorBlockEntity door)) {
			AITMod.LOGGER.error("Failed to find the interior door!");
			return;
		}

		// this is needed for door and console initialization. when we call #setTardis(ITardis) the desktop field is still null.
		door.setDesktop(this);
		door.setTardis(tardis());
	}

	public void changeInterior(TardisDesktopSchema schema) {
		long currentTime = System.currentTimeMillis();
		this.schema = schema;

		DesktopGenerator generator = new DesktopGenerator(this.schema);
		BlockPos doorPos = generator.place((ServerWorld) TardisUtil.getTardisDimension(), this.corners);

		if (doorPos != null) {
			this.setInteriorDoorPos(new AbsoluteBlockPos.Directed(doorPos, TardisUtil.getTardisDimension(), 8));
			this.updateDoor();
		}

		AITMod.LOGGER.warn("Time taken to generate interior: " + (System.currentTimeMillis() - currentTime));
	}

	public void clearOldInterior(TardisDesktopSchema schema) {
		this.schema = schema;
		DesktopGenerator.clearArea((ServerWorld) TardisUtil.getTardisDimension(), this.corners);
//        this.clearInteriorEntities();
	}

	public void cacheConsole(UUID consoleId) {
		if (this.getConsoles().isEmpty()) return;
		ServerWorld dim = (ServerWorld) TardisUtil.getTardisDimension();

		TardisConsole console = this.findConsole(consoleId);

		if (console == null) return;

		AbsoluteBlockPos consolePos = console.position();

		dim.playSound(null, consolePos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 0.5f, 1.0f);

		ConsoleGeneratorBlockEntity generator = new ConsoleGeneratorBlockEntity(consolePos, AITBlocks.CONSOLE_GENERATOR.getDefaultState());

		if (dim.getBlockEntity(consolePos) instanceof ConsoleBlockEntity entity)
			entity.killControls();

		dim.removeBlock(consolePos, false);
		dim.removeBlockEntity(consolePos);

		dim.setBlockState(consolePos, AITBlocks.CONSOLE_GENERATOR.getDefaultState(), Block.NOTIFY_ALL);
		dim.addBlockEntity(generator);

		this.removeConsole(console);
	}

	private Iterable<BlockPos> iterateOverInterior() {
		return BlockPos.iterate(this.corners.getFirst(), this.corners.getSecond());
	}
}
