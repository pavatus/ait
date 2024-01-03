package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.util.ForcedChunkUtil;
import mdteam.ait.tardis.util.desktop.structures.DesktopGenerator;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.Corners;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TardisDesktop {

    @Exclude
    protected final Tardis tardis; // todo this class needs moving to using a TardisLink
    private TardisDesktopSchema schema;
    private AbsoluteBlockPos.Directed doorPos;
    private AbsoluteBlockPos.Directed consolePos;
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
        // if (consolePos == null) searchForConsole();

        return consolePos;
    }

    private void searchForConsole() {
        // here comes the lag dodododo
        for (BlockPos pos : BlockPos.iterate(getCorners().getFirst(), getCorners().getSecond())) {
            if (TardisUtil.getTardisDimension().getBlockEntity(pos) instanceof ConsoleBlockEntity found) {
                // WE FOUND IT!!!
                this.setConsolePos(new AbsoluteBlockPos.Directed(found.getPos(), found.getWorld(), Direction.NORTH));
                return;
            }
        }
    }

    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        // before we do this we need to make sure to delete the old portals, but how?! by registering to this event
        TardisEvents.DOOR_MOVE.invoker().onMove(tardis, pos);

        this.doorPos = pos;
    }

    public void setConsolePos(AbsoluteBlockPos.Directed pos) {
        this.consolePos = pos;
        if (tardis != null)
            tardis.markDirty();
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
        long currentTime = System.currentTimeMillis();
        this.schema = schema;
        DesktopGenerator generator = new DesktopGenerator(this.schema);
        BlockPos doorPos = generator.place((ServerWorld) TardisUtil.getTardisDimension(), this.corners);
        if(doorPos != null) this.setInteriorDoorPos(new AbsoluteBlockPos.Directed(doorPos, TardisUtil.getTardisDimension(), Direction.SOUTH));
        this.updateDoor();
        AITMod.LOGGER.warn("Time taken to generate interior: " + (System.currentTimeMillis() - currentTime));
    }

    public void clearOldInterior(TardisDesktopSchema schema) {
        this.schema = schema;
        DesktopGenerator.clearArea((ServerWorld) TardisUtil.getTardisDimension(), this.corners);
        this.clearExistingEntities();
    }

    private void clearExistingEntities() {
        this.forceLoadInterior();

        for (Direction direction : Direction.values()) {
            BlockPos pos = doorPos.add(direction.getVector()); // Get the position of each adjacent block in the interior.
            BlockEntity blockEntity = TardisUtil.getTardisDimension().getBlockEntity(pos);
            if (blockEntity instanceof DoorBlockEntity) {
                continue;
            }
            TardisUtil.getTardisDimension().removeBlockEntity(pos);  // Remove any existing block entity at that position.
        }
        Box box = this.corners.getBox();
        for (Entity entity : TardisUtil.getTardisDimension().getEntitiesByClass(ItemFrameEntity.class, box, (entity) -> true)) { // todo there seems to be issues with the "box" variable as it is what is causing these things to not work
            System.out.println(entity);
            entity.discard();  // Kill any normal entities at that position.
        }
        for (Entity entity : TardisUtil.getTardisDimension().getEntitiesByClass(ItemEntity.class, box, (entity) -> true)) {
            entity.discard();  // Kill any normal entities at that position.
        }
        // for (LivingEntity entity : TardisUtil.getEntitiesInInterior(tardis)) {
        //     entity.discard();
        // }

        this.stopForceInterior();
    }

    private void forceLoadInterior() {
        World world = TardisUtil.getTardisDimension();
        if (world == null) return;

        for (BlockPos pos : this.iterateOverInterior()) {
            ForcedChunkUtil.keepChunkLoaded((ServerWorld) world, pos);
        }
    }
    private void stopForceInterior() {
        World world = TardisUtil.getTardisDimension();
        if (world == null) return;

        for (BlockPos pos : this.iterateOverInterior()) {
            ForcedChunkUtil.stopForceLoading((ServerWorld) world, pos);
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
        this.iterateOverInterior().forEach((blockPosList::add));
        return blockPosList;
    }
    private Iterable<BlockPos> iterateOverInterior() { return BlockPos.iterate(this.corners.getFirst(), this.corners.getSecond()); }
}
