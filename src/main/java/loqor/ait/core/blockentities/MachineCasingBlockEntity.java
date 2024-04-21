package loqor.ait.core.blockentities;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.util.AITModTags;
import loqor.ait.registry.MachineRecipeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MachineCasingBlockEntity extends BlockEntity {

    private final Set<ItemStack> parts = new HashSet<>();

    public MachineCasingBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.MACHINE_CASING_ENTITY_TYPE, pos, state);
    }

    public void onUse(World world, ItemStack stack, PlayerEntity player) {
        if (stack.isEmpty() && player.isSneaking()) {
            if (this.parts.isEmpty())
                return;

            Iterator<ItemStack> iterator = parts.iterator();

            ItemStack type = iterator.next();
            iterator.remove();

            world.spawnEntity(new ItemEntity(world, this.pos.getX(), this.pos.getY() + 1, this.pos.getZ(), type));
            return;
        }

        // Should this be in SonicItem.Mode.INTERACTION?
        if (SonicItem.isSonic(stack) && SonicItem.findMode(stack) == SonicItem.Mode.INTERACTION) {
            MachineRecipeRegistry.getInstance().findMatching(this.parts).ifPresent(schema -> {
                SonicItem.playSonicSounds(player);

                world.spawnEntity(new ItemEntity(world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), schema.output()));
                world.setBlockState(this.pos, Blocks.AIR.getDefaultState());
                this.markRemoved();
            });

            return;
        }

        this.parts.add(stack.copyWithCount(1));
        stack.decrement(1);
    }
}
