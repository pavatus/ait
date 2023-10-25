package mdteam.ait.core.helper.desktop;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blocks.DoorBlock;
import mdteam.ait.core.helper.*;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.TardisHandler;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.List;

public class TARDISDesktop implements Serializable {
    private DesktopSchema schema;
    private Tardis tardis;
    private List<AbsoluteBlockPos> interiorCornerPosList;
    private BlockPos interiorDoorPos;

    public TARDISDesktop(DesktopSchema schema) {
        setSchema(schema);
        setCorners();
    }

    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
    }
    public Tardis getTardis() {
        return tardis;
    }

    public void setSchema(DesktopSchema schema) {
        this.schema = schema;
    }
    public DesktopSchema getSchema() {
        return schema;
    }
    public World getInteriorDimension() {
        return TardisUtil.getTardisDimension();
    }
    public BlockPos getInteriorDoorPos() {
            return interiorDoorPos;
    }
    public List<AbsoluteBlockPos> getInteriorCornerPositions() {
        return interiorCornerPosList;
    }
    public void setInteriorCornerPositions(List<AbsoluteBlockPos> list) {
        this.interiorCornerPosList = list;
    }

    public BlockPos getCentreBlockPos() {
        return getInteriorCornerPositions().get(0).toBlockPos().add(getSchema().getTemplate().getSize().getX() / 2, 0, getSchema().getTemplate().getSize().getZ() / 2);
    }

    public void setInteriorDoorPosition(BlockPos pos) {
        this.interiorDoorPos = pos;
    }

    /*private BlockPos searchForDoorPosAndUpdate() {
        BlockPos doorPos = interiorCornerPosList.get(0).toBlockPos().add(getSchema().getDoorPosition().toBlockPos());
        System.out.println(doorPos);

        if (!(getInteriorDimension().getBlockState(doorPos).getBlock() instanceof DoorBlock)) {
            doorPos = TardisHandler.searchForDoorBlock(interiorCornerPosList);
        }

        DoorBlockEntity door = (DoorBlockEntity) getInteriorDimension().getBlockEntity(doorPos);
        assert door != null;
        door.setTardis(TardisHandler.getTardisByInteriorPos(doorPos));

        this.interiorDoorPos = doorPos;

        return doorPos;
    }*/

    public static BlockPos offsetDoorPosition(BlockPos blockPos, World world) {
        BlockPos adjustedPos = new BlockPos(0,0,0);
        Direction doorDirection = /*world
                .getBlockState(
                        blockPos)
                .get(Properties.HORIZONTAL_FACING);*/ Direction.NORTH;
        switch(doorDirection) {
            case NORTH -> adjustedPos = new BlockPos.Mutable(blockPos.getX() + 0.5,blockPos.getY(),blockPos.getZ() - 1);
            case SOUTH -> adjustedPos = new BlockPos.Mutable(blockPos.getX() + 0.5,blockPos.getY(),blockPos.getZ() + 1);
            case EAST -> adjustedPos = new BlockPos.Mutable(blockPos.getX() + 1,blockPos.getY(),blockPos.getZ() + 0.5);
            case WEST -> adjustedPos = new BlockPos.Mutable(blockPos.getX() - 1,blockPos.getY(),blockPos.getZ() + 0.5);
        }
        return adjustedPos;
    }

    public void teleportToDoor(Entity entity) {
        if (needsGeneration()) {
            generate();
        }

        Direction doorDirection = getInteriorDimension()
                .getBlockState(
                       getInteriorDoorPos())
                .get(Properties.HORIZONTAL_FACING);

        TeleportHelper helper = new TeleportHelper(entity.getUuid(), new AbsoluteBlockPos(offsetDoorPosition(getInteriorDoorPos(), getInteriorDimension()), doorDirection, getInteriorDimension()));
        helper.teleport((ServerWorld) entity.getWorld());
    }

    public static void teleportToExterior(Entity entity, AbsoluteBlockPos blockPos, World world, Direction direction) {

        TeleportHelper helper = new TeleportHelper(entity.getUuid(), new AbsoluteBlockPos(offsetDoorPosition(blockPos.toBlockPos(), world), direction, world));

        helper.teleport((ServerWorld) entity.getWorld());
    }

    public void delete() {
        DesktopGenerator.InteriorGenerator generator = new DesktopGenerator.InteriorGenerator(tardis, (ServerWorld) getInteriorDimension(),getSchema());
//        generator.deleteInterior(); // @TODO no thread for deleting interiors yet, it was also pretty slow on box mod so maybe theres a faster way? - duzo
        interiorCornerPosList = null;
    }
    public void generate() {
        generate(getSchema());
    }
    public void generate(DesktopSchema schema) {
        setInteriorCornerPositions(DesktopUtil.getNextAvailableInteriorSpot());
        DesktopGenerator.InteriorGenerator generator = new DesktopGenerator.InteriorGenerator(tardis, (ServerWorld) getInteriorDimension(), schema);
        generator.placeStructure((ServerWorld) getInteriorDimension(), getInteriorCornerPositions().get(0).toBlockPos(), Direction.SOUTH);
    }
    public boolean needsGeneration() {
        return interiorCornerPosList == null;
    }
    private void setCorners() {
        if (getInteriorCornerPositions() == null) return;
        getInteriorCornerPositions().set(0, new AbsoluteBlockPos(getInteriorCornerPositions().get(0).toBlockPos(), getInteriorDimension()));
        getInteriorCornerPositions().set(1, new AbsoluteBlockPos(getInteriorCornerPositions().get(1).toBlockPos(), getInteriorDimension()));
    }
}

