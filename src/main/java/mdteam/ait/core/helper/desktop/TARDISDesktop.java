package mdteam.ait.core.helper.desktop;

import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.*;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.TardisData;
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
    private Tardis tardis;
    private List<AbsoluteBlockPos> interiorCornerPosList;
    private BlockPos interiorDoorPos;

    public TARDISDesktop(DesktopSchema schema) {
        this.schema = schema;
    }
    public TARDISDesktop(DesktopSchema schema, NbtCompound nbt) {
        this(schema);
        this.loadCorners(nbt);
    }

    public void link(Tardis tardis) {
        this.tardis = tardis;
    }
    public void setSchema(DesktopSchema schema) {
        this.schema = schema;
    }
    public DesktopSchema getSchema() {return this.schema;}
    public World getInteriorDimension() {
        return TARDISUtil.getTardisDimension();
    }
    public BlockPos getInteriorDoorPos() {
        if (this.interiorDoorPos != null && this.getInteriorDimension().getBlockEntity(this.interiorDoorPos) instanceof DoorBlockEntity) {return this.interiorDoorPos;} // @TODO no interior door entity yet so

        return this.searchForDoorPosAndUpdate();
    }
    public List<AbsoluteBlockPos> getInteriorCornerPositions() {
        return this.interiorCornerPosList;
    }
    public void setInteriorCornerPositions(List<AbsoluteBlockPos> list) {
        this.interiorCornerPosList = list;
    }

    /**
     * Not centered properly. just kinda winging it
     * @return
     */
    public BlockPos getCentreBlockPos() {
        return this.getInteriorCornerPositions().get(0).add(16,0,16);
    }
    private BlockPos searchForDoorPosAndUpdate() {
        // @TODO cba
        return this.getCentreBlockPos().add(0,8,0); // yum
//        BlockPos doorPos = this.interiorCornerPosList.get(0).add(this.getSchema().getDoorPosition());
//        System.out.println(doorPos);
//
//        if (!(this.getInteriorDimension().getBlockState(doorPos).getBlock() instanceof DoorBlock)) {
//            doorPos = TARDISManager.getInstance().searchForDoorBlock(this.interiorCornerPosList);
//        }
//
//        DoorBlockEntity door = (DoorBlockEntity) this.getInteriorDimension().getBlockEntity(doorPos);
//        assert door != null;
//        door.setTARDIS(this.tardis);
//
//        this.interiorDoorPos = doorPos;
//
//        return doorPos;
    }

    private BlockPos getOffsetDoorPosition() {
        BlockPos doorPos = this.getInteriorDoorPos();
        BlockPos adjustedPos = new BlockPos(0,0,0);
        Direction doorDirection = /*this.getInteriorDimension()
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

    public void teleportToDoor(PlayerEntity player) {
        if (this.needsGeneration()) {
            this.generate();
        }

        Direction doorDirection = /*this.getInteriorDimension()
                .getBlockState(
                        this.getInteriorDoorPos())
                .get(Properties.HORIZONTAL_FACING);*/ Direction.NORTH;

        TeleportHelper helper = new TeleportHelper(player.getUuid(),new AbsoluteBlockPos(this.getInteriorDimension(),doorDirection,this.getOffsetDoorPosition()));
        helper.teleport((ServerWorld) player.getWorld());
    }
    public void delete() {
        DesktopGenerator.InteriorGenerator generator = new DesktopGenerator.InteriorGenerator(this.tardis, (ServerWorld) this.getInteriorDimension(),this.getSchema());
//        generator.deleteInterior(); // @TODO no thread for deleting interiors yet, it was also pretty slow on box mod so maybe theres a faster way? - duzo
        this.interiorCornerPosList = null;
    }
    public void generate() {
        this.generate(this.getSchema());
    }
    public void generate(DesktopSchema schema) {
        this.interiorCornerPosList = DesktopUtil.getNextAvailableInteriorSpot();
        DesktopGenerator.InteriorGenerator generator = new DesktopGenerator.InteriorGenerator(this.tardis, (ServerWorld) this.getInteriorDimension(), schema);
        generator.placeStructure((ServerWorld) this.getInteriorDimension(), this.interiorCornerPosList.get(0), Direction.SOUTH);
    }
    public boolean needsGeneration() {
        return this.interiorCornerPosList == null;
    }
    private void loadCorners(NbtCompound tag) {
        AbsoluteBlockPos bottomLeft = new AbsoluteBlockPos(this.getInteriorDimension(), NbtHelper.toBlockPos(tag.getCompound("bottomLeft")));
        AbsoluteBlockPos topRight = new AbsoluteBlockPos(this.getInteriorDimension(),NbtHelper.toBlockPos(tag.getCompound("topRight")));

        if (this.interiorCornerPosList == null) return;

        this.interiorCornerPosList.set(0, bottomLeft);
        this.interiorCornerPosList.set(1, topRight);
    }

    public static class Serializer {
        private static final DesktopSchema.Serializer SCHEMA_SERIALIZER = new DesktopSchema.Serializer();

        public void serialize(NbtCompound tag, TARDISDesktop interior) {
            NbtCompound schema = new NbtCompound();
            SCHEMA_SERIALIZER.serialize(schema, interior.schema);

            if (interior.interiorDoorPos != null) {
                tag.put("doorPos", NbtHelper.fromBlockPos(interior.interiorDoorPos));
            }
            if (interior.interiorCornerPosList != null) {
                tag.put("bottomLeft", NbtHelper.fromBlockPos(interior.interiorCornerPosList.get(0)));
                tag.put("topRight", NbtHelper.fromBlockPos(interior.interiorCornerPosList.get(1)));
            }
            tag.put("schema", schema);
        }
        public NbtCompound serialize(TARDISDesktop interior) {
            NbtCompound nbt = new NbtCompound();
            this.serialize(nbt,interior);
            return nbt;
        }
        public TARDISDesktop deserialize(NbtCompound nbt) {
            return new TARDISDesktop(SCHEMA_SERIALIZER.deserialize(nbt.getCompound("schema")),nbt);
        }

    }
}

