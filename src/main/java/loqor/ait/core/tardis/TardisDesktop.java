package loqor.ait.core.tardis;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import dev.drtheo.blockqueue.ActionQueue;
import dev.drtheo.blockqueue.QueuedStructureTemplate;
import dev.drtheo.blockqueue.util.ChunkEraser;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.core.world.QueuedTardisStructureTemplate;
import loqor.ait.data.Corners;
import loqor.ait.data.DirectedBlockPos;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;

public class TardisDesktop extends TardisComponent {

    private static final StructurePlacementData SETTINGS = new StructurePlacementData().setUpdateNeighbors(false);
    public static final Identifier CACHE_CONSOLE = AITMod.id("cache_console");

    private TardisDesktopSchema schema;
    private DirectedBlockPos doorPos;

    private final Corners corners;
    private final Set<BlockPos> consolePos;

    private static final int RADIUS = 500;
    private static final Corners CORNERS;

    static {
        BlockPos first = new BlockPos(RADIUS, 0, RADIUS);
        CORNERS = new Corners(first.multiply(-1), first);

        ServerPlayNetworking.registerGlobalReceiver(TardisDesktop.CACHE_CONSOLE,
                ServerTardisManager.receiveTardis((tardis, server, player, handler, buf, responseSender) -> {
                    BlockPos console = buf.readBlockPos();

                    server.execute(() -> {
                        if (tardis == null)
                            return;

                        tardis.getDesktop().cacheConsole(console);
                    });
                }));
    }

    public TardisDesktop(TardisDesktopSchema schema) {
        super(Id.DESKTOP);
        this.schema = schema;

        this.corners = CORNERS;
        this.consolePos = new HashSet<>();
    }

    @Override
    public void onCreate() {
        // TODO: lock the door until the queue finishes.
        this.createInteriorChangeQueue(schema, false).ifPresent(ActionQueue::execute);
    }

    @Override
    public void onLoaded() {
        if (this.isClient())
            return;

        for (BlockPos pos : this.consolePos) {
            if (tardis.asServer().getInteriorWorld().getBlockEntity(pos) instanceof ConsoleBlockEntity console)
                console.markNeedsControl();
        }
    }

    public TardisDesktopSchema getSchema() {
        return schema;
    }

    public DirectedBlockPos doorPos() {
        if (!this.hasDoorPosition()) {
            if (!this.consolePos.isEmpty())
                return DirectedBlockPos.create(this.consolePos.stream().findAny().orElseThrow().up(), (byte) RotationPropertyHelper.fromDirection(Direction.NORTH));

            return DirectedBlockPos.create(BlockPos.ofFloored(this.corners.getBox().getCenter()), (byte) RotationPropertyHelper.fromDirection(Direction.NORTH));
        }

        return doorPos;
    }

    public boolean hasDoorPosition() {
        return doorPos != null;
    }

    public void setInteriorDoorPos(DirectedBlockPos pos) {
        TardisEvents.DOOR_MOVE.invoker().onMove(this.tardis, pos);
        this.doorPos = pos;
    }

    // TODO this is strictly for clearing the interior now
    @Deprecated(forRemoval = true, since = "1.1.0")
    public Corners getCorners() {
        return corners;
    }

    public Optional<ActionQueue> createInteriorChangeQueue(TardisDesktopSchema schema, boolean sendEvent) {
        long start = System.currentTimeMillis();
        this.schema = schema;

        if (sendEvent)
            TardisEvents.RECONFIGURE_DESKTOP.invoker().reconfigure(this.tardis);

        ServerTardis tardis = this.tardis.asServer();
        ServerWorld world = tardis.getInteriorWorld();

        Optional<StructureTemplate> optional = this.schema.findTemplate();

        if (optional.isEmpty()) {
            AITMod.LOGGER.error("Failed to find template for {}", this.schema.id());
            return Optional.empty();
        }

        QueuedStructureTemplate template = new QueuedTardisStructureTemplate(optional.get(), tardis);

        Optional<ActionQueue> optionalQueue = template.place(world, BlockPos.ofFloored(corners.getBox().getCenter()),
                BlockPos.ofFloored(corners.getBox().getCenter()), SETTINGS, world.getRandom(), Block.FORCE_STATE);

        optionalQueue.ifPresentOrElse(queue -> queue.thenRun(
                () -> AITMod.LOGGER.warn("Time taken to generate interior: {}ms",
                        System.currentTimeMillis() - start)),
                () -> AITMod.LOGGER.error("Failed to generate interior for {}",
                        this.tardis.getUuid())
        );

        return optionalQueue;
    }

    public ActionQueue createDesktopClearQueue() {
        ServerWorld world = this.tardis.asServer().getInteriorWorld();
        int chunkRadius = ChunkSectionPos.getSectionCoord(RADIUS);

        // FIXME THEO: gross
        TardisUtil.getEntitiesInBox(ItemFrameEntity.class, world, corners.getBox(), frame -> true)
                .forEach(frame -> frame.remove(Entity.RemovalReason.DISCARDED));

        return new ChunkEraser.Builder().build(
                world, -chunkRadius, -chunkRadius, chunkRadius, chunkRadius
        );
    }

    public void cacheConsole(BlockPos consolePos) {
        World dim = this.tardis.asServer().getInteriorWorld();
        dim.playSound(null, consolePos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 0.5f, 1.0f);

        ConsoleGeneratorBlockEntity generator = new ConsoleGeneratorBlockEntity(consolePos,
                AITBlocks.CONSOLE_GENERATOR.getDefaultState());

        if (dim.getBlockEntity(consolePos) instanceof ConsoleBlockEntity entity)
            entity.killControls();

        dim.removeBlock(consolePos, false);
        dim.removeBlockEntity(consolePos);

        dim.setBlockState(consolePos, AITBlocks.CONSOLE_GENERATOR.getDefaultState(), Block.NOTIFY_ALL);
        dim.addBlockEntity(generator);
    }

    public static void playSoundAtConsole(World dim, BlockPos console, SoundEvent sound, SoundCategory category, float volume,
            float pitch) {
        dim.playSound(null, console, sound, category, volume, pitch);
    }

    public void playSoundAtEveryConsole(SoundEvent sound, SoundCategory category, float volume, float pitch) {
        this.getConsolePos().forEach(consolePos -> playSoundAtConsole(this.tardis.asServer().getInteriorWorld(), consolePos, sound, category, volume, pitch));
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
}
