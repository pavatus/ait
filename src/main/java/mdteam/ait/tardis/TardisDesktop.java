package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.AITConsoleVariants;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.util.ForcedChunkUtil;
import mdteam.ait.tardis.util.desktop.structures.DesktopGenerator;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.Corners;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class TardisDesktop {

    @Exclude
    protected final Tardis tardis;
    private TardisDesktopSchema schema;
    private AbsoluteBlockPos.Directed doorPos;
    private AbsoluteBlockPos.Directed consolePos;
    private ConsoleEnum consoleType;
    private AITConsoleVariants consoleVariants;
    private final Corners corners;

    public TardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        this.tardis = tardis;
        this.schema = schema;
        this.corners = TardisUtil.findInteriorSpot();

        BlockPos doorPos = new DesktopGenerator(schema).place(
                (ServerWorld) TardisUtil.getTardisDimension(), this.getCorners()
        );

        if (!(TardisUtil.getTardisDimension().getBlockEntity(doorPos) instanceof DoorBlockEntity door)) {
            AITMod.LOGGER.error("Failed to find the interior door!");
            return;
        }

        // this is needed for door and console initialization. when we call #setTardis(ITardis) the desktop field is still null.
        door.setDesktop(this);
        //console.setDesktop(this);
        door.setTardis(tardis);
        //console.setTardis(tardis);
    }

    public TardisDesktop(Tardis tardis, TardisDesktopSchema schema, Corners corners, AbsoluteBlockPos.Directed door, AbsoluteBlockPos.Directed console) {
        this.tardis = tardis;
        this.schema = schema;
        this.corners = corners;
        this.doorPos = door;
        this.consolePos = console;
    }

    public TardisDesktopSchema getSchema() {
        return schema;
    }

    public AbsoluteBlockPos.Directed getInteriorDoorPos() {
        return doorPos;
    }

    public AbsoluteBlockPos.Directed getConsolePos() {
        return consolePos;
    }

    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        this.doorPos = pos;
    }

    public void setConsolePos(AbsoluteBlockPos.Directed pos) {
        this.consolePos = pos;
    }

    public Corners getCorners() {
        return corners;
    }

    public boolean updateDoor() {
        if (!(TardisUtil.getTardisDimension().getBlockEntity(doorPos) instanceof DoorBlockEntity door)) {
            AITMod.LOGGER.error("Failed to find the interior door!");
            return false;
        }

        // this is needed for door and console initialization. when we call #setTardis(ITardis) the desktop field is still null.
        door.setDesktop(this);
        //console.setDesktop(this);
        door.setTardis(tardis);
        return true;
    }

    public void changeInterior(TardisDesktopSchema schema) {
        this.schema = schema;
        DesktopGenerator generator = new DesktopGenerator(this.schema);
        List<BlockPos> allBlocks = getBlockPosListFromCorners();
        forceLoadChunks(allBlocks);
        DesktopGenerator.clearArea((ServerWorld) TardisUtil.getTardisDimension(), this.corners);
        clearExistingEntities();
        unforceLoadChunks(allBlocks);
        BlockPos doorPos = generator.place((ServerWorld) TardisUtil.getTardisDimension(), this.corners);
        this.setInteriorDoorPos(new AbsoluteBlockPos.Directed(doorPos, TardisUtil.getTardisDimension(), Direction.SOUTH));
        this.updateDoor();
    }

    private void clearExistingEntities() {
        for (Direction direction : Direction.values()) {
            BlockPos pos = doorPos.add(direction.getVector()); // Get the position of each adjacent block in the interior.
            TardisUtil.getTardisDimension().removeBlockEntity(pos);  // Remove any existing block entity at that position.
            Box box = this.corners.getBox();
            for (Entity entity : TardisUtil.getTardisDimension().getEntitiesByClass(ItemFrameEntity.class, box, (entity) -> true)) {
                entity.kill();  // Kill any normal entities at that position.
            }
            for (Entity entity : TardisUtil.getTardisDimension().getEntitiesByClass(ItemEntity.class, box, (entity) -> true)) {
                entity.kill();  // Kill any normal entities at that position.
            }
        }
    }

    private void forceLoadChunks(List<BlockPos> blockPosList) {
        World world = TardisUtil.getTardisDimension();
        if (world == null) return;
        MinecraftServer server = world.getServer();
        if (server == null) return;
        for (BlockPos blockPos: blockPosList) {
            ForcedChunkUtil.keepChunkLoaded((ServerWorld) world, blockPos);
        }

    }

    private void unforceLoadChunks(List<BlockPos> blockPosList) {
        World world = TardisUtil.getTardisDimension();
        if (world == null) return;
        MinecraftServer server = world.getServer();
        if (server == null) return;
        for (BlockPos blockPos: blockPosList) {
            ForcedChunkUtil.stopForceLoading((ServerWorld) world, blockPos);
        }
    }

    private List<BlockPos> getBlockPosListFromCorners() {
        List<BlockPos> blockPosList = new ArrayList<>();
        Box box = this.corners.getBox();

        for (int x = (int) box.minX; x < (int) box.maxX; x++) {
            for (int y = (int) box.minY; y < (int) box.maxY; y++) {
                for (int z = (int) box.minZ; z < (int) box.maxZ; z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    blockPosList.add(blockPos);
                }
            }
        }
        return blockPosList;
    }
}
