package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.util.ForcedChunkUtil;
import mdteam.ait.tardis.data.TardisLink;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TardisDesktop extends TardisLink {
    public static final Identifier CACHE_CONSOLE = new Identifier(AITMod.MOD_ID, "cache_console");
    private TardisDesktopSchema schema;
    private AbsoluteBlockPos.Directed doorPos;
    private AbsoluteBlockPos.Directed consolePos;
    private final Corners corners;

    public TardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        super(tardis,"desktop");
        this.schema = schema;
        this.corners = TardisUtil.findInteriorSpot();

//        BlockPos doorPos = new DesktopGenerator(schema).place(
//                (ServerWorld) TardisUtil.getTardisDimension(), this.getCorners()
//        );
//
//        System.out.println(doorPos);
//
//        if (!(TardisUtil.getTardisDimension().getBlockEntity(doorPos) instanceof DoorBlockEntity door)) {
//            AITMod.LOGGER.error("Failed to find the interior door!");
//            return;
//        }
//
//        // this is needed for door and console initialization. when we call #setTardis(ITardis) the desktop field is still null.
//        door.setDesktop(this);
//        door.setTardis(tardis);
        this.changeInterior(schema);
        //console.setTardis(tardis);
    }

    public TardisDesktop(Tardis tardis, TardisDesktopSchema schema, Corners corners, AbsoluteBlockPos.Directed door, AbsoluteBlockPos.Directed console) {
        super(tardis, "desktop");
        this.schema = schema;
        this.corners = corners;
        this.doorPos = door;
        this.consolePos = console;
    }

    public TardisDesktopSchema getSchema() {
        return schema;
    }

    public AbsoluteBlockPos.Directed getInteriorDoorPos() {
        if (this.doorPos == null && this.getTardis().isPresent()) {
            linkToInteriorBlocks();
        }

        return doorPos;
    }
    // bad laggy bad bad but i cant be botehredddddd
    private void linkToInteriorBlocks() {
        if (TardisUtil.isClient()) return;
        if (doorPos != null && consolePos != null) return; // no need to relink

        BlockEntity entity;
        for (BlockPos pos : this.iterateOverInterior()) {
            entity = TardisUtil.getTardisDimension().getBlockEntity(pos);
            if (entity == null) continue;
            if (doorPos == null && entity instanceof DoorBlockEntity door) {
                door.setTardis(this.getTardis().get());
                continue;
            }
            if (consolePos == null && entity instanceof ConsoleBlockEntity console) {
                console.setTardis(this.getTardis().get());
                continue;
            }
        }
    }

    public AbsoluteBlockPos.Directed getConsolePos() {
        if (consolePos == null && this.getTardis().isPresent()) linkToInteriorBlocks();

        return consolePos;
    }
    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        // before we do this we need to make sure to delete the old portals, but how?! by registering to this event
        if(getTardis().isPresent())
            TardisEvents.DOOR_MOVE.invoker().onMove(getTardis().get(), pos);

        this.doorPos = pos;
    }

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
        if(getTardis().isEmpty()) return;
        //console.setDesktop(this);
        door.setTardis(getTardis().get());
    }

    public void changeInterior(TardisDesktopSchema schema) {
        long currentTime = System.currentTimeMillis();
        this.schema = schema;
        DesktopGenerator generator = new DesktopGenerator(this.schema);
        BlockPos doorPos = generator.place((ServerWorld) TardisUtil.getTardisDimension(), this.corners);
        if(doorPos != null) {
            this.setInteriorDoorPos(new AbsoluteBlockPos.Directed(doorPos, TardisUtil.getTardisDimension(), Direction.SOUTH));
            this.updateDoor();
        }
        AITMod.LOGGER.warn("Time taken to generate interior: " + (System.currentTimeMillis() - currentTime));
    }

    public void clearOldInterior(TardisDesktopSchema schema) {
        this.schema = schema;
        DesktopGenerator.clearArea((ServerWorld) TardisUtil.getTardisDimension(), this.corners);
//        this.clearInteriorEntities();
    }

    private void clearInteriorEntities() {
        this.forceLoadInterior();

        if (this.doorPos == null) return;

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
            entity.discard();  // Kill any normal entities at that position.
        }
        for (Entity entity : TardisUtil.getTardisDimension().getEntitiesByClass(ItemEntity.class, box, (entity) -> true)) {
            entity.discard();  // Kill any normal entities at that position.
        }

        if(getTardis().isEmpty()) return;

        for (LivingEntity entity : TardisUtil.getLivingEntitiesInInterior(getTardis().get(), 256)) {
            entity.discard();
        }

        // for (LivingEntity entity : TardisUtil.getEntitiesInInterior(tardis)) {
        //     entity.discard();
        // }

        this.stopForceInterior();
    }
    public void cacheConsole() {
        if(this.getConsolePos() == null) return;
        ServerWorld dim = (ServerWorld) TardisUtil.getTardisDimension();

        dim.playSound(null, this.getConsolePos(), SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 0.5f, 1.0f);

        ConsoleGeneratorBlockEntity generator = new ConsoleGeneratorBlockEntity(this.getConsolePos(), AITBlocks.CONSOLE_GENERATOR.getDefaultState());

        // todo the console is always null
        if(dim.getBlockEntity(this.getConsolePos()) instanceof ConsoleBlockEntity console) {
            console.killControls();

            // todo this doesnt send to client or something
            // generator.changeConsole(console.getVariant());
            // generator.changeConsole(console.getConsoleSchema());
            // generator.markDirty();
        }

        dim.removeBlock(this.getConsolePos(), false);
        dim.removeBlockEntity(this.getConsolePos());


        dim.setBlockState(this.getConsolePos(), AITBlocks.CONSOLE_GENERATOR.getDefaultState(), Block.NOTIFY_ALL);
        dim.addBlockEntity(generator);

        this.setConsolePos(null);
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
