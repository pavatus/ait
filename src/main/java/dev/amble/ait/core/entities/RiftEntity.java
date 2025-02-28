package dev.amble.ait.core.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import dev.amble.ait.core.AITEntityTypes;
import dev.amble.ait.core.AITItems;
import dev.amble.ait.core.entities.base.DummyLivingEntity;


public class RiftEntity extends DummyLivingEntity {
    private int interactAmount = 0;
    public RiftEntity(EntityType<?> type, World world) {
        super(AITEntityTypes.RIFT_ENTITY, world, false);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        //this.getDataTracker().startTracking(this.getTracked(), Optional.empty());
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient()) return ActionResult.SUCCESS;
        if (interactAmount >= 3) {
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.BLOCK_SCULK_SHRIEKER_BREAK,
                    SoundCategory.MASTER, 1f, 1f);
            this.discard();
            return ActionResult.FAIL;
        }
        interactAmount += 1;
        if (this.getWorld().getRandom().nextBoolean()) {
            player.damage(this.getWorld().getDamageSources().hotFloor(), 4);
            spawnItem(this.getWorld(), this.getBlockPos(), new ItemStack(AITItems.CORAL_FRAGMENT));
            this.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.MASTER, 1f, 1f);
            this.discard();
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }

    public static void spawnItem(World world, BlockPos pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        world.spawnEntity(entity);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        interactAmount = nbt.getInt("interactAmount");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("interactAmount", interactAmount);
    }
}