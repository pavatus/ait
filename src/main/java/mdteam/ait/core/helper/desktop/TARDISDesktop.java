package mdteam.ait.core.helper.desktop;

import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.helper.*;
import mdteam.ait.core.tardis.Tardis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class TARDISDesktop {
    private DesktopSchema schema;
    private transient Tardis tardis;
    private List<AbsoluteBlockPos> interiorCornerPosList;
    private transient BlockPos interiorDoorPos;

    public TARDISDesktop(DesktopSchema schema) {
        this.schema = schema;
        setCorners();
    }

    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
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
        if (interiorDoorPos != null && getInteriorDimension().getBlockEntity(interiorDoorPos) instanceof DoorBlockEntity) {return interiorDoorPos;} // @TODO no interior door entity yet so

        return searchForDoorPosAndUpdate();
    }
    public List<AbsoluteBlockPos> getInteriorCornerPositions() {
        return interiorCornerPosList;
    }
    public void setInteriorCornerPositions(List<AbsoluteBlockPos> list) {
        this.interiorCornerPosList = list;
    }

    /**
     * Not centered properly. just kinda winging it
     * @return
     */
    public BlockPos getCentreBlockPos() {
        return getInteriorCornerPositions().get(0).toBlockPos().add(16, 0, 16);
    }
    private BlockPos searchForDoorPosAndUpdate() {
        // @TODO cba
        return getCentreBlockPos().add(0,8,0); // yum
//        BlockPos doorPos = interiorCornerPosList.get(0).add(getSchema().getDoorPosition());
//        System.out.println(doorPos);
//
//        if (!(getInteriorDimension().getBlockState(doorPos).getBlock() instanceof DoorBlock)) {
//            doorPos = TARDISManager.getInstance().searchForDoorBlock(interiorCornerPosList);
//        }
//
//        DoorBlockEntity door = (DoorBlockEntity) getInteriorDimension().getBlockEntity(doorPos);
//        assert door != null;
//        door.setTARDIS(tardis);
//
//        interiorDoorPos = doorPos;
//
//        return doorPos;
    }

    private BlockPos getOffsetDoorPosition() {
        BlockPos doorPos = getInteriorDoorPos();
        BlockPos adjustedPos = new BlockPos(0,0,0);
        Direction doorDirection = /*getInteriorDimension()
                .getBlockState(
                        doorPos)
                .get(Properties.HORIZONTAL_FACING);*/ Direction.NORTH;
//        switch(doorDirection) {
//            case NORTH -> adjustedPos = new BlockPos(doorPos.getX() + 0.5,doorPos.getY(),doorPos.getZ() - 1.5);
//            case SOUTH -> adjustedPos = new BlockPos(doorPos.getX() + 0.5,doorPos.getY(),doorPos.getZ() + 1.5);
//            case EAST -> adjustedPos = new BlockPos(doorPos.getX() + 1.5,doorPos.getY(),doorPos.getZ() + 0.5);
//            case WEST -> adjustedPos = new BlockPos(doorPos.getX() - 1.5,doorPos.getY(),doorPos.getZ() + 0.5);
//        }
        return doorPos;
    }

    public void teleportToDoor(Entity entity) {
        if (needsGeneration()) {
            generate();
        }

        Direction doorDirection = /*getInteriorDimension()
                .getBlockState(
                        getInteriorDoorPos())
                .get(Properties.HORIZONTAL_FACING);*/ Direction.NORTH;

        TeleportHelper helper = new TeleportHelper(entity.getUuid(),new AbsoluteBlockPos(getOffsetDoorPosition(), doorDirection, getInteriorDimension()));
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

