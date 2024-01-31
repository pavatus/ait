package mdteam.ait.mixin.server;

import com.google.common.collect.Lists;
import mdteam.ait.AITMod;
import mdteam.ait.core.entities.ConsoleControlEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin {

    @Shadow @Final private final List<StructureTemplate.StructureEntityInfo> entities = Lists.newArrayList();
    @Redirect(method = "saveFromWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/StructureTemplate;addEntitiesFromWorld(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)V", ordinal = 0))
    private void something(StructureTemplate instance, World world, BlockPos firstCorner, BlockPos secondCorner) {
        List<Entity> list = world.getEntitiesByClass(Entity.class, new Box(firstCorner, secondCorner), (entityx) -> {
            return !(entityx instanceof PlayerEntity) && !(entityx instanceof ConsoleControlEntity);
        });
        this.entities.clear();

        Vec3d vec3d;
        NbtCompound nbtCompound;
        BlockPos blockPos;
        for(Iterator var5 = list.iterator(); var5.hasNext(); this.entities.add(new StructureTemplate.StructureEntityInfo(vec3d, blockPos, nbtCompound.copy()))) {
            Entity entity = (Entity)var5.next();
            vec3d = new Vec3d(entity.getX() - (double)firstCorner.getX(), entity.getY() - (double)firstCorner.getY(), entity.getZ() - (double)firstCorner.getZ());
            nbtCompound = new NbtCompound();
            entity.saveNbt(nbtCompound);
            if (entity instanceof PaintingEntity) {
                blockPos = ((PaintingEntity)entity).getDecorationBlockPos().subtract(firstCorner);
            } else {
                blockPos = BlockPos.ofFloored(vec3d);
            }
        }
    }
}
