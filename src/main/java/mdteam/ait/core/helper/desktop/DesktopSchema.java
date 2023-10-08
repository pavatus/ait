package mdteam.ait.core.helper.desktop;

import mdteam.ait.AITMod;
import mdteam.ait.core.helper.structures.DesktopStructure;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class DesktopSchema extends DesktopStructure {
    protected BlockPos doorPosition;
    protected String id;
    protected StructureTemplate template;

    public DesktopSchema(Identifier location, String id) {
        super(id);
        this.location = location;
        this.id = id;
    }
    public DesktopSchema(String structureName) {
        this(new Identifier(AITMod.MOD_ID,"interiors/" + structureName), structureName);
    }

    @Deprecated
    public void findDoorPosition(StructureTemplate template) {
        NbtCompound tag = template.writeNbt(new NbtCompound());

        AtomicInteger i = new AtomicInteger();
        AtomicInteger interiorDoorNumberState = new AtomicInteger();

        tag.getList("pallete", tag.COMPOUND_TYPE).forEach((inbt -> {
            NbtCompound nbt = (NbtCompound) inbt;
            String name = nbt.getString("Name");

            // If the name is in the door list

//            if (Arrays.stream(TARDISInteriorDoors.INTERIOR_DOOR_BLOCK_ID_LIST).toArray(door -> name.equals(door))) {
//                interiorDoorNumberState.set(i.intValue());
//            }

            i.getAndIncrement();
        }));

        tag.getList("blocks", tag.COMPOUND_TYPE).forEach((inbt -> {
            NbtCompound nbt = (NbtCompound) inbt;
            int state = nbt.getInt("state");

            if (state == interiorDoorNumberState.get()) {
                NbtList posList = nbt.getList("pos", tag.INT_TYPE);
                doorPosition = new BlockPos(posList.getInt(0),posList.getInt(1),posList.getInt(2));
            }
        }));
    }
    @Deprecated
    public void place(ServerWorld level, BlockPos pos) {
        this.template.place(level,pos,pos,new StructurePlacementData(), Random.create(),2);
    }

    @Deprecated
    /**
     * This is to be ran when the TARDIS level loads so that the TARDIS level isnt returned as null
     */
    public void generateTemplate() {
        //this.template = DesktopUtil.getInteriorLevel(MinecraftClient.getInstance().getServer()).getStructureTemplateManager().getTemplate(location);
    }

    public void setDoorPosition(BlockPos pos) {
        this.doorPosition = pos;
    }
    public BlockPos getDoorPosition() {
        return this.doorPosition;
    }
    public String getID() {return this.id;}

    public static class Serializer {
        public void serialize(NbtCompound nbt, DesktopSchema interior) {
            nbt.putString("id", interior.id);
        }

       public DesktopSchema deserialize(NbtCompound nbt) {
           return DesktopInit.get(nbt.getString("id"));
       }
    }
}
