package loqor.ait.mixin.server.structure;

import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureTemplate.class)
public interface StructureTemplateAccessor {
    @Accessor("blockInfoLists")
    List<StructureTemplate.PalettedBlockInfoList> getBlockInfoLists();

    @Accessor("entities")
    List<StructureTemplate.StructureEntityInfo> getEntities();

    @Accessor("size")
    Vec3i getSize();
}
