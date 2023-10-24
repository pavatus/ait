package mdteam.ait.core.helper.desktop;

import mdteam.ait.AITMod;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.structures.DesktopStructure;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class DesktopSchema extends DesktopStructure implements Serializable {
    protected AbsoluteBlockPos doorPosition;
    protected String id;
    protected transient StructureTemplate template;

    public DesktopSchema(Identifier location, String id) {
        super(id);
        setLocation(location);
        setID(id);
    }
    public DesktopSchema(String structureName) {
        this(new Identifier(AITMod.MOD_ID,"interiors/" + structureName), structureName);
    }

    public void setLocation(Identifier location) {
        this.location = location;
    }

    public StructureTemplate getTemplate() {
        return template;
    }

    public void setDoorPosition(AbsoluteBlockPos doorPosition) {
        this.doorPosition = doorPosition;
    }

    public AbsoluteBlockPos getDoorPosition() {
        return doorPosition;
    }

    public void setID(String string) {
        this.id = string;
    }
    public String getID() {
        return this.id;
    }
}
