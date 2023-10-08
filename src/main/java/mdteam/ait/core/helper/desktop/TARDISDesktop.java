package mdteam.ait.core.helper.desktop;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.DesktopGenerator;
import mdteam.ait.core.helper.TeleportHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.impl.container.ServerPlayerEntitySyncHook;
import net.minecraft.MinecraftVersion;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

import java.util.ArrayList;
import java.util.List;

public class TARDISDesktop {
    private DesktopSchema schema;
    private ExteriorBlockEntity tardisEntity;
    private List<AbsoluteBlockPos> interiorCornerPosList;
    private BlockPos interiorDoorPos;

    public TARDISDesktop(DesktopSchema schema) {
        this.schema = schema;
    }
    public TARDISDesktop(DesktopSchema schema, NbtCompound nbt) {
        this(schema);
        this.loadCorners(nbt);
    }

    public void link(ExteriorBlockEntity tardis) {
        this.tardisEntity = tardis;
    }
    public void setSchema(DesktopSchema schema) {
        this.schema = schema;
    }
    public DesktopSchema getSchema() {return this.schema;}
    public World getInteriorDimension() {
        if (this.tardisEntity == null) return null;

        return this.tardisEntity.getWorld().getServer().getWorld(AITDimensions.TARDIS_DIM_WORLD);
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
    private BlockPos searchForDoorPosAndUpdate() {
        // @TODO no door block entity
        return new BlockPos(0,0,0); // yum

        //BlockPos doorPos = this.interiorCornerPosList.get(0).offset(this.getSchema().getDoorPosition());
        //System.out.println(doorPos);

        //if (!(this.getInteriorDimension().getBlockState(doorPos).getBlock() instanceof DoorBlock)) {
        //    doorPos = TARDISManager.getInstance().searchForDoorBlock(this.interiorCornerPosList);
        //}

        //DoorBlockEntity door = (DoorBlockEntity) this.getInteriorDimension().getBlockEntity(doorPos);
        //assert door != null;
        //door.setTARDIS(this.tardis);

        //this.interiorDoorPos = doorPos;

        //return doorPos;
    }

    private BlockPos getOffsetDoorPosition() {
        BlockPos doorPos = this.getInteriorDoorPos();
        BlockPos adjustedPos = new BlockPos(0,0,0);
        Direction doorDirection = this.getInteriorDimension()
                .getBlockState(
                        doorPos)
                .get(Properties.HORIZONTAL_FACING);
//        switch(doorDirection) {
//            case NORTH -> adjustedPos = new BlockPos(doorPos.getX() + 0.5,doorPos.getY(),doorPos.getZ() - 1.5);
//            case SOUTH -> adjustedPos = new BlockPos(doorPos.getX() + 0.5,doorPos.getY(),doorPos.getZ() + 1.5);
//            case EAST -> adjustedPos = new BlockPos(doorPos.getX() + 1.5,doorPos.getY(),doorPos.getZ() + 0.5);
//            case WEST -> adjustedPos = new BlockPos(doorPos.getX() - 1.5,doorPos.getY(),doorPos.getZ() + 0.5);
//        }
        return adjustedPos;
    }

    public void teleportToDoor(PlayerEntity player) {
        if (this.needsGeneration()) {
            this.generate();
        }

        Direction doorDirection = this.getInteriorDimension()
                .getBlockState(
                        this.getInteriorDoorPos())
                .get(Properties.HORIZONTAL_FACING);

        TeleportHelper helper = new TeleportHelper(player.getUuid(),new AbsoluteBlockPos(this.getInteriorDimension(),doorDirection,this.getOffsetDoorPosition()));
        helper.teleport((ServerWorld) player.getWorld());
    }
    public void delete() {
        DesktopGenerator.InteriorGenerator generator = new DesktopGenerator.InteriorGenerator(this.tardisEntity, (ServerWorld) this.getInteriorDimension(),this.getSchema());
//        generator.deleteInterior(); // @TODO no thread for deleting interiors yet, it was also pretty slow on box mod so maybe theres a faster way? - duzo
        this.interiorCornerPosList = null;
    }
    public void generate() {
        this.generate(this.getSchema());
    }
    public void generate(DesktopSchema schema) {
        this.interiorCornerPosList = new ArrayList<>(); // TARDISManager.getInstance().getNextAvailableInteriorSpot(); // @TODO no TARDISManager and i cant be assed doing it right now just someone else copy it off my mod
        this.interiorCornerPosList.add(new AbsoluteBlockPos(this.getInteriorDimension(),new BlockPos(0,0,0)));
        this.interiorCornerPosList.add(new AbsoluteBlockPos(this.getInteriorDimension(),this.interiorCornerPosList.get(0).add(256,0,256)));


        DesktopGenerator.InteriorGenerator generator = new DesktopGenerator.InteriorGenerator(this.tardisEntity, (ServerWorld) this.getInteriorDimension(), schema);
        generator.placeStructure((ServerWorld) this.getInteriorDimension(), this.interiorCornerPosList.get(0), Direction.SOUTH);
    }
    public boolean needsGeneration() {
        return this.interiorCornerPosList == null;
    }
    private void loadCorners(NbtCompound tag) {
        AbsoluteBlockPos bottomLeft = new AbsoluteBlockPos(this.getInteriorDimension(), NbtHelper.toBlockPos(tag.getCompound("bottomLeft")));
        AbsoluteBlockPos topRight = new AbsoluteBlockPos(this.getInteriorDimension(),NbtHelper.toBlockPos(tag.getCompound("topRight")));

        this.interiorCornerPosList.set(0, bottomLeft);
        this.interiorCornerPosList.set(1, topRight);
    }

    public static class Serializer {
        private static final DesktopSchema.Serializer SCHEMA_SERIALIZER = new DesktopSchema.Serializer();

        public void serialize(NbtCompound tag, TARDISDesktop interior) {
            NbtCompound schema = new NbtCompound();
            SCHEMA_SERIALIZER.serialize(schema, interior.schema);

            tag.put("doorPos",NbtHelper.fromBlockPos(interior.interiorDoorPos));

            tag.put("bottomLeft", NbtHelper.fromBlockPos(interior.interiorCornerPosList.get(0)));
            tag.put("topRight", NbtHelper.fromBlockPos(interior.interiorCornerPosList.get(1)));

            tag.put("schema", schema);
        }
        public TARDISDesktop deserialize(NbtCompound nbt) {
            return new TARDISDesktop(SCHEMA_SERIALIZER.deserialize(nbt.getCompound("schema")),nbt);
        }

    }
}

