package loqor.ait.core.tardis;

import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
import loqor.ait.core.tardis.util.DesktopGenerator;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.Corners;
import loqor.ait.data.DirectedBlockPos;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;

public class TardisDesktop extends TardisComponent {

    public static final Identifier CACHE_CONSOLE = new Identifier(AITMod.MOD_ID, "cache_console");

    private TardisDesktopSchema schema;
    private DirectedBlockPos doorPos;

    private final Corners corners;
    private final Set<BlockPos> consolePos;

    static {
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

        this.corners = TardisUtil.findInteriorSpot();
        this.consolePos = new HashSet<>();
    }

    @Override
    public void onCreate() {
        this.changeInterior(schema, false);
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
        if (!this.hasDoorPosition() && !this.consolePos.isEmpty()) {
            return DirectedBlockPos.create(this.consolePos.stream().findAny().orElseThrow().up(), (byte) RotationPropertyHelper.fromDirection(Direction.NORTH));
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
    //@Deprecated(forRemoval = true, since = "1.1.0")
    public Corners getCorners() {
        return corners;
    }

    public void changeInterior(TardisDesktopSchema schema, boolean sendEvent) {
        long currentTime = System.currentTimeMillis();
        this.schema = schema;

        if (sendEvent)
            TardisEvents.RECONFIGURE_DESKTOP.invoker().reconfigure(this.tardis);

        DesktopGenerator generator = new DesktopGenerator(this.schema);
        boolean success = generator.place(this.tardis, this.tardis.asServer().getInteriorWorld(true), this.corners);

        if (!success)
            AITMod.LOGGER.error("Failed to generate interior for {}", this.tardis.getUuid());

        AITMod.LOGGER.warn("Time taken to generate interior: {}", System.currentTimeMillis() - currentTime);
    }

    public void clearOldInterior() {
        DesktopGenerator.clearArea(this.tardis.asServer().getInteriorWorld(), this.corners);
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

    public static void playSoundAtConsole(ServerWorld dim, BlockPos console, SoundEvent sound, SoundCategory category, float volume,
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
